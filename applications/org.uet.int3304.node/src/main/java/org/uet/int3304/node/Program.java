package org.uet.int3304.node;

import org.uet.int3304.node.AppConfig.Config;
import org.uet.int3304.node.Node.NodeClient;

public class Program {
  public static void main(String[] args) {
    Config.getInstance().loadConfig();

    NodeClient.getInstance().lifeCycle();
  }
}
