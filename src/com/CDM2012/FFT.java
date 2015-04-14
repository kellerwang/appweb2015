package com.CDM2012;

public class FFT {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void fft(double pr[], double pi[], double fr[], double fi[]) {
		int N = pr.length;
		for (int i = 0; i < N; i++) {
			fr[i] = 0;
			fi[i] = 0;
			for (int j = 0; j < N; j++) {
				fr[i] += pr[j] * Math.cos(2 * Math.PI * i * j / N) + pi[j]
						* Math.sin(2 * Math.PI * i * j / N);
				fi[i] += (-pr[j] * Math.sin(2 * Math.PI * i * j / N)) + pi[j]
						* Math.cos(2 * Math.PI * i * j / N);
			}
		}
	}

	public static void ifft(double fr[], double fi[], double pr[], double pi[]) {
		int N = pr.length;
		for (int i = 0; i < N; i++) {
			pr[i] = 0;
			pi[i] = 0;
			for (int j = 0; j < N; j++) {
				pr[i] += fr[j] * Math.cos(2 * Math.PI * i * j / N) / N - fi[j]
						* Math.sin(2 * Math.PI * i * j / N) / N;
				pi[i] += fr[j] * Math.sin(2 * Math.PI * i * j / N) / N + fi[j]
						* Math.cos(2 * Math.PI * i * j / N) / N;
			}
		}

	}

	public static int greater2p2(int n) {
		for (int i = 1;; i++) {
			if (n < Math.pow(2, i)) {
				return (int) Math.pow(2, i);
			}
		}
	}
}
