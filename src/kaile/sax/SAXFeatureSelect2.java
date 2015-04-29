package kaile.sax;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.CDM2012.ClassifyFeats;
import com.CDM2012.FindNN;
import com.CDM2012.FindNN2;
import com.model.SepModel;
import com.model.ShapeletIndexModel;

import util.distance.TSDistance;
import util.file.MyReadFile;
import util.file.MyReadParameter;
import util.math.MyArrayFunction;
import util.normalize.Symbolization;

public class SAXFeatureSelect2 {
	private static final String filePathParameter = "data" + File.separator
			+ "Gaussian";
	private static final String filePath = "data" + File.separator
			+ "ChlorineConcentration_TRAIN.txt";

	private static final int w = 6;
	private static final int a = 3;

	public static Map<String, List<ShapeletIndexModel>> buildAugmentedTreeMap(
			List<Double[]> data, Map<Integer, List<Double>> mapParameter,
			int ql, int w, int a) {
		Map<String, List<ShapeletIndexModel>> augmentedTreeMap = new HashMap<String, List<ShapeletIndexModel>>();
		for (int i = 0; i < data.size(); i++) {
			// the first column of data represents the classes.
			for (int j = 0; j < data.get(i).length - ql; j++) {
				double[] ftr = new double[ql];
				double[] ppa = new double[w];
				String sax = null;
				for (int k = 0; k < ql; k++) {
					ftr[k] = data.get(i)[1 + k + j];
				}
				ppa = Symbolization.getPPA(ftr, w);
				sax = Symbolization.getSAX(ppa, a, mapParameter);
				if (augmentedTreeMap.containsKey(sax)) {
					augmentedTreeMap.get(sax).add(
							new ShapeletIndexModel(i, j, ftr));
				} else {
					List<ShapeletIndexModel> tempShapeletIndexModels = new ArrayList<ShapeletIndexModel>();
					tempShapeletIndexModels.add(new ShapeletIndexModel(i, j,
							ftr));
					augmentedTreeMap.put(sax, tempShapeletIndexModels);
				}
			}
		}
		return augmentedTreeMap;
	}

	public static Double[] saxComputeMatrix(List<Double[]> data, double[] ftr,
			Map<Integer, List<Double>> mapParameter, List<Integer> indexX,
			Map<String, List<ShapeletIndexModel>> augmentedTreeMap) {
		Double[] dis = new Double[indexX.size()];
		// signify the ftr
		double[] ppa = Symbolization.getPPA(ftr, w);
		String sax = Symbolization.getSAX(ppa, a, mapParameter);
		if (augmentedTreeMap.containsKey(sax)) {
//			long startTimeTreeCompute = System.currentTimeMillis();
			List<ShapeletIndexModel> simList = augmentedTreeMap.get(sax);
			for (int j = 0; j < simList.size(); j++) {
				if (indexX.contains(simList.get(j).getRow())) {
					double tempDistance = TSDistance
							.normalizeDistanceOfEqualLength(ftr, simList.get(j)
									.getFtr());
					if (dis[indexX.indexOf(simList.get(j).getRow())] == null
							|| dis[indexX.indexOf(simList.get(j).getRow())] > tempDistance) {
						dis[indexX.indexOf(simList.get(j).getRow())] = tempDistance;
					}
				}
			}
//			long endTimeTreeCompute = System.currentTimeMillis();
//			System.out.println("Time of Tree Compute:"
//					+ (endTimeTreeCompute - startTimeTreeCompute));
//			long startTimeMissingValue = System.currentTimeMillis();
			// fill the missing value
			for (int j = 0; j < dis.length; j++) {
				if (dis[j] == null) {
					double[] x = new double[data.get(indexX.get(j)).length - 1];
					for (int it = 0; it < data.get(indexX.get(j)).length - 1; it++) {
						x[it] = data.get(j)[it + 1];
					}
					dis[j] = FindNN2.findNN2(x, ftr);
				}
			}
			// long endTimeMissingValue = System.currentTimeMillis();
			// System.out.println("Time of Missing Value:"
			// + (endTimeMissingValue - startTimeMissingValue));
			return dis;
		} else {
			throw new RuntimeException("The Symbol is not in the Tree!");
		}
	}

	public static void main(String[] args) {
		//
		Map<Integer, List<Double>> mapParameter = MyReadParameter
				.getBreakPointsParameter(filePathParameter);
		MyReadFile fileDate = new MyReadFile(filePath);
		List<Double[]> data = fileDate.openfile();
		List<Double[]> data2 = new ArrayList<Double[]>(data);
		int ts = 0; // the location to find the first Shapelet
		int ql = 30;
		List<Integer> cls = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			cls.add(data.get(i)[0].intValue());
		}
		List<Integer> cls1 = new ArrayList<Integer>(cls);
		List<Double[]> DIS = new ArrayList<Double[]>();
		List<Double> ACC = new ArrayList<Double>();
		double accuracy = 0;
		int FTR = 1;
		int iter = 1;
		long startTime = System.currentTimeMillis();
		List<Integer> indexX = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			indexX.add(i);
		}
		List<Integer> dataX = new ArrayList<Integer>(indexX);
		// build the sax tree
		long startBuildTreeTime = System.currentTimeMillis();
		Map<String, List<ShapeletIndexModel>> augmentedTreeMap = buildAugmentedTreeMap(
				data, mapParameter, ql, w, a);
		long endBuildTreeTime = System.currentTimeMillis();
		System.out.println("Build Tree Time:"
				+ (endBuildTreeTime - startBuildTreeTime));
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
			for (int i = 1; i < c - ql; i++) {
				for (int n = 0; n < ql; n++) {
					ftr[n] = data2.get(ts)[i + n];
				}
//				long startComputeDistanceTime = System.currentTimeMillis();
				Double[] dis = saxComputeMatrix(data, ftr, mapParameter,
						indexX, augmentedTreeMap);
				// long endComputeDistanceTime = System.currentTimeMillis();
				// System.out.println("Compute Distance Time:"
				// + (endComputeDistanceTime - startComputeDistanceTime));
				for (int n = 0; n < dis.length; n++) {
					dis[n] = dis[n] / Math.sqrt(ql);
				}
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
			Double[] dis = saxComputeMatrix(data, ftr, mapParameter, indexX,
					augmentedTreeMap);
			for (int i = 0; i < dis.length; i++) {
				dis[i] = dis[i] / Math.sqrt(ql);
			}
			double corr = seps.get(indx).getCorr();
			Integer[] ind1 = MyArrayFunction.findSmallerThan(dis,
					Math.sqrt(2 * (1 - corr)));
			Integer[] ind2 = MyArrayFunction.findBiggerThan(dis,
					Math.sqrt(2 * (1 - corr)));
			double m1 = MyArrayFunction.meanOfArrayByIndex(dis, ind1);
			double m2 = MyArrayFunction.meanOfArrayByIndex(dis, ind2);
			double s1 = MyArrayFunction.stdOfArrayByIndex(dis, ind1);
			double s2 = MyArrayFunction.stdOfArrayByIndex(dis, ind2);
			Double[] dist = saxComputeMatrix(data, ftr, mapParameter, dataX,
					augmentedTreeMap);
			for (int i = 0; i < dist.length; i++) {
				dist[i] = dist[i] / Math.sqrt(ql);
			}
			DIS.add(dist);
			double tempACC = ClassifyFeats.classifyFeats(DIS, cls);
			ACC.add(tempACC);
			List<Double[]> temp = new ArrayList<Double[]>();
			temp.add(dis);
			accuracy = ClassifyFeats.classifyFeats(temp, cls1);
			System.out.println("DIS accuracy:" + tempACC + " time:" + FTR);
			System.out.println("Temp accuracy:" + accuracy + " time:" + FTR);
			if (tempACC == 1) {
				break;
			}
			Integer[] I = MyArrayFunction.findSmallerThan(dis, m1 + (0.5 * s1));
			for (int i = 0; i < I.length; i++) {
				cls2[I[i]] = 1;
			}
			List<Integer> X = Arrays.asList(MyArrayFunction.findEqual2Value(
					cls2, 0));
			List<Integer> tempIndexX = new ArrayList<Integer>();
			for (int j = 0; j < X.size(); j++) {
				tempIndexX.add(indexX.get(X.get(j)));
			}
			indexX = tempIndexX;
			List<Integer> listTempInteger = new ArrayList<Integer>();
			List<Double[]> listTempDoubleArray = new ArrayList<Double[]>();
			for (int i = 0; i < X.size(); i++) {
				listTempInteger.add(cls1.get(X.get(i)));
				listTempDoubleArray.add(data2.get(X.get(i)));
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
					ts = X.indexOf(arrayIndex[i]);
					break;
				}
			}
			System.out.println("Iteration Time:"
					+ (System.currentTimeMillis() - iterationTime) + " iter:"
					+ iter);
			iterationTime = System.currentTimeMillis();
			iter = iter + 1;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Running Time:" + (endTime - startTime));
	}

}
