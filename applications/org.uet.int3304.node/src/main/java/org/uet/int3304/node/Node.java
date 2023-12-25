package org.uet.int3304.node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.uet.int3304.node.Generator.Generator;

public class Node implements Runnable {
    private int GATEWAY_PORT = 8080;
    private String GATEWAY_HOST = "localhost";
    private Generator<Float> generator;

    public Node(Generator<Float> generator) {
        this.generator = generator;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(GATEWAY_HOST, GATEWAY_PORT);
            System.out.println("Connected to gateway on " + socket.getInetAddress() + ":" + socket.getPort());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            for (int i = 0; i < 10; i++) {
                Float number = generator.next();
                out.writeFloat(number);
                System.out.println("Sent: " + number);
                out.flush();
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
