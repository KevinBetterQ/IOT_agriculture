package com.example.skzh;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skzh.iot.SerialApp;
import com.skzh.iot.SerialPort;

public class MainActivity extends Activity implements OnClickListener {
	
	GestureDetector detector;
    //private ImageButton smarthome;
    
    
    //定义一些存储串口数据的变量
  	protected SerialApp mSerialApp;
  	protected SerialPort mSerialPort;
  	public static OutputStream mOutputStream;
  	public static InputStream mInputStream;
	
	ComReadThread comread;//自己定义了一个线程来读取串口数据
	//private Socket socket;
	//public static boolean bcloudFlg = false;//全局布尔变量：是否连接云平台
    //public boolean bHeartbeat =false; //全局布尔变量：是否在发送云平台的心跳包
	byte [] bymacTmp = {0,0,0,0,0,0};
	byte [] byEnCmd={0x40,0x06,0x01,0x06,0x00,0x00};
	byte [] byPwmCMD={0x40,0x06,0x01,0x09,0x00,0x00};
	byte [] byRelayCMD={0x40,0x07,0x01,0x0a,0x00,0x00,0x00};
	byte [] byVolOutCMD = {0x40,0x0e,0x01,0x0d,0x00,0x00,0x00,0x00,0x00,
			0x00,0x00,0x00,0x00,0x00};
    Timer timer;//= new Timer();   
    
    private void initSerialPort(){
        try {
        	mSerialApp 		= new SerialApp();
			mSerialPort  	= mSerialApp.getSerialPort(this);
			mInputStream	= mSerialPort.getInputStream();
			mOutputStream   = mSerialPort.getOutputStream();
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		
        //smarthome.setOnClickListener(this);
        initSerialPort();//初始化串口变量
        comread = new ComReadThread();
        comread.start();
        
        Intent intent = new Intent();
		intent.setClass(MainActivity.this,SmartHost.class);
		MainActivity.this.startActivity(intent);
		
	}

   
    
    //另外开辟了一个线程 对串口数据进行转化 ，并将数据广播出去
    class ComReadThread extends Thread {
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
    		super.run();
    		while(!isInterrupted())
    		{
				if (MainActivity.mInputStream == null) 
				{
					return;
				}
			
			int size;
			try {
				int idataindex = 0;//数据帧头的下标
				byte[] buffer =new byte[128];
				size = MainActivity.mInputStream.read(buffer);
				boolean  bfinddata = false;
				while(!bfinddata)//find 0x40 
				{
					if(buffer[idataindex] != (byte)0x40)
					{
						idataindex ++;
						if(idataindex >=124)
						{
							idataindex = 0;
							break;
						}
						
					}
					else if(buffer[idataindex+4] == (byte)0xaa)//心跳帧
					{
						Log.v("skzh","xintiaobao");
			
						sendBeatBroadcast(buffer[idataindex+3]);//处理心跳
						idataindex += 16;
						if(idataindex >= 124)
						{
							idataindex = 0;
							break;
						}
					}
					else if((buffer[idataindex+3]>(byte)0x00)&&(buffer[idataindex+3]<0x0e))//传感器数据帧
					{
						bfinddata = true; 		             
					}
					else{
						idataindex ++;
						if(idataindex >=124){
							idataindex=0;
						}
					}
				}
				
				if(!bfinddata){
					continue;
				}
				byte sum=0;
                for (int i = 0; i < buffer[idataindex+1]-1; i++) 
               {
                		sum+= buffer[i];
                 }			
				int lenth = buffer[idataindex+1];
				if ((buffer[idataindex+lenth-1]==sum)) {
					byte[] data =new byte[16];
					for(int i=0;i<lenth;i++)
					{
						data[i] = buffer[i];
					}
					Log.v("skzh","datareceived");
					processData(data, lenth);
					bfinddata =false;
					idataindex = 0;
				}
				else
				{
					Log.v("skzh","size <11 or sumcheck is wrong");
					idataindex = 0;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
    		}
    	}//end run
    	
    	

    	public void processData(final byte[] buffer, final int size)
    	{
    		
    		System.out.println(buffer[2]);
    		System.out.println(buffer[3]);
    		switch(buffer[3])
    		{
    			case 0x02://temp
    			{
                	//发送temp的广播
                	int temp = (0xff&buffer[5])*256 + 0xff&buffer[6];//温度
                	int humi = (0xff&buffer[7])*256 + 0xff&buffer[8];//湿度
                	double light = (buffer[9]*256+buffer[10])*3012.9/(32768*4);//光照
	    			Intent intent = new Intent("com.skzh.iot.temp");
	    			intent.putExtra("temp", temp);
	    			intent.putExtra("humi", humi);
	    			intent.putExtra("light", light);
	    			sendBroadcast(intent);
	    			
	    			Intent intentSmarthome = new Intent("com.skzh.iot.smarthome");
	    			intentSmarthome.putExtra("type", 2);
	    			intentSmarthome.putExtra("temp", temp);
	    			intentSmarthome.putExtra("humi", humi);
	    			intentSmarthome.putExtra("light", light);
	    			sendBroadcast(intentSmarthome);
    				break;
    			}
    			case 0x04://smoke
    			{
                	//发送广播
            		Intent intent = new Intent("com.skzh.iot.smoke");
	    			intent.putExtra("smoke",(int)buffer[5]);
	    			sendBroadcast(intent);
	    			
	    			Intent intentSmarthome = new Intent("com.skzh.iot.smarthome");
	    			intentSmarthome.putExtra("type", 4);
	    			intentSmarthome.putExtra("smoke", (int)buffer[5]);
	    			sendBroadcast(intentSmarthome);
    				break;
    			}
    			case 0x05://热释电
    			{
                	//发送广播
            		Intent intent = new Intent("com.skzh.iot.doppler");
	    			intent.putExtra("doppler", (int)buffer[5]);
	    			sendBroadcast(intent);
	    			
	    			Intent intentSmarthome = new Intent("com.skzh.iot.smarthome");
	    			intentSmarthome.putExtra("type", 5);
	    			intentSmarthome.putExtra("doppler", (int)buffer[5]);
	    			sendBroadcast(intentSmarthome);
    				break;
    			}
    			case 0x06://电机和led灯
    			{
    				if((byte)buffer[4]==(byte)0xdd)
    				{	
	                	//发送广播
	                	Intent intent = new Intent("com.skzh.iot.engine");
		    			intent.putExtra("engine", (int)buffer[5]);
		    			sendBroadcast(intent);
		    			Intent intentLight = new Intent("com.skzh.iot.enginelight");
		    			int tmp = buffer[5];
		    			intentLight.putExtra("lights", tmp);
		    			sendBroadcast(intentLight);
		    			
		    			Intent intentSmarthome = new Intent("com.skzh.iot.smarthome");
		    			intentSmarthome.putExtra("type", 6);
		    			intentSmarthome.putExtra("engine", (int)buffer[5]);
		    			sendBroadcast(intentSmarthome);
    				}
    				break;
    			}
    			case 0x09://pwm
    			{
	                	//发送广播
	                  	Intent intent = new Intent("com.skzh.iot.pwm");
	                	int ipwm = buffer[5];
		    			intent.putExtra("pwm", ipwm);
		    			sendBroadcast(intent);
		    			
		    			Intent intentSmarthome = new Intent("com.skzh.iot.smarthome");
		    			intentSmarthome.putExtra("type", 9);
		    			intentSmarthome.putExtra("pwm", (int)buffer[5]);
		    			sendBroadcast(intentSmarthome);
    				}//end if 
    				break;
    			
    			case 0x0b:
    			{
                	//发送广播电流
                	Intent intent = new Intent("com.skzh.iot.current");
	    			intent.putExtra("cur1", (int)buffer[5]);
	    			intent.putExtra("cur2", (int)buffer[7]);
	    			sendBroadcast(intent);
    				break;
    			}
    			case 0x0c:
    			{
                	//发送广播电压传感
                	Intent intent = new Intent("com.skzh.iot.voltage");
	    			intent.putExtra("vol1", (int)buffer[5]);
	    			intent.putExtra("vol2", (int)buffer[7]);
	    			sendBroadcast(intent);
    				break;
    			}
    			case 0x0d:
    			{
//    			
	                	//发送广播电压输出
	                	Intent intent = new Intent("com.skzh.iot.voloutput");
		    			intent.putExtra("vol1",(int)buffer[6]);
		    			intent.putExtra("vol2", (int)buffer[8]);
		    			intent.putExtra("vol3", (int)buffer[10]);
		    			intent.putExtra("vol4", (int)buffer[12]);
		    			sendBroadcast(intent);
    				}//end if
    				break;
    			}//end switch
    		}//end processData
    	}
    
    
    //处理心跳，判断是否连接上
    public void sendBeatBroadcast(int type)
    {
    	switch(type)
    	{
    		case 0x06:
    		{
    			Intent intent = new Intent("com.skzh.iot.engine");
    			intent.putExtra("beatheart", 1);
    			sendBroadcast(intent);
    			Intent intentlight = new Intent("com.skzh.iot.enginelight");
    			intentlight.putExtra("beatheart", 1);
    			sendBroadcast(intentlight);
    			break;
    		}
    		case 0x09:
    		{
    			Intent intent = new Intent("com.skzh.iot.pwm");
    			intent.putExtra("beatheart", 1);
    			sendBroadcast(intent);
    			break;
    		}
    		case 0x0a:
    		{
    			Intent intent = new Intent("com.skzh.iot.relay");//继电器
    			intent.putExtra("beatheart", 1);
    			sendBroadcast(intent);
    			break;
    		}
    		case 0x0d:
    		{
    			Intent intent = new Intent("com.skzh.iot.voloutput");
    			intent.putExtra("beatheart", 1);
    			sendBroadcast(intent);
    			break;
    		}
    		default:
    			break;
    			
    	}//end stiwtch
    }//end sendBeatBroadcast
    
    
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
				Log.v("skzh", "####################################");
				//关闭计时器
				if(timer!=null)
				{
					timer.cancel();
					timer = null;
				}
				//关闭串口
				if(mSerialApp!=null)
				{
					mSerialApp.closeSerialPort();
					mSerialApp = null;
					mOutputStream = null;
					mInputStream = null;
				}
				
				//关闭线程
				if(comread!=null)
				{
					comread.interrupt();
					comread = null;
				}
				//handlertest.removeCallbacks(runnable);
				super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
