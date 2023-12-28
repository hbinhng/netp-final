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
  private final BucketAggregator aggregator;
  private final long frameLength;
  private final long retention;
  private final List<BucketFrame> frames;

  /**
   * 
   * @param retention Data retention period in milliseconds.
   */
  public Bucket(long retention) {
    this(retention, 1000 /* 1 second */);
  }

  /**
   * 
   * @param retention   Data retention period in milliseconds.
   * @param frameLength Aggregated frame length in milliseconds.
   */
  public Bucket(long retention, long frameLength) {
    this(retention, frameLength, BucketAggregator.MEAN);
  }

  /**
   * 
   * @param retention   Data retention period in milliseconds.
   * @param frameLength Aggregated frame length in milliseconds.
   * @param aggregator  Aggregator function to aggregate each frame.
   */
  public Bucket(long retention, long frameLength, BucketAggregator aggregator) {
    lock = new Object();
    this.retention = retention;
    this.frameLength = frameLength;
    this.aggregator = aggregator;
    frames = new LinkedList<BucketFrame>();
  }

  private void trim() {
    var now = System.currentTimeMillis();

    synchronized (lock) {
      var iterator = frames.iterator();

      while (iterator.hasNext()) {
        var frame = iterator.next();

        if (now - frame.getSpawnPoint() > retention + frameLength)
          iterator.remove();
      }
    }
  }

  public void pushData(double value) {
    var now = System.currentTimeMillis();

    synchronized (lock) {
      BucketFrame currentFrame;

      if (frames.isEmpty())
        frames.add(currentFrame = new BucketFrame(frameLength, aggregator));
      else {
        var tail = frames.get(frames.size() - 1);

        if (tail.getSpawnPoint() + frameLength < now)
          frames.add(currentFrame = new BucketFrame(frameLength, aggregator));
        else
          currentFrame = tail;
      }

      currentFrame.pushData(value);
    }
  }

  public List<SimpleEntry<Long, Double>> getData() {
    trim();

    var result = new LinkedList<SimpleEntry<Long, Double>>();

    synchronized (lock) {
      for (var frame : frames) {
        var entry = new SimpleEntry<Long, Double>(
            frame.getSpawnPoint(),
            frame.getFrameValue());

        result.add(entry);
      }
    }

    return result;
  }
}
