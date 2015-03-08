package com.CDM2012;

public class ED {
	public double ED(double[] p,double[] q){
		double sum = 0;
		for (int i = 0;i <p.length;i++){
			double a =p[i]-q[i];
			sum =sum + Math.pow(a, 2);
		}
		sum = Math.sqrt(sum);
		return(sum);
	}
}
