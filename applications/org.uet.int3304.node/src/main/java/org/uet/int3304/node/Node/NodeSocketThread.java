package org.uet.int3304.node.Node;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

import org.uet.int3304.node.AppConfig.Config;
import org.uet.int3304.node.AppConfig.NodeType;

public class NodeSocketThread implements Runnable {
  private static final Map<NodeType, Integer> NODE_TYPE_TO_PACKET = Map.ofEntries(
      Map.entry(NodeType.Temperature, 1),
      Map.entry(NodeType.BloodPressure, 2),
      Map.entry(NodeType.Heartbeat, 3));

  private final BufferedReader ingress;
  private final DataOutputStream egress;

  public NodeSocketThread(Socket socket) throws IOException {
    ingress = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    egress = new DataOutputStream(socket.getOutputStream());
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
    } catch (IOException ignored) {
    }
  }

  private void parseConfigResponse(String content) {
    var tokens = content.split("\\s+");

    if (tokens.length < 2) {
      return;
    }

    if (!tokens[0].equals("400")) {
      return;
    }

    for (var token : tokens) {
      var segments = token.split("=");

      if (segments.length != 2)
        continue;

      if (segments[0].equals("DATA_INT")) {
        try {
          var newDataInterval = Long.parseLong(segments[1]);
          NodeState.getInstance().setDataInterval(newDataInterval);
        } catch (NumberFormatException ignored) {
        }
      }
    }
  }

  @Override
  public void run() {
    sendContent("ping\n");

    var config = Config.getInstance();

    var registerCommand = String.format(
        "register %d %s\n",
        NODE_TYPE_TO_PACKET.get(config.getNodeType()),
        config.getGroup());

    sendContent(registerCommand);

    sendContent("configure\n");

    var content = readContent();

    if (content == null) {
      System.err.println("Did not receive anything from server");
      System.err.println("Exitting");
      System.exit(-1);
    }

    parseConfigResponse(content);

    while (true) {
      content = readContent();

      if (content == null) {
        System.out.println("Connection closed unexpectedly by remote");
        break;
      }

      parseConfigResponse(content);
    }

    System.out.println("Socket thread terminated");
  }
}
