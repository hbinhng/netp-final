package org.uet.int3304.gateway.UI.controllers.graphs;

import java.net.URL;
import java.util.ResourceBundle;

import org.uet.int3304.gateway.Bucket.Bucket;
import org.uet.int3304.gateway.UI.BucketId;
import org.uet.int3304.gateway.UI.GatewayUIState;
import org.uet.int3304.gateway.UI.controllers.ChartController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

public class HeartBeatGraphController extends ChartController implements Initializable {
  private static final long DATA_RETENTION = 30 * 1000; // 30 seconds

  @FXML
  private LineChart<Number, Number> chart;

  private final Bucket heartbeatBucket;

  public HeartBeatGraphController() {
    super(Duration.seconds(1));
    heartbeatBucket = new Bucket(DATA_RETENTION);

    GatewayUIState.getInstance().registerBucket(
        BucketId.HB_BUCKET, heartbeatBucket);
  }

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    var heartbeatSeries = new XYChart.Series<Number, Number>();

    var chartData = chart.getData();
    chartData.add(heartbeatSeries);

    heartbeatSeries.setName("Heartbeat");

    chart.setLegendVisible(true);

    linkSeriesWithBucket(heartbeatSeries, heartbeatBucket);
  }
}