package com.CDM2012;

import java.util.Arrays;
import java.util.Comparator;

public class Sortrows {
	public void Sortrows(double[][] arObjects, final int[] arOrders)  
    {  
        Arrays.sort(arObjects, new Comparator<Object>()  
        {  
            public int compare(Object oObjectA, Object oObjectB)  
            {  
                double[] arTempOne = (double[])oObjectA;  
                double[] arTempTwo = (double[])oObjectB;  
                for (int i = 0; i < arOrders.length; i++)  
                {  
                    int k = arOrders[i];  
                    if (arTempOne[k] > arTempTwo[k])  
                    {  
                        return 1;  
                    }  
                    else if (arTempOne[k] < arTempTwo[k])  
                    {  
                        return -1;  
                    }  
                    else  
                    {  
                        continue;  
                    }  
                }  
                return 0;  
            }  
        });  
    }
}
