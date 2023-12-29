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

public class BloodPressureGraphController extends ChartController implements Initializable {
  private static final long DATA_RETENTION = 30 * 1000; // 30 seconds;

  @FXML
  private LineChart<Number, Number> chart;

  private final Bucket systolicBucket;
  private final Bucket diastolicBucket;

  public BloodPressureGraphController() {
    super();
    systolicBucket = new Bucket(DATA_RETENTION);
    diastolicBucket = new Bucket(DATA_RETENTION);

    var state = GatewayUIState.getInstance();
    state.registerBucket(BucketId.SYSTOLIC_BUCKET, systolicBucket);
    state.registerBucket(BucketId.DIASTOLIC_BUCKET, diastolicBucket);
  }

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
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
}