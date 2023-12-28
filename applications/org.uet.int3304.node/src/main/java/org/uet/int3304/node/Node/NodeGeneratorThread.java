package org.uet.int3304.node.Node;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NodeGeneratorThread implements Runnable {
  private final Socket internal;
  private final DataOutputStream egress;

  private final long spawn;

  public NodeGeneratorThread(Socket socket) throws IOException {
    internal = socket;
    egress = new DataOutputStream(internal.getOutputStream());

    spawn = System.currentTimeMillis();
  }

  @Override
  public void run() {
    var next = spawn;
    var generator = NodeClient.getInstance().getGenerator();
    var state = NodeState.getInstance();

    while (true) {
      var now = System.currentTimeMillis();

      if (now < next) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException ignored) {
          break;
        }

        continue;
      }

      var data = generator.next();

      // TODO: Do something with data

      var diff = now - spawn;
      var interval = state.getDataInterval();

      if (diff % interval == 0)
        next = now + interval;
      else
        next = now - (diff % interval) + interval;
    }
  }
}
