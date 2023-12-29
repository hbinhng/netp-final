package org.uet.int3304.gateway;

import org.uet.int3304.gateway.AppConfig.Config;
import org.uet.int3304.gateway.Server.GatewayServer;
import org.uet.int3304.gateway.UI.GatewayUI;

public class Program {
  public static void main(String[] args) {
    var config = Config.getInstance();
    config.loadConfig();

    GatewayServer.getInstance().lifeCycle();

    if (!config.getHeadless())
      GatewayUI.launch(args);
  }

  public static void cleanUp() {
    GatewayServer.getInstance().close();
  }
}
