package com.sax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.sax.ThisIsATree.Node;

public class SymbolCreater {
	/*
	 * main方法
	 */
	public static void main(String[] args) throws IOException, InterruptedException{
		System.out.println("hello world!");
		int slidesize = 1;//滑动距离
		int symbolsize = 25;//符号化时间序列长度
		ThisIsATree tree = new ThisIsATree();//将树类实例化
		tree.createTree();//生成四叉树
		String filepath = "./data";
		Double border[] = new Double[3];
		List<Double> num = new ArrayList<Double>();
		
		readfile(num,filepath);//确定区间
		orderNum(num);
		border[0]=num.get(num.size()/4);
		border[1]=num.get(num.size()/2);
		border[2]=num.get(num.size()*3/4);
		readfile2(tree,slidesize,symbolsize,border,filepath);
		Node root = tree.nodeList.get(0);
		tree.preOrderTraverse(root);//前序遍历输出树
	}
	/*
	 * 符号化
	 */
	private static void createSymbol(ThisIsATree tree,String filename,int slidesize,int symbolsize,Double[] border) throws IOException{
		File file = new File("./data/"+filename);
		int windowsize = 4*symbolsize;//滑窗大小
		BufferedReader reader = null;
		String array[] = new String[5000];//时间序列
		Double num[] = new Double[5000];
		Double tempnum[] = new Double[5000];
		Double average[] = new Double[4];//滑窗4段时间序列均值
		String templist[];
		String temp1="";
		String temp2="";
		int nodeNum=85;//节点编号
		double sum=0;
		int line = 1;//时间序列编号
		try 
		{
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) 
            {
                // 显示行号
            	if(!tempString.equals(""))
            	{
            		System.out.println("line " + line + ": " + tempString);	
            		templist = tempString.split(",");
            		array[line-1]=templist[1];
            		line++;
            	}
            }
            reader.close();
        } 
		catch (IOException e) 
		{
            e.printStackTrace();
        } 
		finally 
		{
            if (reader != null) 
            {
                try 
                {
                    reader.close();
                } 
                catch (IOException e1) 
                {
                }
            }
        }
		
		for(int i = 0 ;i<line-1;i++)
		{
			num[i] = Double.parseDouble(array[i]);
			tempnum[i] = num[i];
			System.out.println("line "+i+" : "+num[i]);
		}
		
		for(int i = 0;i<line-windowsize;i+=slidesize)
		{
			temp1 = temp1 +"window "+ Integer.toString(i) +" :";
			//对每个滑窗进行符号化
			for(int j = 0;j<4;j++)
			{
				for(int k = 0;k<symbolsize;k++)
				{
					if(num[i+j*symbolsize+k]!=null)
					{				
						sum = sum + num[i+j*symbolsize+k];
					}
				}
				average[j] = sum/symbolsize;
				//符号化
				if(average[j]<border[1]){
					if(average[j]<border[0]){
						temp1 = temp1 +"a";
						temp2 = temp2 +"a,"; 
						//temp1 = temp1+Double.toString(average[j])+"-a"+"\r\n";
					}
					else{
						temp1 = temp1 +"b";
						temp2 = temp2 +"b,";
						//temp1 = temp1+Double.toString(average[j])+"-b"+"\r\n";
					}
				}
				else{
					if(average[j]<border[2]){
						temp1 = temp1 +"c";
						temp2 = temp2 +"c,";
						//temp1 = temp1+Double.toString(average[j])+"-c"+"\r\n";
					}
					else{
						temp1 = temp1 +"d";
						temp2 = temp2 +"d,";
						//temp1 = temp1+Double.toString(average[j])+"-d"+"\r\n";
					}	
				}
				sum=0;
			}
			
			//根据符号化结果计算滑窗对应叶节点
			String[] stringArr = temp2.split(",");
			switch (stringArr[0]) {
			case "a":	nodeNum+=0;
			break;
			case "b":	nodeNum+=1*64;
			break;
			case "c":	nodeNum+=2*64;
			break;
			case "d":	nodeNum+=3*64;
			break;
			default:
				break;
			}
			switch (stringArr[1]) {
			case "a":	nodeNum+=0;
			break;
			case "b":	nodeNum+=1*16;
			break;
			case "c":	nodeNum+=2*16;
			break;
			case "d":	nodeNum+=3*16;
			break;
			default:
				break;
			}
			switch (stringArr[2]) {
			case "a":	nodeNum+=0;
			break;
			case "b":	nodeNum+=1*4;
			break;
			case "c":	nodeNum+=2*4;
			break;
			case "d":	nodeNum+=3*4;
			break;
			default:
				break;
			}
			switch (stringArr[3]) {
			case "a":	nodeNum+=0;
			break;
			case "b":	nodeNum+=1;
			break;
			case "c":	nodeNum+=2;
			break;
			case "d":	nodeNum+=3;
			break;
			default:
				break;
			}
			//将滑窗编号存入对应叶节点
			tree.nodeList.get(nodeNum).setValue(filename+"-"+Integer.toString(i));
			temp1=temp1+"\r\n";
			nodeNum=85;
			temp2="";
			
		}
    	FileOutputStream fos1 = new FileOutputStream("./data/"+filename+"-symbol.txt");
    	fos1.write(temp1.getBytes());
    	fos1.close();
	}
	 /*
	  * 排序
	  */
	public static void orderNum(List<Double> tempnum){
		for(int i=0;i<tempnum.size();i++){
			for(int j=i+1;j<tempnum.size();j++){
				double temp=0;		    
				if(tempnum.get(i)>tempnum.get(j)){		     
					temp=tempnum.get(i);		     
					tempnum.set(i,tempnum.get(j));		     
					tempnum.set(j,temp);		    
				}		   
			}		  
		}	 
	 }
	/*
	 * 文件夹下所有txt文件读取
	 */
	public static boolean readfile(List<Double> num,String filepath) throws FileNotFoundException, IOException, InterruptedException {
    	try 
    	{
    		String filename="";
            File file = new File(filepath);
            if (!file.isDirectory()) 
            {
            	System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            } 
            else if (file.isDirectory()) 
            {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) 
                {
                	File tempfile = new File(filepath + "/" + filelist[i]);
                    if (!tempfile.isDirectory()) 
                    {
                    	filename = tempfile.getName();
                    	System.out.println("path=" + tempfile.getPath());
                        System.out.println("absolutepath="+ tempfile.getAbsolutePath());
                        System.out.println("name=" + tempfile.getName());
                    	symbolData(filename,num);//符号化
                    	
                    } 
                    else if (tempfile.isDirectory()) 
                    {
                    	System.out.println("hello");
                        readfile(num,filepath + "/" + filelist[i]);
                    }
                }

            }

    	} 
    	catch (FileNotFoundException e) 
    	{
            System.out.println("readfile()   Exception:" + e.getMessage());
    	}
    	return true;
    }
	public static boolean readfile2(ThisIsATree tree,int slidesize,int symbolsize,Double[] border,String filepath) throws FileNotFoundException, IOException, InterruptedException {
    	try 
    	{
    		String filename="";
            File file = new File(filepath);
            if (!file.isDirectory()) 
            {
            	System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            } 
            else if (file.isDirectory()) 
            {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) 
                {
                	File tempfile = new File(filepath + "/" + filelist[i]);
                    if (!tempfile.isDirectory()) 
                    {
                    	filename = tempfile.getName();
                    	System.out.println("path=" + tempfile.getPath());
                        System.out.println("absolutepath="+ tempfile.getAbsolutePath());
                        System.out.println("name=" + tempfile.getName());
                    	createSymbol(tree,filename,slidesize,symbolsize,border);//符号化
                    	
                    } 
                    else if (tempfile.isDirectory()) 
                    {
                        readfile2(tree,slidesize,symbolsize,border,filepath + "/" + filelist[i]);
                    }
                }

            }

    	} 
    	catch (FileNotFoundException e) 
    	{
            System.out.println("readfile()   Exception:" + e.getMessage());
    	}
    	return true;
    }
	
	
	private static void symbolData(String filename,List<Double> num) throws IOException{
		File file = new File("./data/"+filename);
		BufferedReader reader = null;
		String templist[];
		try 
		{
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) 
            {
                // 显示行号
            	if(!tempString.equals(""))
            	{
            		templist = tempString.split(",");
            		System.out.println(templist[1]);
            		num.add(Double.parseDouble(templist[1]));
            	}
            }
            reader.close();
        } 
		catch (IOException e) 
		{
            e.printStackTrace();
        } 
		finally 
		{
            if (reader != null) 
            {
                try 
                {
                    reader.close();
                } 
                catch (IOException e1) 
                {
                }
            }
        }
	}
}
