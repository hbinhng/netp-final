package org.uet.int3304.node.AppConfig;

public class Config {
  private static Object lock = new Object();
  private static Config instance;

  private String gatewayHost;
  private int gatewayPort;
  private long dataInterval;
  private NodeType nodeType;
  private String group;

  private Config() {
  }

  public void loadConfig() {
    var overlappedConfigurations = new ConfigLoader().load();

    gatewayHost = overlappedConfigurations.getOrDefault("GW_ADDR", "127.0.0.1").trim();
    gatewayPort = Integer.parseInt(overlappedConfigurations.getOrDefault("GW_PORT", "8080"));
    dataInterval = Long.parseLong(overlappedConfigurations.getOrDefault("DATA_INT", "500"));

    var rawNodeType = overlappedConfigurations.getOrDefault("NODE_TYPE", "TP");

    if (rawNodeType.equals("TP"))
      nodeType = NodeType.Temperature;
    else if (rawNodeType.equals("BP"))
      nodeType = NodeType.BloodPressure;
    else if (rawNodeType.equals("HB"))
      nodeType = NodeType.Heartbeat;
    else {
      System.err.printf("Unknown node type '%s'\n", rawNodeType);
      System.exit(-1);
    }

    group = overlappedConfigurations.getOrDefault("NODE_GROUP", "default");
  }

  public String getGatewayHost() {
    return gatewayHost;
  }

  public int getGatewayPort() {
    return gatewayPort;
  }

  public long getDataInterval() {
    return dataInterval;
  }

  public NodeType getNodeType() {
    return nodeType;
  }

  public String getGroup() {
    return group;
  }

  public static Config getInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null)
          instance = new Config();
      }

    return instance;
  }
}
