package util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MyReadFile {
	private String filePath;
	private int numColumn = 0;
	private int numRow = 0;

	public int getNumColumn() {
		return numColumn;
	}

	public int getNumRow() {
		return numRow;
	}

	public MyReadFile(String filePath) {
		super();
		this.filePath = filePath;
	}

	public List<Double[]> openfile() {
		numColumn = 0;
		numRow = 0;
		List<String[]> data = new ArrayList<>();
		try {
			Scanner in = new Scanner(new File(filePath));
			while (in.hasNextLine()) {
				String str = in.nextLine();
				data.add(str.split(" "));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[][] result = data.toArray(new String[][] {});
		for (int i = 0; i < result[0].length; i++) {
			String str = result[0][i];
			if (str != null && str != "" && str.length() > 0) {
				numColumn++;
			}
		}
		numRow = result.length;
		List<Double[]> doubleResult = new ArrayList<Double[]>();
		for (int i = 0; i < numRow; i++) {
			Double[] row = new Double[numColumn];
			int indx = 0;
			for (int j = 0; j < result[i].length; j++) {
				String str = result[i][j];
				if (str != null && str != "" && str.length() > 0) {
					BigDecimal bd = new BigDecimal(str);
					double temp = bd.doubleValue();
					row[indx] = temp;
					indx++;
				}
			}
			doubleResult.add(row);
		}
		return doubleResult;
	}
}
