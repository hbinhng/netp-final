package org.uet.int3304.node.Node;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NodeSocketThread implements Runnable {
  private final Socket internal;

  private final BufferedInputStream ingress;
  private final DataOutputStream egress;

  public NodeSocketThread(Socket socket) throws IOException {
    internal = socket;

    ingress = new BufferedInputStream(socket.getInputStream());
    egress = new DataOutputStream(socket.getOutputStream());
  }

  @Override
  public void run() {
    while (true) {
    }
  }
}
