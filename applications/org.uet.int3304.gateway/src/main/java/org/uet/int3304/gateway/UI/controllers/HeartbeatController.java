package org.uet.int3304.gateway.UI.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class HeartbeatController {

    private LineChart<Number, Number> HBChart;
	private HeartbeatBucket heartbeatBucket;

	public HeartbeatController(LineChart<Number, Number> HBChart) {
		this.HBChart = HBChart;
		initialize();
	}

    public void initialize() {
        XYChart.Series<Number, Number> seriesHB = new XYChart.Series<>();
        HBChart.getData().add(seriesHB);
        seriesHB.setName("Heartbeat");
        HBChart.setLegendVisible(true);
		heartbeatBucket = new HeartbeatBucket();
    }

	public void pushData(double value) {
		heartbeatBucket.pushData(value);
	}

    public void updateChart(int currentTime) {
        HBChart.getData().get(0).getData().add(new XYChart.Data<>(currentTime, heartbeatBucket.getAverage()));
		heartbeatBucket.flushData();
    }

	public class HeartbeatBucket {
		private List<Double> heartbeatBucket;

        public HeartbeatBucket() {
            heartbeatBucket = new ArrayList<>();
        }

		public void pushData(double value) {
            heartbeatBucket.add(value);
        }

        public double getAverage() {
            double sum = 0;
            for (double value : heartbeatBucket) {
                sum += value;
            }
            return sum / heartbeatBucket.size();
        }

        public void flushData() {
            heartbeatBucket.clear();
        }
	}
}
