package util.file;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import util.config.ParameterConfig;

public class MyWriteFile {
	public static void write2Log(String content) {
		write2FileAppend(ParameterConfig.RESULT_LOG_OUTPUT, content + "\n");
	}

	public static void write2ACCFile(List<Double> ACC) {
		for (int i = 0; i < ACC.size(); i++) {
			write2File(ParameterConfig.ACC_OUTPUT, ACC.get(i) + "\n");
		}
	}

	public static void write2FTRFile(double[] ftr, int iter) {
		if (iter > 1) {
			for (int j = 0; j < ftr.length; j++) {
				MyWriteFile.write2FileAppend(ParameterConfig.FTR_OUTPUT,
						Double.toString(ftr[j]));
				if (j != ftr.length - 1) {
					MyWriteFile.write2FileAppend(ParameterConfig.FTR_OUTPUT,
							",");
				}
			}
			MyWriteFile.write2FileAppend(ParameterConfig.FTR_OUTPUT, "\n");
		} else {
			MyWriteFile.write2File(ParameterConfig.FTR_OUTPUT, "");
			for (int j = 0; j < ftr.length; j++) {
				MyWriteFile.write2FileAppend(ParameterConfig.FTR_OUTPUT,
						Double.toString(ftr[j]));
				if (j != ftr.length - 1) {
					MyWriteFile.write2FileAppend(ParameterConfig.FTR_OUTPUT,
							",");
				}
			}
			MyWriteFile.write2FileAppend(ParameterConfig.FTR_OUTPUT, "\n");
		}
	}

	public static void write2FileAppend(String fileName, String content) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName, true);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void write2File(String fileName, String content) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
