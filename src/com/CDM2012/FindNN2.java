package com.CDM2012;

import util.math.StatisticsBase;

public class FindNN2 {
	public static double[] findNN(double[] x, double[] y) {
		int length = x.length - y.length + 1;
		double[] dist = new double[length];
		for (int i = 0; i < length; i++) {
			double[] temp = new double[y.length];
			for (int j = 0; j < y.length; j++) {
				temp[j] = x[i + j];
			}
			dist[i] = normalizeDistanceOfEqualLength(y, temp);
		}
		return getMinWithIndex(dist);
	}

	public static double[] getMinWithIndex(double[] d) {
		double[] result = new double[2];
		result[1] = Double.MAX_VALUE;
		result[0] = 0;
		for (int i = 0; i < d.length; i++) {
			if (result[1] > d[i]) {
				result[1] = d[i];
				result[0] = i;
			}
		}
		return result;
	}

	public static double normalizeDistanceOfEqualLength(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new RuntimeException("Not Equal Length Time Series!");
		}
		double[] normalizeA = normalize(a);
		double[] normalizeB = normalize(b);
		// double[] normalizeA = a;
		// double[] normalizeB = b;
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += Math.pow(normalizeA[i] - normalizeB[i], 2);
		}
		return Math.sqrt(sum);
	}

	private static double[] normalize(double[] data) {
		double mean = StatisticsBase.getAverage(data);
		double std = StatisticsBase.getStandardDiviation(data);
		double[] data2 = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			data2[i] = (data[i] - mean) / std;
		}
		return data2;
	}
}
