package org.uet.int3304.gateway.UI;

import java.util.HashMap;
import java.util.Map;

import org.uet.int3304.gateway.bucket.Bucket;

public class GatewayUIState {
  private static final Object lock = new Object();
  private static GatewayUIState instance;

  private final Map<String, Bucket> buckets;

  public GatewayUIState() {
    buckets = new HashMap<>();
  }

  public void registerBucket(String id, Bucket bucket) {
    buckets.put(id, bucket);
  }

  public void write(String bucketId, double value) {
    var bucket = buckets.get(bucketId);

    if (bucket == null)
      return;

    bucket.pushData(value);
  }

  public static GatewayUIState getInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null)
          instance = new GatewayUIState();
      }

    return instance;
  }
}
