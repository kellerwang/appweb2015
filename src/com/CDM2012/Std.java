package com.CDM2012;

public class Std {
	public static double Std(double[][] dis,int[] ind,int p){
		Mean mean = new Mean();
		double sum = 0;
		int it = 0;
		double avg = mean.Mean(dis, ind, p);
		for(int i = 0; i < ind.length;i++){
			sum = sum + Math.pow((dis[ind[i]][p]-avg),2);
			it++;
		}
		sum = sum/it;
		sum = Math.sqrt(sum);
		return(sum);
	}

}
