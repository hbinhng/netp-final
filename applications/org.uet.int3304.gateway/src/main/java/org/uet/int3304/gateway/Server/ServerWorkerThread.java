package org.uet.int3304.gateway.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.uet.int3304.gateway.UI.BucketId;
import org.uet.int3304.gateway.UI.GatewayUIState;

public class ServerWorkerThread implements Runnable {
  private static final String UNKNOWN_SENDER = "100 Who are you?\n";
  private static final String UNKNOWN_COMMAND_ERROR = "101 Unknown command\n";
  private static final String MALFORMED_ARGUMENTS = "102 Malformed arguments\n";
  private static final String DATA_SOURCE_ALREADY_EXISTS = "309 Group already has similar data source\n";
  private static final String NODE_NOT_REGISTERED = "401 Node is not registered\n";
  private static final String DATA_RECEIVED = "500 Data received\n";
  private static final String GREET_REPLY = "200 Pong\n";

  private final long connectionId;

  private BufferedReader ingress;
  private DataOutputStream egress;

  private boolean greeted;
  private String group;
  private String bucketId;

  public ServerWorkerThread(Socket socket, long connectionId) throws IOException {
    this.connectionId = connectionId;

    ingress = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    egress = new DataOutputStream(socket.getOutputStream());

    greeted = false;
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

    // Only write to bucket when current group matches with selected
    // group on GUI.
    if (group.equals("SELECTED_GROUP_ON_GUI"))
      GatewayUIState.getInstance().write(bucketId, data);

    sendContent(DATA_RECEIVED);
  }

  public long getConnectionId() {
    return connectionId;
  }

  @Override
  public void run() {
    System.out.println("Worker thread initialized");

    while (true) {
      var content = readContent();

      if (content == null) {
        System.out.println("Client unexpectedly closed connection");
        break;
      }

      var tokens = content.split("\\s+");

      if (tokens.length == 0) {
        sendContent(UNKNOWN_COMMAND_ERROR);
        continue;
      }

      switch (tokens[0]) {
        case "ping":
          handlePing();
          break;
        case "register":
          handleRegisterRequest(tokens);
          break;
        case "configure":
          handleConfigureRequest(tokens);
          break;
        case "data":
          handleDataRequest(tokens);
          break;
        default:
          sendContent(UNKNOWN_COMMAND_ERROR);
      }
    }

    GatewayServer.getInstance().disposeWorker(connectionId);
  }
}
