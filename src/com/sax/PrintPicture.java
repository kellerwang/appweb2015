package com.sax;

import java.awt.Font;  
import java.text.SimpleDateFormat;  

import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.DateAxis;  
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.plot.XYPlot;  
import org.jfree.data.time.Day;  
import org.jfree.data.time.TimeSeries;  
import org.jfree.data.time.TimeSeriesCollection;  
import org.jfree.data.xy.XYDataset;  

import java.awt.GridLayout;  
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;  

public class PrintPicture {
	ChartPanel frame1;
	public PrintPicture(){
		XYDataset xydataset = createDataset();  
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("开盘价", "日期", "价格",xydataset, true, true, true);  
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();  
        DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();  
        dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));  
        frame1=new ChartPanel(jfreechart,true);  
        dateaxis.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题  
        dateaxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题  
        ValueAxis rangeAxis=xyplot.getRangeAxis();//获取柱状  
        rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));  
        jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));  
        jfreechart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体 
	}
	private static XYDataset createDataset() {  //这个数据集有点多，但都不难理解  
        @SuppressWarnings("deprecation")
		TimeSeries timeseries = new TimeSeries("开盘价",Day.class);
        
        File file = new File("D:\\testts2\\600000.txt");
        BufferedReader reader = null;
		String array[] = new String[5000];
		Double num[] = new Double[5000];
		String templist[];
		String tempdate[];
		String temp="";
		int line = 1;
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
                tempdate = templist[0].split("-");
                System.out.println(tempdate[0]+tempdate[1]);
                if(tempdate[0].equals("2005")&&tempdate[1].equals("06"))
                {
                	timeseries.add(new Day(Integer.parseInt(tempdate[2]),Integer.parseInt(tempdate[1]),Integer.parseInt(tempdate[0])),Double.parseDouble(templist[1]));
                }
                temp = temp + templist[1]+"\r\n";
                array[line]=templist[1];
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
		
		for(int i = 1 ;i<line;i++)
		{
			num[i] = Double.parseDouble(array[i]);
			//num[i] = Double.valueOf(array[i]);
			System.out.println("line "+i+" : "+num[i]);
			
		}

        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();  
        timeseriescollection.addSeries(timeseries);  

        return timeseriescollection;  
    }  
  public ChartPanel getChartPanel(){  
        return frame1;  
          
    }  
  public static void main(String args[]){  
	    JFrame frame=new JFrame("Java数据统计图");  
	    frame.setLayout(new GridLayout(2,2,30,30));  

	    frame.add(new PrintPicture().getChartPanel());    //添加折线图  
	    frame.setBounds(0, 0, 800, 600);  
	    frame.setVisible(true);  
	}  
}  

