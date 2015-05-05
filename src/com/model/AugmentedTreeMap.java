package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.normalize.Symbolization;

public class AugmentedTreeMap {
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
}
