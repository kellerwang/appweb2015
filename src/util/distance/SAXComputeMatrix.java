package util.distance;

import java.util.List;
import java.util.Map;

import util.normalize.Symbolization;

import com.CDM2012.FindNN2;
import com.model.ShapeletIndexModel;

public class SAXComputeMatrix {
	public static Double[] saxComputeMatrix(List<Double[]> data, double[] ftr,
			Map<Integer, List<Double>> mapParameter, List<Integer> indexX,
			Map<String, List<ShapeletIndexModel>> augmentedTreeMap, int w, int a) {
		Double[] dis = new Double[indexX.size()];
		// signify the ftr
		double[] ppa = Symbolization.getPPA(ftr, w);
		String sax = Symbolization.getSAX(ppa, a, mapParameter);
		if (augmentedTreeMap.containsKey(sax)) {
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
			return dis;
		} else {
			throw new RuntimeException("The Symbol is not in the Tree!");
		}
	}
}
