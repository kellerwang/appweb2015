package com.CDM2012;

import java.util.List;

public class ClassifyFeats {
	public static double getED(double[] p, double[] q) {
		double sum = 0;
		for (int i = 0; i < p.length; i++) {
			sum = sum + Math.pow(p[i] - q[i], 2);
		}
		sum = Math.sqrt(sum);
		return sum;
	}

	public static double classifyFeats(List<Double[]> feats,
			List<Integer> labels) {
		int n = feats.get(0).length;
		int correct = 0;
		for (int i = 0; i < n; i++) {
			double[] x = new double[feats.size()];
			double[] y = new double[feats.size()];
			double bsf = Double.MAX_VALUE;
			int minI = -1;
			for (int j = 0; j < n; j++) {
				if (i == j)
					continue;
				for (int k = 0; k < feats.size(); k++) {
					x[k] = feats.get(k)[i];
					y[k] = feats.get(k)[j];
				}
				double d = getED(x, y);
				if (d < bsf) {
					bsf = d;
					minI = j;
				}
			}
			if (minI == -1) {
				double mn = 0.02;
			}
			if (labels.get(minI) == labels.get(i)) {
				correct = correct + 1;
			}
		}
		double accuary = (double) correct / (double) n;
		return accuary;
	}
}
