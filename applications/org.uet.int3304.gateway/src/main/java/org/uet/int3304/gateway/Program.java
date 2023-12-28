package org.uet.int3304.gateway;

import org.uet.int3304.gateway.AppConfig.Config;
import org.uet.int3304.gateway.UI.GatewayUI;

public class Program {
    public static void main(String[] args) {
        var config = Config.getInstance();
        config.loadConfig();
        
        if (!config.getHeadless()) {
            GatewayUI.launch(args);
            // Test push data 
            while(true) {
                GatewayUI.getInstance().pushTData(36.5);
                GatewayUI.getInstance().pushHBData(80);
                GatewayUI.getInstance().pushBPData(120, 80);
            }
            // End test push data
        }
    }
}
