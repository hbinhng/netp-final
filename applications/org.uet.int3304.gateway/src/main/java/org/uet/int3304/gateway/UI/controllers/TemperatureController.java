package org.uet.int3304.gateway.UI.controllers;

import org.uet.int3304.gateway.Bucket.Bucket;
import org.uet.int3304.gateway.UI.BucketId;
import org.uet.int3304.gateway.UI.GatewayUIState;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class TemperatureController extends ChartController {
  private static final long DATA_RETENTION = 30 * 1000; // 30 seconds;

  private final Bucket temperatureBucket;

  public TemperatureController(LineChart<Number, Number> chart) {
    super();
    temperatureBucket = new Bucket(DATA_RETENTION);

    var temperatureSeries = new XYChart.Series<Number, Number>();

    var chartData = chart.getData();
    chartData.add(temperatureSeries);

    temperatureSeries.setName("Temperature");

    chart.setLegendVisible(true);

    linkSeriesWithBucket(temperatureSeries, temperatureBucket);

    GatewayUIState.getInstance().registerBucket(
        BucketId.TEMP_BUCKET, temperatureBucket);
  }
}
