package util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyReadParameter {
	public static Map<Integer, List<Double>> getBreakPointsParameter(
			String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] strTempArray = tempString.split(",");
				List<Double> tempList = new ArrayList<Double>();
				for (int i = 1; i < strTempArray.length; i++) {
					tempList.add(Double.parseDouble(strTempArray[i]));
				}
				map.put(Integer.parseInt(strTempArray[0]), tempList);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return map;
	}
}
