package org.uet.int3304.gateway.UI.controllers;

import org.uet.int3304.gateway.bucket.Bucket;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class BloodPressureController extends ChartController {
  private static final long DATA_RETENTION = 30 * 1000; // 30 seconds;

  private final Bucket systolicBucket;
  private final Bucket diastolicBucket;

  public BloodPressureController(LineChart<Number, Number> chart) {
    super();
    systolicBucket = new Bucket(DATA_RETENTION);
    diastolicBucket = new Bucket(DATA_RETENTION);

    var systolicSeries = new XYChart.Series<Number, Number>();
    var diastolicSeries = new XYChart.Series<Number, Number>();

    var chartData = chart.getData();
    chartData.add(systolicSeries);
    chartData.add(diastolicSeries);

    systolicSeries.setName("Systolic");
    diastolicSeries.setName("Diastolic");

    chart.setLegendVisible(true);

    linkSeriesWithBucket(systolicSeries, systolicBucket);
    linkSeriesWithBucket(diastolicSeries, diastolicBucket);
  }

  public void pushData(double systolicValue, double diastolicValue) {
    systolicBucket.pushData(systolicValue);
    diastolicBucket.pushData(diastolicValue);
  }
}
