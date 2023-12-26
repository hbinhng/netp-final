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
            String data;
            int count = 3;
            while ((data = in.readUTF()) != null) {
                System.out.println("Received: " + data);
                // handleRequest(request);
                // if (request.equals("QUIT")) {
                // break;
                // }

                //Demo gateway sinh dữ liệu điều khiển IoT node
                if (count > 0) {
                    count--;
                    out.writeInt(1);
                } else out.writeInt(-1);
                
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
