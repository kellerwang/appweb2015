package com.CDM2012;

public class classifyFeats {
	public static double classifyFeats(double feats[][],double[]labels){
		int n = feats.length;
		int correct = 0;
		double[] x = new double[feats[0].length];
		double[] y = new double[feats[0].length];
		for(int i = 0;i < n;i++){
			double bsf = 1000000;
			int minI = -1;
			for(int j = 0;j<n;j++){
				if(i == j)
					continue;
				for(int num = 0;num < feats[0].length;num++){
					x[num] = feats[i][num];
					y[num] = feats[j][num];
				}
				ED ed = new ED();
				double d = ed.ED(x, y);
				if(d < bsf){
					bsf = d;
					minI = j;
				}
			}
			if(minI == -1){
				double mn = 0.02;
			}
			if(labels[minI] == labels[i]){
				correct = correct + 1;
			}
		}
		
		double accuary = correct/n;
		return(accuary);
	}

}
