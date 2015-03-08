package com.CDM2012;

public class Mean {
	public static double Mean(double[][]dis,int[] ind,int p){
		double sum =0;
		int it = 0;
		for (int i = 0;i < ind.length;i++){
			sum = sum + dis[ind[i]][p];
			it++;
			System.out.println(i);
		}
		sum = sum/it;
		return sum;
	}
}
