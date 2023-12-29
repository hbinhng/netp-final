package org.uet.int3304.gateway.Bucket;

import java.util.LinkedList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

/**
 * This bucket holds data with a retention policy
 * data can be pulled out in a frame of time
 * and all data in each frame will be calculated
 * using aggregate function.
 */
public class Bucket {
  private final Object lock;
  private final long retention;
  private final List<SimpleEntry<Long, Double>> data;

  /**
   * 
   * @param retention Data retention period in milliseconds.
   */
  public Bucket(long retention) {
    this.retention = retention;
    data = new LinkedList<>();
    lock = new Object();
  }

  private void trim() {
    var now = System.currentTimeMillis();

    synchronized (lock) {
      var iterator = data.iterator();

      while (iterator.hasNext()) {
        var piece = iterator.next();

        if (now - piece.getKey() > retention)
          iterator.remove();
      }
    }
  }

  public void pushData(double value) {
    var now = System.currentTimeMillis();

    synchronized (lock) {
      data.add(new SimpleEntry<>(now, value));
    }
  }

  public List<SimpleEntry<Long, Double>> getData() {
    trim();

    var result = new LinkedList<SimpleEntry<Long, Double>>();

    synchronized (lock) {
      var iterator = data.iterator();

      while (iterator.hasNext())
        result.add(iterator.next());
    }

    return result;
  }
}
