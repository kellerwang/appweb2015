package com.CDM2012;

public class computeMatrix {
	public static double[][] computeMatrix(double[][]ftr,double[][]data2){
		int n = data2.length;
		int F = ftr.length;
		double[][] dis = new double [n][F];
		double[][] loc = new double [n][F];
		for(int i = 0;i < n;i++){
			for(int j = 0;j < F;j++){
				dis[i][j] = 0;
			}
		}
		
		findNN findnn = new findNN();
		
		double[] x = new double[data2[0].length-1];
		double[] y = new double[ftr[0].length];
		
		for(int j = 0;j < n;j++){
			for(int i = 0;i < F;i++){
				for(int it = 0;it < data2[j].length-1;it++){
				//	System.out.println(data2[j].length-1);
					x[it] = data2[j][it+1];
				}
				for(int it2 = 0;it2 < ftr[i].length;it2++){
					y[it2] = ftr[i][it2];
				}
				
				
				double[] result = findnn.findNN(x, y); 
				loc[j][i] = result[0];
				dis[j][i] = result[1];
			}
		}
		return dis;
	}
}
