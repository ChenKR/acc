package com.example.acc;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.example.acc.falldetection.MyReceiver;



import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class detection extends  Service{
	MediaPlayer mp ;
	arr[] cosh=new arr[250];
	SensorManager sensormanager = null;
	Sensor accSensor = null;
	row cos=new row();
	char min; 
	double second;
	public String state="nonfall";
	int count,counter,counter2,counter3;
	public double x,y,z,minnumber,std,passtime,svm,delta,std11;
	private static final String TAG = "detection";
	smartphone smart=new smartphone();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent,int startId)
	{
		 mp = MediaPlayer.create(detection.this, R.raw.alert);
		sensormanager = (SensorManager)getSystemService(SENSOR_SERVICE);
	        accSensor  = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);	
	        sensormanager.registerListener(accSensorListener, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
		super.onStart(intent, startId);
	}
	
	@Override 
	public void onDestroy() { 
	Log.i("TAG","Services onDestory"); 
	sensormanager.unregisterListener(accSensorListener);
	
	super.onDestroy(); 
	} 
	
	private SensorEventListener accSensorListener = new SensorEventListener()
    {

		public void onAccuracyChanged(Sensor arg0, int arg1) 
		{		
			
		}
		long now = 0;
		long time = 0,second,i;
		int temp = 0;
		@Override
		public void onSensorChanged(SensorEvent event) 
		{
			double suma = 0,sumb = 0,avga,avgb,sumx1 = 0,sumx2 = 0,sumy1 = 0,sumy2 = 0,sumz1 = 0,sumz2 = 0,avgx1 = 0,avgx2 = 0,avgy1 = 0,avgy2 = 0,avgz1 = 0,avgz2 = 0;
			double std1 =0.3,suma1 = 0,sumb1 = 0,avga1,avgb1, std2 = 0;
			if(event.sensor == accSensor)
			{
				x = event.values[SensorManager.DATA_X]/9.8;
				y = event.values[SensorManager.DATA_Y]/9.8;
				z = event.values[SensorManager.DATA_Z]/9.8;
			}
			i=event.timestamp;
			svm=smart.smartphone(x,y,z);
			cos.cos(x, y, z);
			cosh[count]=new arr(svm,cos.cosx,cos.cosy,cos.cosz);
			if(count%250==0)
			{
				if(cos.cosx<cos.cosy)
				{  	
					min='x';
					minnumber=cos.cosx;
				}
				else
				{ 
					minnumber=cos.cosy;
					min='y';
				}
				if(cos.cosz<minnumber)
				{
					min='z';
				}
			}
			
			
			if(second>5000)
			{	
				if(svm>2.3)
				{
					counter=count-100;
					passtime=second-2000;
					counter2=count;
					counter3=count+100;
					state="mayfall";
					Log.e(TAG, "This is state:"+state);
				}
				
				if(state.equals("mayfall"))
				{
					
					if(counter<0)
						{
							counter=250-Math.abs(counter);
						}
					//Log.e(TAG, "This is count and counter:"+count+" "+counter[0]);
					if(count-counter==200)
					{
						Log.e(TAG, "This is count>counter:"+count+" "+counter);
						for(int i=counter;i<count;i++)
						{
							suma+=cosh[i].at;
							sumb+=Math.pow(cosh[i].at,2.0);
						}
						avga=suma/200;
						avgb=sumb/200;
						std=Math.sqrt(avgb-Math.pow(avga, 2.0));
						
					
						/*Log.e(TAG, "This is suma:"+suma);
						Log.e(TAG, "This is sumb:"+sumb);
						Log.e(TAG, "This is avga:"+avga);
						Log.e(TAG, "This is avgb:"+avgb);
						Log.e(TAG, "This is std:"+std);*/
					}
					else if((count+(250-counter))==200)
					{
						Log.e(TAG, "This is count<counter:"+count+" "+counter);
						for(int i=counter;i<250;i++)
						{
							suma+=cosh[i].at;
							sumb+=Math.pow(cosh[i].at,2.0);	
						}
						for(int j=0;j<=count;j++)
						{
							suma+=cosh[j].at;
							sumb+=Math.pow(cosh[j].at,2.0);
						}
						avga=suma/200;
						avgb=sumb/200;
						std=Math.sqrt(avgb-Math.pow(avga, 2.0));
						Log.e(TAG, "This is std:"+std);
						
					}
				}	
				if(std>0.3)
						{
					
							switch(min)
							{
							case 'x':
								if(counter2-counter==100)
								{
									for(int i=counter;i<counter2;i++)
									{
										sumx1+=cosh[i].cosx;	
									}
									avgx1=sumx1/100;
									counter2=0;
									Log.e(TAG, "This is avgx1:"+avgx1);
									Log.e(TAG, "This is second:"+second);
								}
								else if((counter2+(250-counter))==100)
								{
									for(int i=counter;i<250;i++)
									{
										sumx1+=cosh[i].cosx;	
									}
									for(int j=0;j<=counter2;j++)
									{
										sumx1+=cosh[j].cosx;
									}
									avgx1=sumx1/100;
									
									Log.e(TAG, "This is avgx1:"+avgx1);
								}
								if(count-counter2==100)
								{
									for(int i=counter2;i<count;i++)
									{
										sumx2+=cosh[i].cosx;	
									}
									avgx2=sumx2/100;
									
									Log.e(TAG, "This is avgx1:"+avgx2);
									Log.e(TAG, "This is second:"+second);
								}
								else if((count+(250-counter2))==100)
								{
									for(int i=counter2;i<250;i++)
									{
										sumx2+=cosh[i].cosx;	
									}
									for(int j=0;j<=count;j++)
									{
										sumx2+=cosh[j].cosx;
									}
									avgx2=sumx2/100;
									
									Log.e(TAG, "This is avgx2:"+avgx2);
								}
								Log.e(TAG, "This is min:"+min);
								
								delta=avgx2-avgx1;
							break;
							case 'y':
								if(counter2-counter==100)
								{
									for(int i=counter;i<counter2;i++)
									{
										sumy1+=cosh[i].cosy;	
									}
									avgy1=sumy1/100;
									
									Log.e(TAG, "This is avgy1:"+avgy1);
									Log.e(TAG, "This is second:"+second);
								}
								else if((counter2+(250-counter))==100)
								{
									for(int i=counter;i<250;i++)
									{
										sumy1+=cosh[i].cosy;	
									}
									for(int j=0;j<=counter2;j++)
									{
										sumy1+=cosh[j].cosy;
									}
									avgy1=sumy1/100;
									
									Log.e(TAG, "This is avgy1:"+avgy1);
								}
								if(count-counter2==100)
								{
									for(int i=counter2;i<count;i++)
									{
										sumy2+=cosh[i].cosy;	
									}
									avgy2=sumy2/100;
								
									Log.e(TAG, "This is avgy2:"+avgy2);
									Log.e(TAG, "This is second:"+second);
								}
								else if((count+(250-counter2))==100)
								{
									for(int i=counter2;i<250;i++)
									{
										sumy2+=cosh[i].cosy;	
									}
									for(int j=0;j<=count;j++)
									{
										sumy2+=cosh[j].cosy;
									}
									avgy2=sumy2/100;
									
									Log.e(TAG, "This is avgy2:"+avgy2);
								}
								Log.e(TAG, "This is min:"+min);
								delta=avgy2-avgy1;
							break;
							case 'z':
								if(counter2-counter==100)
								{
									for(int i=counter;i<counter2;i++)
									{
										sumz1+=cosh[i].cosz;	
									}
									avgz1=sumz1/100;
									
									Log.e(TAG, "This is avgz1:"+avgz1);
									Log.e(TAG, "This is second:"+second);
								}
								else if((counter2+(250-counter))==100)
								{
									for(int i=counter;i<250;i++)
									{
										sumz1+=cosh[i].cosz;	
									}
									for(int j=0;j<=counter2;j++)
									{
										sumz1+=cosh[j].cosz;
									}
									avgz1=sumz1/100;
									
									Log.e(TAG, "This is avgz1:"+avgz1);
								}
								if(count-counter2==100)
								{
									for(int i=counter2;i<count;i++)
									{
										sumz2+=cosh[i].cosz;	
									}
									avgz2=sumz2/100;
									
									Log.e(TAG, "This is avgz1:"+avgz2);
									Log.e(TAG, "This is second:"+second);
								}
								else if((count+(250-counter2))==100)
								{
									for(int i=counter2;i<250;i++)
									{
										sumz2+=cosh[i].cosz;	
									}
									for(int j=0;j<=count;j++)
									{
										sumz2+=cosh[j].cosz;
									}
									avgz2=sumz2/100;
									
									Log.e(TAG, "This is avgz2:"+avgz2);
								}Log.e(TAG, "This is min:"+min);
								delta=avgz2-avgz1;
								Log.e(TAG, "This is sumz2:"+sumz2);
							break;
							}
							std=0;
							state="fallstage";
							delta=Math.abs(delta);
						}
				Log.e(TAG, "This is delstate:"+state);	
				Log.e(TAG, "This is std:"+std);
						if(delta>0.25)
						{
							Log.e(TAG, "This is counter3:"+counter3+" "+count);
							
							if(counter3>250)
							{
								counter3=counter3-250;
							}
							
							if(count-counter3==100)
							{
								for(int i=counter3;i<count;i++)
								{
									suma1+=cosh[i].at;
									sumb1+=Math.pow(cosh[i].at,2.0);
								}
								avga1=suma1/100;
								avgb1=sumb1/100;delta=0;
								std1=Math.sqrt(avgb1-Math.pow(avga1, 2.0));
								
								//Log.e(TAG, "This is 12std:"+std1);
								
							}
							else if((count+(250-counter3))==100)
							{
								for(int i=counter3;i<250;i++)
								{
									suma1+=cosh[i].at;
									sumb1+=Math.pow(cosh[i].at,2.0);	
								}
								for(int j=0;j<=count;j++)
								{
									suma1+=cosh[j].at;
									sumb1+=Math.pow(cosh[j].at,2.0);
								}
								avga1=suma1/100;
								avgb1=sumb1/100;delta=0;
								std1 = Math.sqrt(avgb1-Math.pow(avga1, 2.0));
									
							}
							
							Log.e(TAG, "This is 12std:"+std1);
						}
						
						
						
							std11=std1;
						if(std1<0.2||Double.isNaN(std1))
							{
							std=0;delta=0;
								state="fall";
							}
						
						
				
				
				
			}		
					
			
			
			if(state.equals("fall"))
			{
				mp.start();			             
			}
			
			if (now != 0) 
			{
				temp=1;
				time = i - now;
				second=time/ 1000000;
			}
			// To set up now on the first event and do not change it while we do not have "nbElements" events
			if (temp == 0) 
			{
				now = i;
			}
			
		
		count++;
		if(count==250)
		{
			count=0;
		}
			
			 Intent intent = new Intent();
			 intent.putExtra("second", second);
			 intent.putExtra("svm", svm);
			 intent.putExtra("state", state);
			 intent.setAction("android.intent.action.test");
			 sendBroadcast(intent); 
			 Log.e(TAG, "This is x:"+x);
			 Log.e(TAG, "This is y:"+y);
			 Log.e(TAG, "This is z:"+z);
		}
    };
    
}
