package kaile.sax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.model.ShapeletIndexModel;

import util.distance.TSDistance;
import util.file.MyReadFile;
import util.file.MyReadParameter;
import util.normalize.Normalize;
import util.normalize.Symbolization;

public class SAXFeatureSelect {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePathParameter = "data" + File.separator + "Gaussian";
		Map<Integer, List<Double>> mapParameter = MyReadParameter
				.getBreakPointsParameter(filePathParameter);
		String filePath = "data" + File.separator + "Trace.txt";
		MyReadFile fileDate = new MyReadFile(filePath);
		List<Double[]> data = fileDate.openfile();
		int ql = 30;
		int w = 6;
		int a = 3;
		int size = data.get(0).length - ql;
		Map<String, List<ShapeletIndexModel>> augmentedTreeMap = new HashMap<String, List<ShapeletIndexModel>>();
		for (int i = 0; i < data.size(); i++) {
			// data每行第一列表示类别
			for (int j = 0; j < size; j++) {
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
		double[][][] distanceTable = new double[data.size()][data.size()][size];
		Iterator iter = augmentedTreeMap.entrySet().iterator();
		long sumComputeTime = 0;
		while (iter.hasNext()) {
			Entry<String, List<ShapeletIndexModel>> entry = (Entry<String, List<ShapeletIndexModel>>) iter
					.next();
			String key = entry.getKey();
			List<ShapeletIndexModel> val = entry.getValue();
			for (int i = 0; i < val.size() - 1; i++) {
				for (int j = i + 1; j < val.size(); j++) {
					if (val.get(i).getRow() != val.get(j).getRow()) {
						double temp = TSDistance
								.normalizeDistanceOfEqualLength(val.get(i)
										.getFtr(), val.get(j).getFtr());
						sumComputeTime++;
						if (distanceTable[val.get(i).getRow()][val.get(j)
								.getRow()][val.get(j).getColumn()] != 0) {
							if (distanceTable[val.get(i).getRow()][val.get(j)
									.getRow()][val.get(j).getColumn()] > temp) {
								distanceTable[val.get(i).getRow()][val.get(j)
										.getRow()][val.get(j).getColumn()] = temp;
							}
						} else {
							distanceTable[val.get(i).getRow()][val.get(j)
									.getRow()][val.get(j).getColumn()] = temp;
						}
						if (distanceTable[val.get(j).getRow()][val.get(i)
								.getRow()][val.get(i).getColumn()] != 0) {
							if (distanceTable[val.get(j).getRow()][val.get(i)
									.getRow()][val.get(i).getColumn()] > temp) {
								distanceTable[val.get(j).getRow()][val.get(i)
										.getRow()][val.get(i).getColumn()] = temp;
							}
						} else {
							distanceTable[val.get(j).getRow()][val.get(i)
									.getRow()][val.get(i).getColumn()] = temp;
						}
					}
				}
			}

		}
		long sumEmpty = 0;
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.size(); j++) {
				for (int j2 = 0; j2 < size; j2++) {
					if (i != j) {
						if (distanceTable[i][j][j2] == 0) {
							sumEmpty++;
						}
					}
				}
			}
		}
		System.out.println("sumComputeTime:" + sumComputeTime);
		System.out.println("sumEmpty:" + sumEmpty);
		System.out.println("ComputeRate:"
				+ (sumComputeTime + sumEmpty * (long) size)
				/ Math.pow((long) (data.size() * size), 2));
	}
}
