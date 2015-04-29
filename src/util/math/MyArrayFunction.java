package util.math;

import java.util.ArrayList;
import java.util.List;

public class MyArrayFunction {
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
}
