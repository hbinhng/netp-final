package org.uet.int3304.gateway;

import org.uet.int3304.gateway.AppConfig.Config;
import org.uet.int3304.gateway.UI.GatewayUI;

public class Program {
    public static void main(String[] args) {
        var config = Config.getInstance();
        config.loadConfig();

        if (!config.getHeadless())
            GatewayUI.launch(args);
    }
}
