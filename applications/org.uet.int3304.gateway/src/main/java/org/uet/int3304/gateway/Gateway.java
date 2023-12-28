package org.uet.int3304.gateway;

import java.net.ServerSocket;
import java.net.Socket;

public class Gateway {
    private int PORT = 8080;

    public void start() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                System.out.println("Connected to node on " + socket.getInetAddress() + ":" + socket.getPort());
                GatewayRunnable client = new GatewayRunnable(socket);
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
