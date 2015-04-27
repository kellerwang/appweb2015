package util.normalize;

import util.math.StatisticsBase;

public class Normalize {
	public static double[] normalize(double[] data) {
		double mean = StatisticsBase.getAverage(data);
		double std = StatisticsBase.getStandardDiviation(data);
		double[] data2 = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			data2[i] = (data[i] - mean) / std;
		}
		return data2;
	}
}
