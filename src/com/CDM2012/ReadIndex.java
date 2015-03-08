package com.CDM2012;

public class ReadIndex {
	public static int ReadIndex(double[][]cls1,double[][]cls2){
		int L = cls1.length;
		int A = 0;
		int B = 0;		
		int C = 0;
		int D = 0;
		for(int i = 0;i < L;i++){
			for(int j = i+1;j < L;j++){
				
				if((cls1[i][1] == cls1[j][1])&&(cls2[i][1] == cls2[j][1]))
					A = A + 1;
				else if((cls1[i][1] != cls1[j][1])&&(cls2[i][1] != cls2[j][1]))
					B = B + 1;
				else if((cls1[i][1] == cls1[j][1])&&(cls2[i][1] != cls2[j][1]))
					C = C + 1;
				else
					D = D + 1;	
			}
		}
		int RI = (A+B)/(A+B+C+D);
		return(RI);
	}
}
