package org.uet.int3304.gateway.UI.controllers;

import org.uet.int3304.gateway.Bucket.Bucket;
import org.uet.int3304.gateway.UI.BucketId;
import org.uet.int3304.gateway.UI.GatewayUIState;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class HeartbeatController extends ChartController {
  private static final long DATA_RETENTION = 30 * 1000; // 30 seconds

  private final Bucket heartbeatBucket;

  public HeartbeatController(LineChart<Number, Number> chart) {
    super();
    heartbeatBucket = new Bucket(DATA_RETENTION);

    var heartbeatSeries = new XYChart.Series<Number, Number>();

    var chartData = chart.getData();
    chartData.add(heartbeatSeries);

    heartbeatSeries.setName("Heartbeat");

    chart.setLegendVisible(true);

    linkSeriesWithBucket(heartbeatSeries, heartbeatBucket);

    GatewayUIState.getInstance().registerBucket(
        BucketId.HB_BUCKET, heartbeatBucket);
  }
}
