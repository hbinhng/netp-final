package org.uet.int3304.gateway.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerWorkerThread implements Runnable {
  private final long connectionId;

  private BufferedReader ingress;
  private DataOutputStream egress;

  public ServerWorkerThread(Socket socket, long connectionId) throws IOException {
    this.connectionId = connectionId;

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

      try {
        egress.write(content.getBytes());
      } catch (IOException exception) {
        System.err.println("Cannot send message to client");
        System.err.println(exception.getMessage());
      }
    }

    GatewayServer.getInstance().disposeWorker(connectionId);
  }
}
