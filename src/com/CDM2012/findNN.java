package com.CDM2012;

public class findNN {
	public double[] findNN(double[] x,double[] y){
		double[] xt = x;
		double[] yt = y;
		double[] finalRE = new double[2];
		int n;
		int it = 0;
		double sum = 0;
		double std = 0;
		int m;
		
		
		n = x.length;
		
		for(int i = 0;i<y.length;i++){
			sum = sum + y[i];
			it++;
		}
		
		sum = sum/it;
		for (int i = 0;i<y.length;i++){
			std = std + Math.pow((y[i]-sum),2);
		}
		std = std/it;
		std = Math.sqrt(std);
		
		for(int i = 0;i<y.length;i++){
			y[i] = (y[i]-sum)/std;
		}
		m = y.length;
		double[] yy = new double[m];
		int j = 0;
		for(int i = m;i>0;i--){
			yy[j] = y[i-1];
			j++;
		}
		y = yy;
		double[] nx = new double[2*n];
		double[] ny = new double[2*n];
		for(int i = 0;i<n;i++){
			nx[i] = x[i];
		}
		for(int i = 0;i<m;i++){
			ny[i] = y[i];						//��չ����query
		}
		
		//����Ҷ
		FFT fft = new FFT();
		Complex[] fx = new Complex[1024];
		Complex[] fy = new Complex[1024];
		for(int i = 0;i < 1024;i++){
			if(i < 2*n){
				Complex nfx = new Complex(nx[i],0);
				Complex nfy = new Complex(ny[i],0);
				fx[i] = nfx;
				fy[i] = nfy;
				}
			else{
				Complex zero = new Complex(0,0);
				fx[i] = zero;
				fy[i] = zero;
			}
		}
		Complex[] X = fft.fft(fx);
		Complex[] Y = fft.fft(fy);
		Complex[] Z = new Complex[X.length];
		for(int i = 0;i < X.length;i++){
			Z[i] = X[i].times(Y[i]);
		}
		double[] z = new double[n];
		Complex[] zz = fft.ifft(Z);				
		for(int i = 0;i<n;i++){
			z[i] = zz[i].re();			//�ó�����Ҷ�任��Ľ��
		}
		
		double sumy = 0;
		double sumy2 = 0;
		for (int i = 0;i<m;i++){
			sumy = sumy + y[i];   
			sumy2 = sumy2 + Math.pow(y[i], 2);
		}
		
		double[] cum_sumx = new double[2*n];
		double[] cum_sumx2 = new double[2*n];
		double[] sumx = new double[n-m];
		double[] sumx2 = new double[n-m];
		double[] meanx = new double[n-m];
		double[] sigmax2 = new double[n-m];
		double[] sigmax = new double[n-m];
		cum_sumx[0] = x[0];
		cum_sumx2[0] = Math.pow(x[0], 2);
		for(int i = 1;i < 2*n ;i++){
			cum_sumx[i] = cum_sumx[i-1] + nx[i];
			cum_sumx2[i] = cum_sumx2[i-1] + Math.pow(nx[i], 2);
		}
		for(int i = 0;i < n-m;i++){
			sumx[i] = cum_sumx[m+i]-cum_sumx[i];
			sumx2[i] = cum_sumx2[m+i]-cum_sumx2[i];
		}
		for(int i = 0;i < n-m;i++){  
			meanx[i] = sumx[i]/m; 
		}
		for(int i = 0;i < n-m;i++){
			sigmax2[i] = (sumx2[i]/m) - (Math.pow(meanx[i], 2));
			sigmax[i] = Math.sqrt(sigmax2[i]);
		}
		
		double[] dist = new double[n-m];
		for(int i = 0;i < n-m;i++){
			dist[i] = ((sumx2[i] - (2*sumx[i]*meanx[i]) + (m*Math.pow(meanx[i], 2)))/sigmax2[i]) - 
					(((2*z[m + i])-(sumy*meanx[i]))/sigmax[i]) + 
					sumy2;
		}
		for(int i = 0;i <n-m;i++){
			dist[i] = Math.sqrt(dist[i]);
		}
		
		Min min = new Min();
		double[] result = min.Min(dist);
		finalRE[0] = result[1] + 1;
		finalRE[1] = result[0];
		return(finalRE);
	}
}

















