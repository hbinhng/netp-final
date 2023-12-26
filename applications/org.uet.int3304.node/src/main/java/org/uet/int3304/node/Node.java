package org.uet.int3304.node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.uet.int3304.node.Generator.Generator;

public class Node implements Runnable {
    private int GATEWAY_PORT = 8080;
    private String GATEWAY_HOST = "localhost";
    private Generator<Float> generator;
    private String type;
    private int delayTime = 1000;

    public Node(Generator<Float> generator, String type) {
        this.generator = generator;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(GATEWAY_HOST, GATEWAY_PORT);
            System.out.println("Connected to gateway on " + socket.getInetAddress() + ":" + socket.getPort());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            while (true) {
                Float number = generator.next();
                Data data = new Data(type, number);
                out.writeUTF(data.toString());
                System.out.println("Type: " + type + " send: " + number);
    
                int serverResponse = in.readInt();
    
                switch (serverResponse) {
                    case 1:
                        delayTime += 1000;
                        break;
                    case -1:
                        delayTime = Math.max(0, delayTime - 1000);
                        break;
                    case 0:
                        break;
                }
                System.out.println("Gateway control:" + serverResponse);

                if (delayTime == 0) {
                    break;
                }
                Thread.sleep(delayTime);
                out.flush();
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
