package com.sax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class TimeseriesCreater {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world!");
        try
        {
            readfile("d://testts");
        } 
        catch (FileNotFoundException ex) 
        {
        } 
        catch (IOException ex) 
        {
        }
        System.out.println("ok");
    }
    public static boolean readfile(String filepath) throws FileNotFoundException, IOException, InterruptedException {
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
                    	File tempfile = new File(filepath + "\\" + filelist[i]);
                        if (!tempfile.isDirectory()) 
                        {
                        	filename = tempfile.getName();
                        	creatts(tempfile,filename);
                        	System.out.println("path=" + tempfile.getPath());
                            System.out.println("absolutepath="+ tempfile.getAbsolutePath());
                            System.out.println("name=" + tempfile.getName());
                        } 
                        else if (tempfile.isDirectory()) 
                        {
                            readfile(filepath + "\\" + filelist[i]);
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
    
    public static void creatts(File file,String filename) throws IOException, InterruptedException{
		String temp="";
        BufferedReader reader = null;
        String templist[];
        
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            reader.readLine();
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                templist = tempString.split(",");
                temp=temp+templist[0]+","+templist[1]+"\r\n";
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		FileOutputStream fos = new FileOutputStream("D:\\testts2\\"+filename);
    	fos.write(temp.getBytes());
    	fos.close();
										    	

	}

    
    
}
