package org.uet.int3304.gateway;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class GatewayRunnable implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    // private boolean isHello = false;

    public GatewayRunnable(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    private void handleRequest(String request) throws IOException {

    }

    @Override
    public void run() {
        try {
            Float number;
            while ((number = in.readFloat()) != null) {
                System.out.println("Received: " + number);
                // handleRequest(request);
                // if (request.equals("QUIT")) {
                // break;
                // }
                out.flush();
            }
            socket.close();
            Thread.currentThread().join();
        } catch (EOFException e) {
            System.out.println("Client disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
