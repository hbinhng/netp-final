package org.uet.int3304.gateway.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.AbstractMap.SimpleEntry;

import org.uet.int3304.gateway.AppConfig.Config;
import org.uet.int3304.gateway.UI.NodeState;

public class GatewayServer {
  private static final Object lock = new Object();
  private static GatewayServer instance;

  private final int capacity;
  private final Map<Long, SimpleEntry<Thread, ServerWorkerThread>> connections;
  private final ServerSocket internal;
  private Thread mainThread;

  private GatewayServer() throws IOException {
    var config = Config.getInstance();

    var port = config.getPort();
    capacity = config.getMaxWorkers();

    internal = new ServerSocket(
        port, 4, InetAddress.getByName("0.0.0.0"));
    internal.setReuseAddress(true);

    if (internal.isBound() && !internal.isClosed())
      System.out.printf("Server is listening on :%d\n", port);
    else
      throw new IOException("Cannot bind server");

    connections = new HashMap<>(config.getMaxWorkers());
  }

  public void lifeCycle() {
    var serverMainThread = new ServerMainThread(internal);

    mainThread = new Thread(serverMainThread);

    mainThread.start();
  }

  public void close() {
    System.out.println("Closing server");
    mainThread.interrupt();

    synchronized (connections) {
      for (var thread : connections.values()) {
        thread.getKey().interrupt();
        thread.getValue().close();
      }
    }

    try {
      internal.close();
    } catch (IOException exception) {
      System.err.println("Cannot close socket server");
      System.err.println(exception.getMessage());
    }
  }

  public void registerWorker(ServerWorkerThread worker) {
    var thread = new Thread(worker);

    thread.setName(String.format("Worker#%d", worker.getConnectionId()));

    thread.start();

    connections.put(worker.getConnectionId(), new SimpleEntry<>(thread, worker));
    System.out.printf("Connection pool: %d/%d\n", connections.size(), capacity);
  }

  public void disposeWorker(long connectionId) {
    connections.remove(connectionId);
  }

  public boolean full() {
    return connections.size() >= capacity;
  }

  public void configure() {
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        var dataInterval = NodeState.getInstance().getDataInterval();

        synchronized (lock) {
          for (var connection : connections.values()) {
            var worker = connection.getValue();

            worker.configure(dataInterval);
          }
        }
      }
    }, 0l);
  }

  public void configure(long connectionId) {
  }

  public static GatewayServer getInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null)
          try {
            instance = new GatewayServer();
          } catch (IOException exception) {
            System.err.println("Cannot create server");
            System.err.println(exception.getMessage());
            System.exit(-1);
          }
      }

    return instance;
  }
}
