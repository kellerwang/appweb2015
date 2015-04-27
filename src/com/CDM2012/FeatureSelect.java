package com.CDM2012;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.model.SepModel;

import util.file.MyReadFile;

public class FeatureSelect {

	public static Integer[] findSmallerThan(Double[] data, double value) {
		List<Integer> index = new ArrayList<Integer>();
		for (int i = 0; i < data.length; i++) {
			if (data[i] < value) {
				index.add(i);
			}
		}
		Integer[] result = new Integer[index.size()];
		return index.toArray(result);
	}

	public static Integer[] findBiggerThan(Double[] data, double value) {
		List<Integer> index = new ArrayList<Integer>();
		for (int i = 0; i < data.length; i++) {
			if (data[i] > value) {
				index.add(i);
			}
		}
		Integer[] result = new Integer[index.size()];
		return index.toArray(result);
	}

	public static double meanOfArrayByIndex(Double[] data, Integer[] index) {
		double sum = 0;
		for (int i = 0; i < index.length; i++) {
			sum = sum + data[index[i]];
		}
		sum = sum / index.length;
		return sum;
	}

	public static double stdOfArrayByIndex(Double[] data, Integer[] index) {
		double sum = 0;
		double avg = meanOfArrayByIndex(data, index);
		for (int i = 0; i < index.length; i++) {
			sum = sum + Math.pow((data[index[i]] - avg), 2);
		}
		sum = sum / data.length;
		sum = Math.sqrt(sum);
		return sum;
	}

	public static Integer[] findEqual2Value(int[] data, int value) {
		List<Integer> index = new ArrayList<Integer>();
		for (int i = 0; i < data.length; i++) {
			if (data[i] == value) {
				index.add(i);
			}
		}
		Integer[] result = new Integer[index.size()];
		return index.toArray(result);
	}

	public static void main(String[] args) {
		String filePath = "data" + File.separator + "Trace.txt";
//		String outputFile = "output" + File.separator + "result.txt";
		MyReadFile fileDate = new MyReadFile(filePath);
		List<Double[]> data = fileDate.openfile();
		double accuracy = 0;
		List<Integer> cls = new ArrayList<Integer>();
		List<Integer> cls1 = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			cls.add(data.get(i)[0].intValue());
			cls1.add(data.get(i)[0].intValue());
		}
		int ts = 0; // the location to find the first Shapelet
		List<Double[]> data2 = fileDate.openfile();
		int ql = 50;
		int FTR = 1;
		int iter = 1;
		List<Double[]> DIS = new ArrayList<Double[]>();
		List<Double> ACC = new ArrayList<Double>();

		while (true) {
			int r = data2.size();
			int c = data2.get(0).length;
			int[] cls2 = new int[r];
			for (int i = 0; i < r; i++) {
				cls2[i] = 0;
			}
			List<SepModel> seps = new ArrayList<SepModel>();
			double[] ftr = new double[ql];
			int cnt = 0;
			for (int j = ql; j <= 50; j++) {
				for (int i = 1; i < c - ql; i++) {
					for (int n = 0; n < ql; n++) {
						ftr[n] = data2.get(ts)[i + n];
					}
					Double[] dis = ComputeMatrix.computeMatrix(ftr, data2);
					for (int n = 0; n < dis.length; n++) {
						dis[n] = dis[n] / Math.sqrt(ql);
					}

					SepModel sep = new SepModel(0.0, 0.0, 0.0, ql, i);
					seps.add(sep);
					for (double corr = 0.95; corr >= 0.65; corr = corr - 0.01) {
						Integer[] ind1 = findSmallerThan(dis,
								Math.sqrt(2 * (1 - corr)));
						Integer[] ind2 = findBiggerThan(dis,
								Math.sqrt(2 * (1 - corr)));
						if (ind1.length == 0 || ind2.length == 0)
							continue;
						double m1 = meanOfArrayByIndex(dis, ind1);
						double m2 = meanOfArrayByIndex(dis, ind2);
						double s1 = stdOfArrayByIndex(dis, ind1);
						double s2 = stdOfArrayByIndex(dis, ind2);

						double l1 = (double) ind1.length;
						double l2 = (double) ind2.length;
						double q = l1 / l2;
						double curr = 0;

						if (q > 0.2 && q < 5)
							curr = (m2 - s2 - (m1 + s1));
						else {
							curr = 0;
						}
						if (curr > seps.get(cnt).getGap()) {
							seps.get(cnt).setGap(curr);
							seps.get(cnt).setCorr(corr);
							seps.get(cnt).setQ(q);
						}
					}
					cnt++;
				}
			}
			Collections.sort(seps);
			int indx = 0;
			for (int i = 0; i < seps.size(); i++) {
				double q = seps.get(i).getQ();
				if (q > 0.2 && q < 5) {
					indx = i;
					break;
				}
			}

			ql = seps.get(indx).getQl();

			for (int n = 0; n < ql; n++) {
				ftr[n] = data2.get(ts)[seps.get(indx).getIndex() + n];
			}
			Double[] dis = ComputeMatrix.computeMatrix(ftr, data2);
			for (int i = 0; i < dis.length; i++) {
				dis[i] = dis[i] / Math.sqrt(ql);
			}
			double corr = seps.get(indx).getCorr();
			Integer[] ind1 = findSmallerThan(dis, Math.sqrt(2 * (1 - corr)));
			Integer[] ind2 = findBiggerThan(dis, Math.sqrt(2 * (1 - corr)));

			double m1 = meanOfArrayByIndex(dis, ind1);
			double m2 = meanOfArrayByIndex(dis, ind2);
			double s1 = stdOfArrayByIndex(dis, ind1);
			double s2 = stdOfArrayByIndex(dis, ind2);

			Double[] dist = ComputeMatrix.computeMatrix(ftr, data);
			for (int i = 0; i < dist.length; i++) {
				dist[i] = dist[i] / Math.sqrt(ql);
			}
			DIS.add(dist);
			ACC.add(ClassifyFeats.classifyFeats(DIS, cls));
			List<Double[]> temp = new ArrayList<Double[]>();
			temp.add(dis);
			accuracy = ClassifyFeats.classifyFeats(temp, cls1);
			System.out.println("accuracy:" + accuracy + " time:" + FTR);

			Integer[] I = findSmallerThan(dis, m1 + (0.5 * s1));
			for (int i = 0; i < I.length; i++) {
				cls2[I[i]] = 1;
			}
			Integer[] X = findEqual2Value(cls2, 0);
			List<Integer> listTempInteger = new ArrayList<Integer>();
			List<Double[]> listTempDoubleArray = new ArrayList<Double[]>();
			for (int i = 0; i < X.length; i++) {
				listTempInteger.add(cls1.get(X[i]));
				listTempDoubleArray.add(data2.get(X[i]));
			}
			cls1 = listTempInteger;
			data2 = listTempDoubleArray;
			FTR = FTR + 1;

			if (ind1.length < 2 || ind2.length < 2) {
				break;
			}

			int[] arrayIndex = new int[dis.length];
			for (int i = 0; i < arrayIndex.length; i++) {
				arrayIndex[i] = i;
			}
			for (int i = 0; i < dis.length - 1; i++) {
				for (int j = i + 1; j < dis.length; j++) {
					if (dis[i] < dis[j]) {
						double tempDouble = dis[i];
						int p = arrayIndex[i];
						dis[i] = dis[j];
						arrayIndex[i] = arrayIndex[j];
						dis[j] = tempDouble;
						arrayIndex[j] = p;
					}
				}
			}
			for (int i = 0; i < r; i++) {
				if (cls2[arrayIndex[i]] == 0) {
					ts = Arrays.asList(X).indexOf(arrayIndex[i]);
					break;
				}
			}

			iter = iter + 1;

		}
	}
}
