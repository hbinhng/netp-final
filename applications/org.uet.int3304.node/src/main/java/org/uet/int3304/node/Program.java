package org.uet.int3304.node;

import org.uet.int3304.node.AppConfig.Config;
import org.uet.int3304.node.Generator.ECGSignalGenerator;
import org.uet.int3304.node.Generator.TemperatureGenerator;

public class Program {
    public static void main(String[] args) {
        Config.getInstance().loadConfig();
        Node temperatureNode = new Node(new TemperatureGenerator(), "Temperature");
        Thread temperatureThread = new Thread(temperatureNode);
        Node ecgNode = new Node(new ECGSignalGenerator(), "ECGSignal");
        Thread ecgThread = new Thread(ecgNode);

        temperatureThread.start();
        ecgThread.start();
    }
}
