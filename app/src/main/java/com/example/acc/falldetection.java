package com.example.acc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.ads.*;
import android.widget.ImageView;
import com.example.acc.GifAnimationDrawable;
import android.graphics.drawable.AnimationDrawable;
import com.ant.liao.GifView;

 class arr {
	 	double at;
	 	double cosx;
		double cosy;
		double cosz;
	 public arr(double at2 ,double cosx2, double cosy2, double cosz2) {
		// TODO Auto-generated constructor stub
		at=at2;
		cosx=cosx2;
		cosy=cosy2;
		cosz=cosz2;
	}
	
	 
}
public class falldetection extends Activity {
    private PowerManager mPowerManager;
    private AdView adView;
	public double svm,second;
	private static final String TAG = "falldetection";
    public int count=0;
	SensorManager sensormanager = null;
	List<Sensor> sensorList;
	Sensor accSensor = null;
	TextView txt = null,txt_1 = null,txt_at = null,txt_state;
	public String state="nonfall",message="您的長輩發生跌倒，請盡速前往察看。";
	Button imok,start,stop;
	smartphone smart=new smartphone();
	detection detect=new detection();
	 MyReceiver receiver; 
	 EditText phonenumber;
	 SmsManager smsManager;
	 private  GifView  gif3;
	private boolean f = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		gif3 = (GifView) findViewById(R.id.gif2);
		// 設置Gif圖片源
		gif3.setGifImage(getResources().openRawResource(R.raw.walker));
		// 添加監聽
		//gif3.setOnClickListener(this);
		//設置顯示的大小，拉伸或者壓縮
		gif3.setShowDimension(300, 300);
		// 設置加载方式：先載入後顯示、邊仔入編顯示、只顯示第一偵再顯示
		//gif3.setGifImageType(GifView.GifImageType.COVER);
        gif3.showCover();


		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
       mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass()
               .getName());
        //sensormanager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //accSensor  = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);	
        findViews();
        start.setOnClickListener(startservice);
        smsManager = SmsManager.getDefault();
        stop.setEnabled(false);
        stop.setOnClickListener(stopservice);
        imok.setOnClickListener(ok);
		
       // sensormanager.registerListener(new accSensorListener(), accSensor, SensorManager.SENSOR_DELAY_FASTEST);


		
        //runAppFromApkFileOnSdCard();
        
    }
    protected void runAppFromApkFileOnSdCard() {

    	
    	Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.exigo.tiny");
    	startActivity( LaunchIntent );
    	/*String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/com.google.zxing.client.android-1.apk";
 PackageManager pm = getPackageManager();

 PackageInfo info = pm.getPackageArchiveInfo(apkPath, 
                        PackageManager.GET_ACTIVITIES);
 //Log.i("ActivityInfo", "Package name is " + info.packageName);

 for (android.content.pm.ActivityInfo a : info.activities) {
     Log.i("ActivityInfo", a.name);
 }*/
    }
    private void findViews() {
    	txt= (TextView)findViewById(R.id.second); 
        txt_at= (TextView)findViewById(R.id.SVM);
        imok=(Button) findViewById(R.id.OK);
        start=(Button) findViewById(R.id.start_service);
        stop=(Button) findViewById(R.id.stop_service);
        txt_state =(TextView)findViewById(R.id.state);
        phonenumber=(EditText)findViewById(R.id.editText1);
       // iv = (ImageView)findViewById( R.id.image1);
    }

    public  void writeFiles() {
    	
	   	 
	   	
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED) ){
		try {
			
		File myFile =  new File("/sdcard/loge.txt");
		BufferedWriter bw = new BufferedWriter( new FileWriter(myFile , true));
		//Log.e(TAG, "This is dif:"+count);
		bw.write(second+" "+svm+" "+state+"\r\n");
		
		bw.close();
		
		} catch (IOException e) {
		e.printStackTrace();
		}}
}
    public class MyReceiver extends BroadcastReceiver { 
        //自定义一个广播接收器 
    	
        @Override 
        public void onReceive(Context context, Intent intent) { 
          // TODO Auto-generated method stub 
          //System.out.println("OnReceiver"); 
          Bundle bundle=intent.getExtras(); 
           second=bundle.getDouble("second");
           svm=bundle.getDouble("svm");
           state=bundle.getString("state");
          txt.setText("Second : " + second + " ms");
          txt_at.setText("SVM:"+svm);
          Log.e(TAG, "This is count:"+count);
          if(count==1)
			{
      	  smsManager.sendTextMessage(phonenumber.getText().toString(), 
       			 null, message,
       			 PendingIntent.getBroadcast(getApplicationContext(), 0,new Intent(), 0),
       			 null);
      	NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);          
		Intent notifyIntent = new Intent(falldetection.this,falldetection.class);
		notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
		notifyIntent.setAction(Intent.ACTION_MAIN);
		notifyIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent appIntent=PendingIntent.getActivity(falldetection.this,0,
		                                                  notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);


				NotificationCompat.Builder builder =
						new NotificationCompat.Builder(falldetection.this);

                builder.setSmallIcon(R.drawable.ic_launcher)
                        .setTicker("notification on status bar")
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Custom effect")
						.setContentText("notification on status bar.")
						.setContentInfo("3");

               // Notification notification = new Notification();


				// 建立震動效果，陣列中元素依序為停止、震動的時間，單位是毫秒
				long[] vibrate_effect = {1000, 500, 1000, 400, 1000, 300, 1000, 200, 1000, 100};
// 設定震動效果
				builder.setVibrate(vibrate_effect);

// 設定閃燈效果，參數依序為顏色、打開與關閉時間，單位是毫秒
				builder.setLights(1000,1000,1000);
// 建立通知物件
				Notification notification = builder.build();
// 使用CUSTOM_EFFECT_ID為編號發出通知
				manager.notify(0, notification);
		//送出Notification
		manager.notify(0,notification);
		}
          if(state.equals("fall"))
			{
        	 txt_state.setTextColor(0xFFFF0000);
        	 txt_state.setText("State: fall");
        	 count=count+1;
        	 if(count>3)
        	 {
        		 count=2;
        	 }
  		  	}
         
          if(state.equals("nonfall"))
			{
        	  txt_state.setTextColor(0xFF000000);
        	  txt_state.setText("State: nonfall");
        	  count=0;
			}
         writeFiles();
        } 
       
        
      } 
    @Override

  public boolean onKeyDown(int keyCode, KeyEvent event) 
    {

       if (keyCode == KeyEvent.KEYCODE_BACK)
       {
    	   Intent intent = new Intent(falldetection.this, detection.class);
	        stopService(intent);
	       
            finish(); 
       }
      return false;
    }

    private OnClickListener startservice = new OnClickListener()
        {
            public void onClick(View v)
             {

                 gif3.showAnimation();
            	stop.setEnabled(true);
            	phonenumber.setEnabled(false);
            	Intent intent = new Intent(falldetection.this, detection.class);
		        startService(intent);
		        count=0;
		        receiver=new MyReceiver();
		        IntentFilter filter=new IntentFilter();
		        filter.addAction("android.intent.action.test");
		        falldetection.this.registerReceiver(receiver,filter);

             }

        };

 private OnClickListener stopservice = new OnClickListener()
        {
            public void onClick(View v)
             {
				 gif3.showCover();
				 Intent intent = new Intent(falldetection.this, detection.class);
		        stopService(intent);
		        falldetection.this.unregisterReceiver(receiver); 
		        stop.setEnabled(false);
		        phonenumber.setEnabled(true);
		        count=0;
             }
        };  
        private OnClickListener ok = new OnClickListener()
        {
            public void onClick(View v)
             {
				 gif3.showAnimation();
				 Intent intent = new Intent(falldetection.this, detection.class);
		        stopService(intent);
		        startService(intent);
             }
        };
        @Override
        protected void onResume() {
            super.onResume();
	        
        }
       
        @Override
        protected void onPause() {
            super.onPause();
           
           
        }   

   
    
	
}
