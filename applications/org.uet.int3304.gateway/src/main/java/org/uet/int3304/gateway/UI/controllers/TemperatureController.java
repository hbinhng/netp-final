package org.uet.int3304.gateway.UI.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class TemperatureController {

  private LineChart<Number, Number> TChart;
  private TemperatureBucket temperatureBucket;

  public TemperatureController(LineChart<Number, Number> TChart) {
    this.TChart = TChart;
    initialize();
  }

  public void initialize() {
    XYChart.Series<Number, Number> seriesT = new XYChart.Series<>();
    TChart.getData().add(seriesT);
    seriesT.setName("Temperature");
    TChart.setLegendVisible(true);
    temperatureBucket = new TemperatureBucket();
  }

  public void pushData(double value) {
    temperatureBucket.pushData(value);
  }

  public void updateChart(int currentTime) {
    TChart.getData().get(0).getData().add(new XYChart.Data<>(currentTime, temperatureBucket.getAverage()));
    temperatureBucket.flushData();
  }

  public class TemperatureBucket {
    private List<Double> temperatureBucket;

    public TemperatureBucket() {
      temperatureBucket = new ArrayList<>();
    }

    public void pushData(double value) {
      temperatureBucket.add(value);
    }

    public double getAverage() {
      double sum = 0;
      for (double value : temperatureBucket) {
        sum += value;
      }
      return sum / temperatureBucket.size();
    }

    public void flushData() {
      temperatureBucket.clear();
    }
  }
}
