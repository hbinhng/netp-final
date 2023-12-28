package org.uet.int3304.gateway.UI.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class BloodPressureController {

    private LineChart<Number, Number> BPChart;
	private BloodPressureBucket bloodPressureBucket;

	public BloodPressureController(LineChart<Number, Number> BPChart) {
		this.BPChart = BPChart;
		initialize();
	}

    public void initialize() {
        XYChart.Series<Number, Number> seriesSBP = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesDBP = new XYChart.Series<>();
        BPChart.getData().addAll(seriesSBP, seriesDBP);
        seriesSBP.setName("Systolic");
        seriesDBP.setName("Diastolic");
        BPChart.setLegendVisible(true);
		bloodPressureBucket = new BloodPressureBucket();
    }

	public void pushData(double systolicValue, double diastolicValue) {
		bloodPressureBucket.pushData(systolicValue, diastolicValue);
	}

    public void updateChart(int currentTime) {
        BPChart.getData().get(0).getData().add(new XYChart.Data<>(currentTime, bloodPressureBucket.getSystolicAverage()));
        BPChart.getData().get(1).getData().add(new XYChart.Data<>(currentTime, bloodPressureBucket.getDiastolicAverage()));
		bloodPressureBucket.flushData();
    }

	public class BloodPressureBucket {
		List<Double> systolicBucket;
		List<Double> diastolicBucket;

		public BloodPressureBucket() {
			systolicBucket = new ArrayList<>();
			diastolicBucket = new ArrayList<>();
		}

		public void pushData(double systolicValue, double diastolicValue) {
			systolicBucket.add(systolicValue);
			diastolicBucket.add(diastolicValue);
		}

		public double getSystolicAverage() {
			double sum = 0;
			for (double value : systolicBucket) {
				sum += value;
			}
			return sum / systolicBucket.size();
		}

		public double getDiastolicAverage() {
			double sum = 0;
			for (double value : diastolicBucket) {
				sum += value;
			}
			return sum / diastolicBucket.size();
		}

		public void flushData() {
			systolicBucket.clear();
			diastolicBucket.clear();
		}
	}
}
