package com.CDM2012;

import java.util.List;

public class ComputeMatrix {
	public static Double[] computeMatrix(double[] ftr, List<Double[]> data) {
		int n = data.size();
		Double[] dis = new Double[n];
		double[] loc = new double[n];
		for (int i = 0; i < n; i++) {
			dis[i] = 0.0;
		}
		int lengthX = data.get(0).length - 1;
		double[] x = new double[lengthX];
		double[] y = new double[ftr.length];

		for (int j = 0; j < n; j++) {
			for (int it = 0; it < lengthX; it++) {
				x[it] = data.get(j)[it + 1];
			}
			for (int it2 = 0; it2 < ftr.length; it2++) {
				y[it2] = ftr[it2];
			}
			double[] result = FindNN2.findNN(x, y);
			loc[j] = result[0];
			dis[j] = result[1];
		}
		return dis;
	}
}
