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

public class TemperatureGraphController extends ChartController implements Initializable {
  private static final long DATA_RETENTION = 30 * 1000; // 30 seconds;

  @FXML
  private LineChart<Number, Number> chart;

  private final Bucket temperatureBucket;

  public TemperatureGraphController() {
    super(Duration.seconds(1));
    temperatureBucket = new Bucket(DATA_RETENTION);

    GatewayUIState.getInstance().registerBucket(
        BucketId.TEMP_BUCKET, temperatureBucket);
  }

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    var temperatureSeries = new XYChart.Series<Number, Number>();

    temperatureSeries.setName("Temperature");

    linkSeriesWithBucket(temperatureSeries, temperatureBucket);

    var chartData = chart.getData();
    chartData.add(temperatureSeries);

    chart.setLegendVisible(true);

  }
}
