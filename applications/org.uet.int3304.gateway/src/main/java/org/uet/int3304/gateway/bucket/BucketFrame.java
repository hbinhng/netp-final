package org.uet.int3304.gateway.bucket;

import java.util.LinkedList;
import java.util.List;

/**
 * This frame holds data of a bucket in a frameLength
 * time frame.
 */
public class BucketFrame {
  private final long spawnPoint;
  private final BucketAggregator aggregator;

  private final List<Double> buffer;

  /**
   * 
   * @param frameLength Aggregated frame length in milliseconds.
   */
  public BucketFrame(long frameLength) {
    this(frameLength, BucketAggregator.MEAN);
  }

  /**
   * 
   * @param frameLength Aggregated frame length in milliseconds.
   * @param aggregator  Aggregator function to aggregate each frame.
   */
  public BucketFrame(long frameLength, BucketAggregator aggregator) {
    var now = System.currentTimeMillis();
    spawnPoint = now - (now % frameLength);
    this.aggregator = aggregator;
    buffer = new LinkedList<Double>();
  }

  private double aggregateUsingMin() {
    var result = Double.MAX_VALUE;

    for (var value : buffer)
      if (value < result)
        result = value;

    return result;
  }

  private double aggregateUsingMean() {
    var result = 0d;

    for (var value : buffer)
      result += value;

    return result / buffer.size();
  }

  private double aggregateUsingMax() {
    var result = Double.MIN_VALUE;

    for (var value : buffer)
      if (value > result)
        result = value;

    return result;
  }

  public void pushData(double value) {
    buffer.add(value);
  }

  public double getFrameValue() {
    switch (aggregator) {
      case MIN:
        return aggregateUsingMin();
      case MEAN:
        return aggregateUsingMean();
      case MAX:
        return aggregateUsingMax();
      default:
        return 0d;
    }
  }

  public long getSpawnPoint() {
    return spawnPoint;
  }
}
