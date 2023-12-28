package org.uet.int3304.gateway.AppConfig;

public class Config {
  private static Object lock = new Object();
  private static Config instance;

  private int port;
  private int maxWorkers;
  private long dataPollInterval;
  private boolean headless;

  private Config() {
  }

  public void loadConfig() {
    var overlappedConfigurations = new ConfigLoader().load();

    port = Integer.parseInt(overlappedConfigurations.getOrDefault("PORT", "8080"));
    maxWorkers = Integer.parseInt(overlappedConfigurations.getOrDefault("WORKER", "10"));
    dataPollInterval = Long.parseLong(overlappedConfigurations.getOrDefault("DATA_INT", "500"));
    headless = overlappedConfigurations.getOrDefault("HEADLESS", "false").trim().equals("true");
  }

  public int getPort() {
    return port;
  }

  public int getMaxWorkers() {
    return maxWorkers;
  }

  public long getDataPollInterval() {
    return dataPollInterval;
  }

  public boolean getHeadless() {
    return headless;
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
