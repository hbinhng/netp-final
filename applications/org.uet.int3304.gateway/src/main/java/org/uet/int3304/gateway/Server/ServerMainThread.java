package org.uet.int3304.gateway.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.uet.int3304.gateway.AppConfig.Config;

public class ServerMainThread implements Runnable {
  private final int capacity;

  private final ServerSocket server;

  public ServerMainThread(ServerSocket server) {
    capacity = Config.getInstance().getMaxWorkers();
    this.server = server;
  }

  @Override
  public void run() {
    System.out.printf("Server life cycle method started, client capacity: %d\n", capacity);

    var connectionCount = 0l;
    var gatewayServer = GatewayServer.getInstance();

    while (true) {
      if (Thread.interrupted())
        break;

      if (gatewayServer.full()) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException exception) {
          return;
        }

        continue;
      }

      System.out.println("Waiting for new client ...");

      Socket socket;

      try {
        socket = server.accept();
      } catch (IOException exception) {
        if (Thread.interrupted())
          break;

        System.err.println("Failed to accept new client");
        System.err.println(exception.getMessage());

        continue;
      }

      System.out.println("New client connected");

      ServerWorkerThread worker = null;

      try {
        worker = new ServerWorkerThread(socket, connectionCount++);
      } catch (IOException exception) {
        System.err.println("Cannot initialize worker thread");
        System.err.println(exception.getMessage());
      }

      GatewayServer.getInstance().registerWorker(worker);
    }

    System.out.println("Shuting down main thread");
  }
}