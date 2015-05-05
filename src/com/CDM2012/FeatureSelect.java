package com.CDM2012;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.model.SepModel;

import util.distance.ComputeMatrix;
import util.file.MyReadFile;
import util.file.MyWriteFile;
import util.math.MyArrayFunction;

public class FeatureSelect {

	public static void runCDM2012Algorithm(String filePath, int ql, int k) {
		MyReadFile fileDate = new MyReadFile(filePath);
		MyWriteFile.write2Log("CDM2012 Algorithm");
		MyWriteFile.write2Log(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date(System.currentTimeMillis())));
		MyWriteFile.write2Log("DataSet:" + filePath);
		MyWriteFile.write2Log("k:" + k);
		MyWriteFile.write2Log("ql:" + ql);
		List<Double[]> data = fileDate.openfile();
		double accuracy = 0;
		List<Integer> cls = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			cls.add(data.get(i)[0].intValue());
		}
		List<Integer> cls1 = new ArrayList<Integer>(cls);
		int ts = new Random().nextInt(data.size()); // the location to find the
													// first Shapelet
		MyWriteFile.write2Log("First Shapelet Index:" + ts);
		List<Double[]> data2 = new ArrayList<Double[]>(data);
		int FTR = 1;
		int iter = 1;
		List<Double[]> DIS = new ArrayList<Double[]>();
		List<Double> ACC = new ArrayList<Double>();
		long startTime = System.currentTimeMillis();
		long iterationTime = System.currentTimeMillis();
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
			// for (int j = ql; j <= 50; j++) {
			for (int i = 1; i < c - ql; i++) {
				for (int n = 0; n < ql; n++) {
					ftr[n] = data2.get(ts)[i + n];
				}
				Double[] dis = ComputeMatrix.computeMatrix(ftr, data2);
				SepModel sep = new SepModel(0.0, 0.0, 0.0, ql, i);
				seps.add(sep);
				for (double corr = 0.95; corr >= 0.65; corr = corr - 0.01) {
					Integer[] ind1 = MyArrayFunction.findSmallerThan(dis,
							Math.sqrt(2 * (1 - corr)));
					Integer[] ind2 = MyArrayFunction.findBiggerThan(dis,
							Math.sqrt(2 * (1 - corr)));
					if (ind1.length == 0 || ind2.length == 0)
						continue;
					double m1 = MyArrayFunction.meanOfArrayByIndex(dis, ind1);
					double m2 = MyArrayFunction.meanOfArrayByIndex(dis, ind2);
					double s1 = MyArrayFunction.stdOfArrayByIndex(dis, ind1);
					double s2 = MyArrayFunction.stdOfArrayByIndex(dis, ind2);
					double l1 = (double) ind1.length;
					double l2 = (double) ind2.length;
					double q = l1 / l2;
					double curr = 0;
					if (q > (1 / (double) k) && q < (1 - 1 / (double) k))
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
			// }
			Collections.sort(seps);
			int indx = 0;
			for (int i = 0; i < seps.size(); i++) {
				double q = seps.get(i).getQ();
				if (q > (1 / (double) k) && q < (1 - 1 / (double) k)) {
					indx = i;
					break;
				}
			}

			ql = seps.get(indx).getQl();

			for (int n = 0; n < ql; n++) {
				ftr[n] = data2.get(ts)[seps.get(indx).getIndex() + n];
			}
			Double[] dis = ComputeMatrix.computeMatrix(ftr, data2);
			double corr = seps.get(indx).getCorr();
			Integer[] ind1 = MyArrayFunction.findSmallerThan(dis,
					Math.sqrt(2 * (1 - corr)));
			Integer[] ind2 = MyArrayFunction.findBiggerThan(dis,
					Math.sqrt(2 * (1 - corr)));

			double m1 = MyArrayFunction.meanOfArrayByIndex(dis, ind1);
			double m2 = MyArrayFunction.meanOfArrayByIndex(dis, ind2);
			double s1 = MyArrayFunction.stdOfArrayByIndex(dis, ind1);
			double s2 = MyArrayFunction.stdOfArrayByIndex(dis, ind2);

			Double[] dist = ComputeMatrix.computeMatrix(ftr, data);
			DIS.add(dist);
			double tempACC = ClassifyFeats.classifyFeats(DIS, cls);
			ACC.add(tempACC);
			List<Double[]> temp = new ArrayList<Double[]>();
			temp.add(dis);
			accuracy = ClassifyFeats.classifyFeats(temp, cls1);
			MyWriteFile.write2Log("DIS accuracy:" + tempACC + " time:" + FTR);
			MyWriteFile.write2Log("Temp accuracy:" + accuracy + " time:" + FTR);
			MyWriteFile.write2FTRFile(ftr, iter);
			if (tempACC == 1) {
				break;
			}
			Integer[] I = MyArrayFunction.findSmallerThan(dis, m1 + (0.5 * s1));
			for (int i = 0; i < I.length; i++) {
				cls2[I[i]] = 1;
			}
			Integer[] X = MyArrayFunction.findEqual2Value(cls2, 0);
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
			MyWriteFile.write2Log("Iteration Time:"
					+ (System.currentTimeMillis() - iterationTime) + " iter:"
					+ iter);
			iterationTime = System.currentTimeMillis();
			iter = iter + 1;
		}
		long endTime = System.currentTimeMillis();
		MyWriteFile.write2Log("Running Time:" + (endTime - startTime));
		MyWriteFile.write2ACCFile(ACC);
		MyWriteFile.write2Log("\n");
	}
}
