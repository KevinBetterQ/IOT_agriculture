
package com.example.skzh;

import com.example.skzh.SmartHomeActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmartVideo extends Activity implements OnClickListener{
	private Button btnForward;//电机正转按钮
	private Button btnStop;//电机停止按钮
	private Button btnReverse;//电机逆转按钮
	private ImageButton ibsetting;//界面的设置按钮
	
	//pwm
	private Button btnpwmadd;
	private Button btnpwmjj;
	int num=0;
	
	private ImageView ivEngineStatus;
	private EditText etAlarmphone;
	private LinearLayout dlglayout;
	SharedPreferences alarmsetsp;
	protected OutputStream mOutputStream;
	private boolean bFlgContrlcmd;
	SmarthomeRec smarthomeRec;
	byte cmd[] = {(byte)0x40, (byte)0x06, (byte)0x01, (byte)0x09, 
			(byte)0x09,(byte)0x05};//传递给串口的指令
	byte cmd2[] = {(byte)0x40, (byte)0x06, (byte)0x01, (byte)0x06, 
			(byte)0x09,(byte)0x05};//传递给串口的指令
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smartvideo);
		
		alarmsetsp = getSharedPreferences("alarmphone", Activity.MODE_PRIVATE);
		
		btnForward = (Button)findViewById(R.id.smart_btnforward);
		btnStop = (Button)findViewById(R.id.smart_btnstop);
		btnReverse = (Button)findViewById(R.id.smart_btnback);
		btnForward.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnReverse.setOnClickListener(this);
		ivEngineStatus = (ImageView)findViewById(R.id.smarthomeengin);
		
		btnpwmadd = (Button) findViewById(R.id.pwmadd);
		btnpwmjj = (Button) findViewById(R.id.pwmjj);
		btnpwmadd.setOnClickListener(this);
		btnpwmjj.setOnClickListener(this);
		
		
		ibsetting = (ImageButton)findViewById(R.id.alarmsetting);
		ibsetting.setOnClickListener(this);
		
		//创建广播接收器
		smarthomeRec = new SmarthomeRec();
		IntentFilter filter = new IntentFilter("com.skzh.iot.smarthome");
		registerReceiver(smarthomeRec, filter);//当意图过滤器和系统当前发送的广播吻合时，就会直接执行广播接受者，这里也就是smarthomeRec
		mOutputStream = MainActivity.mOutputStream;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnForward){//发送指令让电机正转
			cmd2[4] = 0x0a;
			bFlgContrlcmd = true;
			byte suma2=0;
		       for (int i = 0; i < cmd2.length-1; i++) 
		         {
		     	 suma2+= cmd2[i];
		         }
		        cmd2[cmd2.length-1] = suma2; 
		       
					try {
						mOutputStream.write(cmd2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}else if(v==btnReverse){//发送指令让电机逆转
			cmd2[4] = 0x0b;
			bFlgContrlcmd = true;
			byte suma2=0;
		       for (int i = 0; i < cmd2.length-1; i++) 
		         {
		     	 suma2+= cmd2[i];
		         }
		        cmd2[cmd2.length-1] = suma2; 
		       
					try {
						mOutputStream.write(cmd2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}else if(v==btnStop){//发送指令让电机停止
			cmd2[4] = 0x0c;
			bFlgContrlcmd = false;
			byte suma2=0;
		       for (int i = 0; i < cmd2.length-1; i++) 
		         {
		     	 suma2+= cmd2[i];
		         }
		        cmd2[cmd2.length-1] = suma2; 
		       
					try {
						mOutputStream.write(cmd2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}else if(v==ibsetting){//设置报警手机号
			dlglayout = (LinearLayout)getLayoutInflater().inflate(R.layout.alarmset, null);
			etAlarmphone = (EditText)dlglayout.findViewById(R.id.etalarmset);
			new AlertDialog.Builder(this).setTitle("报警手机号设置").setView(dlglayout).setPositiveButton("保存",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							SharedPreferences.Editor editor = alarmsetsp.edit();
							editor.putString("phonenum", etAlarmphone.getText().toString());
							editor.commit();
							Toast.makeText(SmartVideo.this, "保存成功.", Toast.LENGTH_SHORT).show();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							return;
						}
					}).show();
			String str = alarmsetsp.getString("phonenum", "");
			etAlarmphone.setText(str);
			return;
		}
		else if(v==btnpwmadd){
			if(num<90)
			{
				System.out.println(num);
				num=num+10;
				//textCommand.setText(num+"");
				if(num==0){
					cmd[4]= 0x00;
				}
				if(num==10){
					cmd[4]= 0x01;
				}
				if(num==20){
					cmd[4]= 0x02;
				}
				if(num==30){
					cmd[4]= 0x03;
				}
				if(num==40){
					cmd[4]= 0x04;
				}
				if(num==50){
					cmd[4]= 0x05;
				}
				if(num==60){
					cmd[4]= 0x06;
				}
				if(num==70){
					cmd[4]= 0x07;
				}
				if(num==80){
					cmd[4]= 0x08;
				}
				if(num==90){
					cmd[4]= 0x09;
				}
			}
		}
		else if(v==btnpwmjj){
			if(num>0)
			{
				System.out.println(num);
				num=num-10;
				//textCommand.setText(num+"");
				if(num==0){
					cmd[4]= 0x00;
				}
				if(num==10){
					cmd[4]= 0x01;
				}
				if(num==20){
					cmd[4]= 0x02;
				}
				if(num==30){
					cmd[4]= 0x03;
				}
				if(num==40){
					cmd[4]= 0x04;
				}
				if(num==50){
					cmd[4]= 0x05;
				}
				if(num==60){
					cmd[4]= 0x06;
				}
				if(num==70){
					cmd[4]= 0x07;
				}
				if(num==80){
					cmd[4]= 0x08;
				}
				if(num==90){
					cmd[4]= 0x09;
				}
			}
		}
		
        byte suma=0;
       for (int i = 0; i < cmd.length-1; i++) 
         {
     	 suma+= cmd[i];
         }
        cmd[cmd.length-1] = suma; 
       
			try {
				mOutputStream.write(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		

		
	}
	
	
	//自定义一个广播接收器
		class SmarthomeRec extends BroadcastReceiver//广播接收，进行界面显示
		{

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				int iType = intent.getIntExtra("type", 0);
				switch(iType)
				{
					
					case 6://电机
					{
						int engineStatus = intent.getIntExtra("engine", 0);
						Drawable image=null;
	            		if((engineStatus&0x08) == 0x08){//电机正传
	            			image = getResources().getDrawable(R.drawable.forward);
	            		}else if((engineStatus&0x04)==0x04){//电机反传
	            			image = getResources().getDrawable(R.drawable.for_back);
	            		}else if(((0xff&engineStatus)|0xf3)==0xf3){//电机停止
	            			image = getResources().getDrawable(R.drawable.stop);
	            		}
	            		//stopCommand.setBackgroundDrawable(image);
	            		//ivEngineStatus.setImageDrawable(image);
						break;
					}
					
					
					default:
						break;
					
				}
			}
		}
		
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
		}
}
