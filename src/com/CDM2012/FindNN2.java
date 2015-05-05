package com.CDM2012;

import util.distance.TSDistance;
import util.math.StatisticsBase;
import util.normalize.Normalize;

public class FindNN2 {
	public static double[] findNN(double[] x, double[] y) {
		int length = x.length - y.length + 1;
		double[] dist = new double[length];
		for (int i = 0; i < length; i++) {
			double[] temp = new double[y.length];
			for (int j = 0; j < y.length; j++) {
				temp[j] = x[i + j];
			}
			dist[i] = TSDistance.normalizeDistanceOfEqualLength2(y, temp);
		}
		return getMinWithIndex(dist);
	}

	public static double findNN2(double[] x, double[] y) {
		int length = x.length - y.length + 1;
		double[] dist = new double[length];
		for (int i = 0; i < length; i++) {
			double[] temp = new double[y.length];
			for (int j = 0; j < y.length; j++) {
				temp[j] = x[i + j];
			}
			dist[i] = TSDistance.normalizeDistanceOfEqualLength(y, temp);
		}
		return getMinWithIndex(dist)[1];
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

}
