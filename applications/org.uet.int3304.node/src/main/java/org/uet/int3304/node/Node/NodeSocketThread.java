package org.uet.int3304.node.Node;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import org.uet.int3304.node.AppConfig.Config;
import org.uet.int3304.node.AppConfig.NodeType;

public class NodeSocketThread implements Runnable {
  private static final Map<NodeType, Integer> NODE_TYPE_TO_PACKET = Map.ofEntries(
      Map.entry(NodeType.Temperature, 1),
      Map.entry(NodeType.BloodPressure, 2),
      Map.entry(NodeType.Heartbeat, 3));

  private final Socket internal;

  private final BufferedInputStream ingress;
  private final DataOutputStream egress;

  public NodeSocketThread(Socket socket) throws IOException {
    internal = socket;

    ingress = new BufferedInputStream(socket.getInputStream());
    egress = new DataOutputStream(socket.getOutputStream());
  }

  private void sendContent(String content) {
    try {
      egress.write(content.getBytes());
    } catch (IOException ignored) {
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

    while (true) {
      // Wait for config
    }
  }
}
