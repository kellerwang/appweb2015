package util.normalize;

import java.util.List;
import java.util.Map;

public class Symbolization {
	public static double[] getPPA(double[] data, int w) {
		double[] dataNormalize = Normalize.normalize(data);
		int length = dataNormalize.length;
		if (length % w != 0) {
			throw new RuntimeException("Length cannot be divisible by w!");
		}
		double[] result = new double[w];
		for (int i = 0; i < w; i++) {
			double sum = 0;
			for (int j = (length / w) * i; j < (length / w) * (i + 1); j++) {
				sum += dataNormalize[j];
			}
			result[i] = sum / (double) length * (double) w;
		}
		return result;
	}

	public static String getSAX(double[] data, int a,
			Map<Integer, List<Double>> mapParameter) {
		List<Double> parameterLisDoubles = mapParameter.get(a);
		String resultString = "";
		for (int i = 0; i < data.length; i++) {
			char tempLabelChar = 'a';
			for (int j = 0; j < parameterLisDoubles.size(); j++) {
				// 上边界
				if (data[i] > parameterLisDoubles.get(j)) {
					tempLabelChar += 1;
				} else {
					break;
				}
			}
			resultString += tempLabelChar;
		}
		return resultString;
	}
}
