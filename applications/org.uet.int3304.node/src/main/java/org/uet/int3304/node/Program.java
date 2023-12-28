package org.uet.int3304.node;

import org.uet.int3304.node.Generator.ECGSignalGenerator;
import org.uet.int3304.node.Generator.TemperatureGenerator;

public class Program {
    public static void main(String[] args) {
        Node node1 = new Node(new TemperatureGenerator());
        Thread thread1 = new Thread(node1);
        Node node2 = new Node(new ECGSignalGenerator());
        Thread thread2 = new Thread(node2);
        thread1.start();
        thread2.start();
    }
}
