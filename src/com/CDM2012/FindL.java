package com.CDM2012;

public class FindL{
	public static int[] FindL(double[][] dis,double B){
		int it = 0;
		int length=0;
		double[] A = new double[dis.length];
		for (int i = 0; i < dis.length;i++){
			A[i] = dis[i][0];
		}
		for(int i = 0;i < A.length;i++){
			if(A[i] > B){
				length++;
			}
		}
		int[] ind2 = new int[length];
		for(int i = 0;i < A.length;i++){
			if(A[i] > B){
				ind2[it] = i;
				it++;
			}
		}
		return(ind2);
	}
}