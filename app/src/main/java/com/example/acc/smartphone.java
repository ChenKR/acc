package com.example.acc;

import java.math.BigDecimal;

import android.util.Log;

public class smartphone {

	private static final String TAG = null;

	public double smartphone(double x2, double y2, double z2) {
		// TODO Auto-generated method stub
	   double x=Math.abs(x2*x2);
       double y=Math.abs(y2*y2);
       double z=Math.abs(z2*z2);
       
       double at=x+y+z;
      
       double t=Math.sqrt(at);
       BigDecimal   b   =   new   BigDecimal(t);  
      double   f1  =    b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
    
   
   return f1;
	}
int p;
	public double difference(row[] roww, int count) {
		
		double fordsum = 0;
		for(p=0;p<((count-1)/2);p++){
			
			fordsum=roww[p+1].av+roww[p].av;
			
		}
		
		fordsum=fordsum/((count-1)/2);
		Log.e(TAG, "This is difat:"+fordsum);
		return fordsum;
	}

	public double meanbf(row[] roww, int count) {
		int q=p;
		double afsum = 0;
		for(q=p;q<count-1;q++){
			
			afsum=roww[q+1].av+roww[q].av;
			
		}
		
		afsum=afsum/(count/2);
		Log.e(TAG, "This is afsum:"+((count)/2));
		return afsum;
	}

}
