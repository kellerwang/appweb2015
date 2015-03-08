package com.CDM2012;

public class FindS{
	public static int[] FindS(double[][] dis,double B){
		int it = 0;
		int length=0;
		double[] A = new double[dis.length];
		for (int i = 0; i < dis.length;i++){
			A[i] = dis[i][0];
		}
		for(int i = 0;i < A.length;i++){
			if(A[i] < B){
				length++;
			}
		}
		int[] ind1 = new int[length];
		for(int i = 0;i < A.length;i++){
			if(A[i] < B){
				ind1[it] = i;
				it++;
			}
		}
		return(ind1);
	}
}
