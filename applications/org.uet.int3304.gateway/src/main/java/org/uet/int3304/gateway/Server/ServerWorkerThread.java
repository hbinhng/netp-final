package org.uet.int3304.gateway.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.uet.int3304.gateway.Group.GroupManager;
import org.uet.int3304.gateway.UI.BucketId;
import org.uet.int3304.gateway.UI.GatewayUIState;
import org.uet.int3304.gateway.UI.NodeState;

public class ServerWorkerThread implements Runnable {
  private static final String UNKNOWN_SENDER = "100 Who are you?\n";
  private static final String UNKNOWN_COMMAND_ERROR = "101 Unknown command\n";
  private static final String MALFORMED_ARGUMENTS = "102 Malformed arguments\n";
  private static final String GREET_REPLY = "200 Pong\n";
  private static final String GROUP_AND_NODE_REGISTERED = "300 Group and node registered\n";
  private static final String NODE_REGISTERED_TO_GROUP = "301 Node registered to a group\n";
  private static final String DATA_SOURCE_ALREADY_EXISTS = "309 Group already has similar data source\n";
  private static final String NODE_UNREGISTERED = "310 Node unregistered\n";
  private static final String NODE_ALREADY_UNREGISTERED = "311 Node already unregistered\n";
  private static final String NODE_NOT_REGISTERED = "401 Node is not registered\n";
  private static final String DATA_RECEIVED = "500 Data received\n";
  private static final String BYE = "501 OK bye\n";

  private final long connectionId;

  private BufferedReader ingress;
  private DataOutputStream egress;

  private boolean greeted;
  private String group;
  private String bucketId;

  private final GroupManager groupManager;
  private final Socket internal;
  private final NodeState nodeState;

  public ServerWorkerThread(Socket socket, long connectionId) throws IOException {
    internal = socket;
    this.connectionId = connectionId;

    ingress = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    egress = new DataOutputStream(socket.getOutputStream());

    greeted = false;

    groupManager = GroupManager.getInstance();
    nodeState = NodeState.getInstance();
  }

  private String readContent() {
    String content = null;

    try {
      content = ingress.readLine();
    } catch (IOException ex) {
      return null;
    }

    return content;
  }

  private void sendContent(String content) {
    try {
      egress.write(content.getBytes());
    } catch (IOException exception) {
      System.err.println("Cannot send message to client");
      System.err.println(exception.getMessage());
    }
  }

  private void handlePing() {
    greeted = true;

    sendContent(GREET_REPLY);
  }

  private void handleRegisterRequest(String[] tokens) {
    if (!greeted) {
      sendContent(UNKNOWN_SENDER);
      return;
    }

    if (tokens.length != 3) {
      sendContent(MALFORMED_ARGUMENTS);
      return;
    }

    String bucketId;

    switch (tokens[1]) {
      case "1":
        bucketId = BucketId.TEMP_BUCKET;
        break;
      case "2":
        bucketId = BucketId.SYSTOLIC_BUCKET;
        break;
      case "3":
        bucketId = BucketId.HB_BUCKET;
        break;
      default:
        sendContent(MALFORMED_ARGUMENTS);
        return;
    }

    var group = tokens[2];

    int registration = groupManager.registerNode(group, bucketId, connectionId);

    switch (registration) {
      case 2:
        sendContent(GROUP_AND_NODE_REGISTERED);
        break;

      case 1:
        sendContent(NODE_REGISTERED_TO_GROUP);
        break;

      default:
        sendContent(DATA_SOURCE_ALREADY_EXISTS);
        return;
    }

    System.out.printf("Connection [%d] registered to group [%s] as [%s] data source\n",
        connectionId, group, bucketId);

    this.bucketId = bucketId;
    this.group = group;
  }

  private void handleUnregisterRequest() {
    if (group == null || bucketId == null) {
      sendContent(NODE_ALREADY_UNREGISTERED);
      return;
    }

    groupManager.unregisterNode(group, bucketId);

    System.out.printf("Connection [%d] unregistered from group [%s] as [%s] data source\n",
        connectionId, group, bucketId);

    group = null;
    bucketId = null;

    sendContent(NODE_UNREGISTERED);
  }

  private void handleConfigureRequest(String[] tokens) {
    if (!greeted) {
      sendContent(UNKNOWN_SENDER);
      return;
    }
  }

  private void handleDataRequest(String[] tokens) {
    if (!greeted) {
      sendContent(UNKNOWN_SENDER);
      return;
    }

    if (group == null || bucketId == null) {
      sendContent(NODE_NOT_REGISTERED);
      return;
    }

    if (tokens.length != 2) {
      sendContent(MALFORMED_ARGUMENTS);
      return;
    }

    float data;

    try {
      data = Float.parseFloat(tokens[1]);
    } catch (NumberFormatException ignored) {
      sendContent(MALFORMED_ARGUMENTS);
      return;
    }

    var selectedGroup = nodeState.getGroup();

    if (selectedGroup != null && group.equals(selectedGroup))
      GatewayUIState.getInstance().write(bucketId, data);

    sendContent(DATA_RECEIVED);
  }

  private void handleQuit() {
    sendContent(BYE);
  }

  public long getConnectionId() {
    return connectionId;
  }

  @Override
  public void run() {
    System.out.println("Worker thread initialized");

    while (true) {
      if (Thread.interrupted())
        break;

      var content = readContent();

      if (content == null) {
        System.out.printf("Client on connection [%d] unexpectedly closed connection\n", connectionId);
        break;
      }

      var tokens = content.split("\\s+");

      if (tokens.length == 0) {
        sendContent(UNKNOWN_COMMAND_ERROR);
        continue;
      }

      var quit = false;

      switch (tokens[0]) {
        case "ping":
          handlePing();
          break;
        case "register":
          handleRegisterRequest(tokens);
          break;
        case "unregister":
          handleUnregisterRequest();
          break;
        case "configure":
          handleConfigureRequest(tokens);
          break;
        case "data":
          handleDataRequest(tokens);
          break;
        case "quit":
          handleQuit();
          quit = true;
          break;
        default:
          sendContent(UNKNOWN_COMMAND_ERROR);
          break;
      }

      if (quit) {
        System.out.printf("Client on connection [%d] disconnected\n", connectionId);
        break;
      }
    }

    groupManager.unregisterNode(group, bucketId);

    System.out.printf("Connection [%d] unregistered from group [%s] as [%s] data source\n",
        connectionId, group, bucketId);

    if (!internal.isClosed()) {
      try {
        internal.close();
      } catch (IOException exception) {
        System.err.printf("Cannot close connection [%d]\n", connectionId);
        System.err.println(exception.getMessage());
      }
    }

    GatewayServer.getInstance().disposeWorker(connectionId);
  }

  public void close() {
    try {
      internal.close();
    } catch (IOException exception) {
      System.err.printf("Cannot close socket on [%d] connection\n", connectionId);
      System.err.println(exception.getMessage());
    }
  }
}
