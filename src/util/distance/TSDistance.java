package util.distance;

import util.normalize.Normalize;

public class TSDistance {
	public static double normalizeDistanceOfEqualLength(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new RuntimeException("Not Equal Length Time Series!");
		}
		double[] normalizeA = Normalize.normalize(a);
		double[] normalizeB = Normalize.normalize(b);
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += Math.pow(normalizeA[i] - normalizeB[i], 2);
		}
		return Math.sqrt(sum);
	}
}
