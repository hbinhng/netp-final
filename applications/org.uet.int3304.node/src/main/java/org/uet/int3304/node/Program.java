package org.uet.int3304.node;

public class Program {

  public static void main(String[] args) {
    String type = args[0];
    String gatewayIpAddress = args[1];
    String gatewayPort = args[2];

    System.out.println("type: " + type);
    System.out.println("gatewayIpAddress: " + gatewayIpAddress);
    System.out.println("gatewayPort: " + gatewayPort);

    System.out.println("Hello World!");
  }
}
