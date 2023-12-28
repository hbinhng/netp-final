package org.uet.int3304.gateway.UI.controllers;

import java.util.LinkedList;

import org.uet.int3304.gateway.bucket.Bucket;

import javafx.collections.FXCollections;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class BloodPressureController {
  private static final long DATA_RETENTION = 30 * 1000; // 30 seconds;

  private final XYChart.Series<Number, Number> systolicSeries;
  private final XYChart.Series<Number, Number> diastolicSeries;

  private final Bucket systolicBucket;
  private final Bucket diastolicBucket;

  public BloodPressureController(LineChart<Number, Number> chart) {
    systolicBucket = new Bucket(DATA_RETENTION);
    diastolicBucket = new Bucket(DATA_RETENTION);

    systolicSeries = new XYChart.Series<>();
    diastolicSeries = new XYChart.Series<>();

    var chartData = chart.getData();
    chartData.add(systolicSeries);
    chartData.add(diastolicSeries);

    systolicSeries.setName("Systolic");
    diastolicSeries.setName("Diastolic");

    chart.setLegendVisible(true);
  }

  private void syncBucket(XYChart.Series<Number, Number> series, Bucket bucket) {
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

  public void pushData(double systolicValue, double diastolicValue) {
    systolicBucket.pushData(systolicValue);
    diastolicBucket.pushData(diastolicValue);
  }

  public void updateChart() {
    syncBucket(systolicSeries, systolicBucket);
    syncBucket(diastolicSeries, diastolicBucket);
  }
}
