package org.uet.int3304.gateway.UI.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

  @FXML
  private LineChart<Number, Number> TChart;

  @FXML
  private LineChart<Number, Number> HBChart;

  @FXML
  private LineChart<Number, Number> BPChart;

  private TemperatureController temperatureController;
  private HeartbeatController heartbeatController;
  private BloodPressureController bloodPressureController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    temperatureController = new TemperatureController(TChart);
    heartbeatController = new HeartbeatController(HBChart);
    bloodPressureController = new BloodPressureController(BPChart);

    // Set up the timeline to update the charts periodically
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      updateBPChart();
      updateHBChart();
      updateTChart();
    }));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  public void pushTData(double value) {
    temperatureController.pushData(value);
  }

  public void pushHBData(double value) {
    heartbeatController.pushData(value);
  }

  public void pushBPData(double systolicValue, double diastolicValue) {
    bloodPressureController.pushData(systolicValue, diastolicValue);
  }

  public void updateTChart() {
    temperatureController.updateChart();
  }

  public void updateHBChart() {
    heartbeatController.updateChart();
  }

  public void updateBPChart() {
    bloodPressureController.updateChart();
  }
}
