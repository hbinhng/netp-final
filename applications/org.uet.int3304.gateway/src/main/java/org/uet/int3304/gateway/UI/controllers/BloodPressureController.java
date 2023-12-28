package org.uet.int3304.gateway.UI.controllers;

import org.uet.int3304.gateway.UI.BucketId;
import org.uet.int3304.gateway.UI.GatewayUIState;
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

    var state = GatewayUIState.getInstance();
    state.registerBucket(BucketId.SYSTOLIC_BUCKET, systolicBucket);
    state.registerBucket(BucketId.DIASTOLIC_BUCKET, diastolicBucket);
  }
}
