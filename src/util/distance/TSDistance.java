package util.distance;

import util.math.StatisticsBase;
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
		return Math.sqrt(sum / a.length);
	}

	public static double normalizeDistanceOfEqualLength2(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new RuntimeException("Not Equal Length Time Series!");
		}
		double[] normalizeA = Normalize.normalize(a);
		double[] normalizeB = Normalize.normalize(b);
		double sumOfMultiplication = 0;
		int n = normalizeA.length;
		for (int i = 0; i < n; i++) {
			sumOfMultiplication += normalizeA[i] * normalizeB[i];
		}
		double c = (sumOfMultiplication - (double) n
				* StatisticsBase.getAverage(normalizeA)
				* StatisticsBase.getAverage(normalizeB))
				/ ((double) n * StatisticsBase.getVariance(normalizeA) * StatisticsBase
						.getVariance(normalizeB));

		return Math.sqrt(2 * (1 - c));
	}
}
