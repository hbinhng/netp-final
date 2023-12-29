package org.uet.int3304.gateway.UI;

import java.util.LinkedList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class TimelineManager {
  private static final Object lock = new Object();
  private static TimelineManager instance;

  private final List<Timeline> timelines;

  private TimelineManager() {
    timelines = new LinkedList<>();
  }

  public Timeline registerTimeline(KeyFrame keyFrame) {
    var timeline = new Timeline(keyFrame);

    timelines.add(timeline);

    return timeline;
  }

  public void stop() {
    for (var timeline : timelines)
      timeline.stop();
  }

  public static TimelineManager getInstance() {
    if (instance == null)
      synchronized (lock) {
        if (instance == null)
          instance = new TimelineManager();
      }

    return instance;
  }
}
