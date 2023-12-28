package org.uet.int3304.gateway.AppConfig;

import java.util.HashMap;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigLoader {
  public ConfigLoader() {

  }

  private void overrideMap(Map<String, String> accumulator, Dotenv env) {
    for (var entry : env.entries())
      accumulator.put(entry.getKey(), entry.getValue());
  }

  public Map<String, String> load() {
    var result = new HashMap<String, String>();

    var defaultEnv = Dotenv.configure()
        .directory(System.getProperty("user.dir"))
        .filename(".env")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();

    this.overrideMap(result, defaultEnv);

    var localEnv = Dotenv.configure()
        .directory(System.getProperty("user.dir"))
        .filename(".env.local")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();

    this.overrideMap(result, localEnv);

    var systemEnv = System.getenv();

    for (var entry : systemEnv.entrySet())
      result.put(entry.getKey(), entry.getValue());

    return result;
  }
}
