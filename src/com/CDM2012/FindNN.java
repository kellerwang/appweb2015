package com.CDM2012;

import java.util.ArrayList;

import util.math.StatisticsBase;

public class FindNN {

	private static double[] normalize(double[] data, double mean, double std) {
		for (int i = 0; i < data.length; i++) {
			data[i] = (data[i] - mean) / std;
		}
		return data;
	}

	private static double[] reverseTheQuery(double[] data) {
		int size = data.length;
		for (int i = 0; i < size / 2; i++) {
			double temp = data[i];
			data[i] = data[size - 1 - i];
			data[size - 1 - i] = temp;
		}
		return data;
	}

	public static double[] findNN(double[] x, double[] y) {

		int n = x.length;
		double meanY = StatisticsBase.getAverage(y);
		double stdY = StatisticsBase.getStandardDiviation(y);
		int m = y.length;
		y = normalize(y, meanY, stdY);
		y = reverseTheQuery(y);

		double[] nx = new double[2 * n];
		double[] ny = new double[2 * n];
		for (int i = 0; i < n; i++) {
			nx[i] = x[i];
		}
		for (int i = 0; i < m; i++) {
			ny[i] = y[i];
		}
		int size = FFT.greater2p2(2 * n);
		Complex[] fx = new Complex[size];
		Complex[] fy = new Complex[size];
		for (int i = 0; i < size; i++) {
			if (i < n) {
				Complex nfx = new Complex(nx[i], 0);
				Complex nfy = new Complex(ny[i], 0);
				fx[i] = nfx;
				fy[i] = nfy;
			} else {
				Complex zero = new Complex(0, 0);
				fx[i] = zero;
				fy[i] = zero;
			}
		}
		Complex[] X = FFT2.fft(fx);
		Complex[] Y = FFT2.fft(fy);
		Complex[] Z = new Complex[size];
		for (int i = 0; i < size; i++) {
			Z[i] = X[i].times(Y[i]);
		}
		double[] z = new double[size];
		Complex[] zz = FFT2.ifft(Z);
		for (int i = 0; i < size; i++) {
			z[i] = zz[i].re();
		}

		double sumy = StatisticsBase.getSum(y);
		double sumy2 = StatisticsBase.getSquareSum(y);

		double[] cum_sumx = new double[2 * n];
		double[] cum_sumx2 = new double[2 * n];
		double[] sumx = new double[n - m];
		double[] sumx2 = new double[n - m];
		double[] meanx = new double[n - m];
		double[] sigmax2 = new double[n - m];
		double[] sigmax = new double[n - m];
		cum_sumx[0] = x[0];
		cum_sumx2[0] = Math.pow(x[0], 2);
		for (int i = 1; i < 2 * n; i++) {
			cum_sumx[i] = cum_sumx[i - 1] + nx[i];
			cum_sumx2[i] = cum_sumx2[i - 1] + Math.pow(nx[i], 2);
		}
		for (int i = 0; i < n - m; i++) {
			sumx[i] = cum_sumx[m + i] - cum_sumx[i];
			sumx2[i] = cum_sumx2[m + i] - cum_sumx2[i];
		}
		for (int i = 0; i < n - m; i++) {
			meanx[i] = sumx[i] / m;
		}
		for (int i = 0; i < n - m; i++) {
			sigmax2[i] = (sumx2[i] / m) - (Math.pow(meanx[i], 2));
			sigmax[i] = Math.sqrt(sigmax2[i]);
		}

		double[] dist = new double[n - m];
		for (int i = 0; i < n - m; i++) {
			dist[i] = ((sumx2[i] - (2 * sumx[i] * meanx[i]) + (m * Math.pow(
					meanx[i], 2))) / sigmax2[i])
					- (2 * (z[m + i] - sumy * meanx[i]) / sigmax[i])
					+ sumy2;
		}
		for (int i = 0; i < n - m; i++) {
			dist[i] = Math.sqrt(dist[i]);
		}

		double[] result = getMinWithIndex(dist);
		return result;
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
