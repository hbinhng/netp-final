package org.uet.int3304.gateway.AppConfig;

public class Config {
  private static Object lock = new Object();
  private static Config instance;

  private int port;
  private long dataPollInterval;

  private Config() {
  }

  public void loadConfig() {
    var overlappedConfigurations = new ConfigLoader().load();

    port = Integer.parseInt(overlappedConfigurations.get("PORT"));
    dataPollInterval = Long.parseLong(overlappedConfigurations.get("DATA_INT"));
  }

  public int getPort() {
    return port;
  }

  public long getDataPollInterval() {
    return dataPollInterval;
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