package com.CDM2012;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class openFile {
	public static double[][] openfile(){
		 
        List<String[]> data = new ArrayList<>();
 
        try {
            Scanner in = new Scanner(new File("D:/hadoop/matlab/ICDM2012_ClusteringTimeSeries/sampleDataset/Trace.txt"));
 
            while (in.hasNextLine()) {
                String str = in.nextLine();
 
                data.add(str.split(" "));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // listת��Ϊ����
        String[][] result = data.toArray(new String[][] {});
        int it = 0;
        int it2 = 0;
        
        for (int i = 0;i < result[0].length;i++){
        	String st = result[0][i];
        	if (st != ""&&st != null&&st.length() > 0){
        		it++;
        	}
        }
       
        double[][] dresult = new double[result.length][it];
        for (int i = 0;i < result.length;i++) {
        	String[] strings = result[i];
            for (int j=0;j < result[i].length;j++) {
            	String string = result[i][j];
            	if (string != ""&&string != null&&string.length() > 0){
            	BigDecimal bd = new BigDecimal(string);
            	double test = bd.doubleValue();
            	dresult[i][it2] = test;
            	it2++;
            	}
            }
            it2 = 0;
        }
        return(dresult);
    }	
}
