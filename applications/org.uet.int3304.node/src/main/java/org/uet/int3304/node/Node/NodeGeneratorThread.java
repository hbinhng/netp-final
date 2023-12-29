package org.uet.int3304.node.Node;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class NodeGeneratorThread implements Runnable {
  private class DataTransmitTask extends TimerTask {
    private final DataOutputStream egress;
    private final float data;

    public DataTransmitTask(float data, DataOutputStream egress) {
      this.data = data;
      this.egress = egress;
    }

    @Override
    public void run() {
      var request = String.format("data %f\n", data);

      try {
        egress.write(request.getBytes());
      } catch (IOException ignored) {
      }
    }
  }

  private final Socket internal;
  private final DataOutputStream egress;
  private final Timer timer;

  private final long spawn;

  public NodeGeneratorThread(Socket socket) throws IOException {
    internal = socket;
    egress = new DataOutputStream(internal.getOutputStream());

    spawn = System.currentTimeMillis();
    timer = new Timer();
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

      // Delayed data transmit on other thread to prevent blocking this thread
      timer.schedule(new DataTransmitTask(data, egress), 0);

      var diff = now - spawn;
      var interval = state.getDataInterval();

      if (diff % interval == 0)
        next = now + interval;
      else
        next = now - (diff % interval) + interval;
    }
  }
}
