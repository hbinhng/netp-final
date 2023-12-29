package org.uet.int3304.gateway.UI.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.uet.int3304.gateway.Bucket.Bucket;
import org.uet.int3304.gateway.UI.TimelineManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

public abstract class ChartController {
  private final List<SimpleEntry<XYChart.Series<Number, Number>, Bucket>> associations;

  protected ChartController(Duration refreshInterval) {
    associations = new LinkedList<>();

    var timeline = TimelineManager.getInstance()
        .registerTimeline(
            new KeyFrame(refreshInterval, (event) -> {
              updateChart();
            }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  protected void linkSeriesWithBucket(XYChart.Series<Number, Number> series, Bucket bucket) {
    associations.add(new SimpleEntry<>(series, bucket));
  }

  protected void syncBucket(XYChart.Series<Number, Number> series, Bucket bucket) {
    var bucketData = bucket.getData();
    var seriesData = new LinkedList<XYChart.Data<Number, Number>>();

    var now = System.currentTimeMillis() / 1000;

    for (var data : bucketData) {
      var x = data.getKey() / 1000 - now;
      var y = data.getValue();

      seriesData.add(
          new XYChart.Data<Number, Number>(x, y));
    }

    series.setData(FXCollections.observableList(seriesData));
  }

  public void updateChart() {
    for (var association : associations)
      syncBucket(association.getKey(), association.getValue());
  }
}
