package com.CDM2012;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


public class featureSelect {
	public static void main(String[] args){
		openFile file = new openFile();
		double[][] data = file.openfile();//�����ݶ���double�͵Ķ�ά����
		
		double accuracy = 0;
		
		double[] cls1 = new double[data.length];//ȡʱ������
		for (int i = 0; i<data.length;i++){
			cls1[i] = data[i][0];
		}
		
		int ts = 1;
		
		double[][] data2 = data;
		int ql = 50;
		
		double[][] DIS = new double[200][1];
		double[] ACC = new double[5000];
		int FTR = 1;
		int iter = 1;
		
		while(true){
			int r = data.length;
			int c = data[0].length;
			double[][] cls2 = new double[200][1];
			for(int i = 0;i < data.length;i++){
				cls2[i][0] = 0;
			}
			double[][] seps = new double[225][5];
			double ftr[][] = new double[1][ql];
			int cnt = 1; 	
			
			for(int j = ql;j <= 50;j++){
				for(int i = 2;i <= c-ql;i++){
					for(int n = 0;n < ql;n++){
						ftr[0][n] = data2[ts-1][i-1+n];
					}
					computeMatrix CM = new computeMatrix();
					double[][] dis = CM.computeMatrix(ftr, data2); //-------------------------------�����ȷ
					for(int n = 0;n < dis.length;n++){
						for(int m = 0;m < dis[n].length;m++){
							dis[n][m] = dis[n][m]/Math.sqrt(ql);
						}
					}
					
					seps[cnt-1][0] = 0;  			//gap
					seps[cnt-1][1] = 0;				//corr
					seps[cnt-1][2] = 0;   			//q=length(ind1)/length(ind2)
					seps[cnt-1][3] = ql; 			//ql= qlen
					seps[cnt-1][4] = i;   			//index
					
					double corr = 0.95;
					
					for( corr = 0.95;corr >= 0.65;corr=corr-0.01){
						FindS finds = new FindS();
						int[] ind1 = finds.FindS(dis,Math.sqrt(2*(1-corr)));//-------------������
						FindL findl = new FindL();
						int[] ind2 = findl.FindL(dis, Math.sqrt(2*(1-corr)));//------------������
						if(ind1.length == 0||ind2.length == 0)
							continue;
						Mean mean = new Mean();
						Std std = new Std();
						double m1 = mean.Mean(dis, ind1, 0);
						double m2 = mean.Mean(dis, ind2, 0);
						double s1 = std.Std(dis, ind1, 0);
						double s2 = std.Std(dis, ind2, 0);
						
						double l1 = (double) ind1.length;
						double l2 = (double) ind2.length;
						double q = l1/l2;
						double curr = 0;
						
						if(q > 0.2&&q < 5)
							curr = (m2-s2-(m1+s1));
						else{
							curr = 0;
						}
						if(curr > seps[cnt-1][0]){
							seps[cnt-1][0] = curr;
							seps[cnt-1][1] = corr;
							seps[cnt-1][2] = q;
						}						
					}
					cnt = cnt + 1;
				}
				System.out.println(ql);
			}
			for(int i = 0;i < seps.length;i++){
				for(int j = 0;j <seps[0].length;j++){
					System.out.print(seps[i][j]);
					System.out.print("    ");
				}
				System.out.println();
			}
			double a[][] = seps;
			Sortrows sortrows = new Sortrows(); 
			int arOrders[] = {1,0};
			sortrows.Sortrows(a, arOrders);
			int indx = 0;
			for(int i = 0;i < a.length;i++){
				double q = a[i][2];
				if(q > 0.2&&q < 5){
					indx = i;
					break;
				}
			}
			if(indx == 0){
				break;
			}
			ql = (int) a[indx][3];
			
			for(int n = 0;n < ql;n++){
				int num = (int) a[indx-1][4];
				ftr[0][n] = data2[ts-1][num-1+n];
			}
			System.out.println(1);
			computeMatrix CM = new computeMatrix();
			double[][] dis = CM.computeMatrix(ftr, data2);
			for(int i = 0;i<dis.length;i++){
				for(int j = 0;j <dis[0].length;j++){
					dis[i][j] = dis[i][j]/Math.sqrt(ql);
				}
			}
			double corr = a[indx-1][1];
			FindS finds = new FindS();
			int[] ind1 = finds.FindS(dis,Math.sqrt(2*(1-corr)));
			FindL findl = new FindL();
			int[] ind2 = findl.FindL(dis,Math.sqrt(2*(1-corr)));
			Mean mean = new Mean();
			Std std = new Std();
			double m1 = mean.Mean(dis, ind1,0);
			double m2 = mean.Mean(dis, ind2,0);
			double s1 = std.Std(dis, ind1, 0);
			double s2 = std.Std(dis, ind2, 0);
			
			double[][] dist = CM.computeMatrix(ftr, data);
			for(int i = 0;i<dis.length;i++){
				for(int j = 0;j <dis[0].length;j++){
					dist[i][j] = dis[i][j]/Math.sqrt(ql);
				}
			}
			
			double[] labels = new double[data.length];
			for(int i = 0;i < data.length;i++){
				labels[i] = data[i][0]; 
			}
			
			for(int i = 0;i < dist[0].length;i++){
				DIS[i][0] = dist[i][0];
			}
			
			classifyFeats cf = new classifyFeats();
			ACC[FTR] = cf.classifyFeats(DIS,labels);
			
			accuracy = cf.classifyFeats(dis, cls1);
			System.out.println(accuracy);
			
			int[] I = finds.FindS(dis,m1+(0.5*s1));
			for(int i = 0;i<I.length;i++){
				cls2[I[i]][0] = 1;
			}
			FindE finde = new FindE();
			double[] Cl1 = cls1;
			int[] X = finde.FindE(cls2, 0);
			for(int i = 0;i < cls1.length;i++){
				if(i < X.length){
					cls1[i] = Cl1[X[i]];
				}
				else
					cls1[i] = 0;
			}
			
			for(int i =0;i <data2.length;i++){
				for(int j = 0;j<data2[0].length;j++){
					if (i < X.length){
						data2[i][j] = data[X[i]][j];
					}
					else
						data2[i][j] = 0;
				}
			}
			
			FTR = FTR + 1;
			if (ind1.length<2||ind2.length<2){
				break;
			}
			try{
				FileOutputStream fos=new FileOutputStream("D:/hadoop/matlab/ICDM2012_ClusteringTimeSeries/sampleDataset/result.txt"); 
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				BufferedWriter bw=new BufferedWriter(osw);   
				bw.write(ts);
				bw.newLine();
				String result = Double.toString(a[indx][4]);
				bw.write(result);
				bw.newLine(); 
				for(int i = 0;i < ftr.length;i++){
					for(int j = 0;j < ftr[0].length;j++){
						bw.write(Double.toString(ftr[i][j]));
					}
				bw.newLine(); 
				fos.close();
				osw.close();
				bw.close();
				}
			} catch (Exception ex){
			}
		}
	System.out.println("over");
	}
}
