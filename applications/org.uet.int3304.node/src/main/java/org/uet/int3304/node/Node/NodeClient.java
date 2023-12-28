package org.uet.int3304.node.Node;

import java.io.IOException;
import java.net.Socket;

import org.uet.int3304.node.AppConfig.Config;
import org.uet.int3304.node.Generator.BloodPressureGenerator;
import org.uet.int3304.node.Generator.ECGSignalGenerator;
import org.uet.int3304.node.Generator.Generator;
import org.uet.int3304.node.Generator.TemperatureGenerator;

public class NodeClient {
  private static final Object lock = new Object();
  private static NodeClient instance;

  private final Socket internal;

  private final Generator<Float> generator;

  public NodeClient() throws IOException {
    var config = Config.getInstance();

    var host = config.getGatewayHost();
    var port = config.getGatewayPort();
    var nodeType = config.getNodeType();

    internal = new Socket(host, port);

    switch (nodeType) {
      case BloodPressure:
        generator = new BloodPressureGenerator();
        break;

      case Temperature:
        generator = new TemperatureGenerator();
        break;

      case Heartbeat:
        generator = new ECGSignalGenerator();
        break;

      default:
        generator = null;
        break;
    }

    System.out.printf("Connected to server at %s:%d\n", host, port);
  }

  public void lifeCycle() {
    try {
      var thread = new Thread(new NodeMainThread(internal));

      thread.start();
    } catch (IOException exception) {
      System.err.println("Cannot start connection thread");
      System.err.println(exception.getMessage());
      System.exit(-1);
    }

    try {
      var thread = new Thread(new NodeGeneratorThread(internal));

      thread.start();
    } catch (IOException exception) {
      System.err.println("Cannot start generator thread");
      System.err.println(exception.getMessage());
      System.exit(-1);
    }
  }

  public Generator<Float> getGenerator() {
    return generator;
  }

  public static NodeClient getInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null)
          try {
            instance = new NodeClient();
          } catch (IOException exception) {
            System.err.println("Cannot connect to gateway");
            System.err.println(exception.getMessage());
            System.exit(-1);
          }
      }

    return instance;
  }
}
