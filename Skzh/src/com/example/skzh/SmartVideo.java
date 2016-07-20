
package com.example.skzh;


import com.example.skzh.SmartHomeActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmartVideo extends Activity implements OnClickListener{
	private Button btnForward;//�����ת��ť
	private Button btnStop;//���ֹͣ��ť
	private Button btnReverse;//�����ת��ť
	
	//pwm
	private Button btnpwmadd;
	private Button btnpwmjj;
	public static int num=0;
	private boolean ispwm;//����ģʽpwm�����ж�
	
	//ģʽ
	private CheckBox ms;
	public static boolean isms;
	private ImageView ivEngineStatus;
	
	//����
	private EditText et_wendu;
	private EditText et_guang;
	//��edittext��ȡ��������ֵ
	public static int sz_wendu;
	public static double sz_guang;
	

	protected OutputStream mOutputStream;
	//private boolean bFlgContrlcmd;
	SmarthomeRec smarthomeRec;
	byte cmd[] = {(byte)0x40, (byte)0x06, (byte)0x01, (byte)0x09, 
			(byte)0x09,(byte)0x05};//���ݸ����ڵ�ָ��
	byte cmd2[] = {(byte)0x40, (byte)0x06, (byte)0x01, (byte)0x06, 
			(byte)0x09,(byte)0x05};//���ݸ����ڵ�ָ��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smartvideo);
		
		isms=false;
		
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
		
		et_wendu = (EditText) findViewById(R.id.et_wendu);
		et_guang = (EditText) findViewById(R.id.et_guang);
		sz_wendu = 30;
		sz_guang = 60.0;
		
		ms = (CheckBox) findViewById(R.id.checkBox);
		ms.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Toast.makeText(SmartVideo.this, "����ģʽ����", Toast.LENGTH_SHORT).show();
					isms = true;
					String ss = et_wendu.getText().toString();
					if("".equals(ss)){
						ss="30";
					}
					sz_wendu = Integer.parseInt(ss);
					
					String ww = et_guang.getText().toString();
					System.out.println(ww);
					if("".equals(ww)){
						sz_guang = 60.0;
					}else{
						sz_guang = Double.valueOf(ww);
					}
					
					
					if(isms) System.out.println("moshitrue");
				}else{
					Toast.makeText(SmartVideo.this, "����ģʽ�ر�", Toast.LENGTH_SHORT).show();
					isms = false;
				}
				
			}
		});
		
		
		
		
		//�����㲥������
		smarthomeRec = new SmarthomeRec();
		IntentFilter filter = new IntentFilter("com.skzh.iot.smarthome");
		registerReceiver(smarthomeRec, filter);//����ͼ��������ϵͳ��ǰ���͵Ĺ㲥�Ǻ�ʱ���ͻ�ֱ��ִ�й㲥�����ߣ�����Ҳ����smarthomeRec
		mOutputStream = MainActivity.mOutputStream;
	}
	
	
		

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnForward){//����ָ���õ����ת
			isms=false;
			cmd2[4] = 0x0a;
			SmartHomeActivity.bFlgContrlcmd = true;
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
					
					//��
					try {
						SmartHomeActivity.writer.write("kmotor");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}else if(v==btnReverse){//����ָ���õ����ת
			isms=false;
			cmd2[4] = 0x0b;
			SmartHomeActivity.bFlgContrlcmd = true;
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
					
					//��
					try {
						SmartHomeActivity.writer.write("kmotor");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}else if(v==btnStop){//����ָ���õ��ֹͣ
			isms=false;
			cmd2[4] = 0x0c;
			SmartHomeActivity.bFlgContrlcmd = false;
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
					
					//��
					try {
						SmartHomeActivity.writer.write("gmotor");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			//��Ӧ�������������ٷ�һ��
			 suma2=0;
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
							
						//��
					try {
								SmartHomeActivity.writer.write("gmotor");
								SmartHomeActivity.writer.flush();
						} catch (IOException e) {
								// TODO Auto-generated catch block
							e.printStackTrace();
					}
		}
		else if(v==btnpwmadd){
			isms=false;
			if(num<90)
			{
				num=num+10;
				//textCommand.setText(num+"");
				if(num==0){
					cmd[4]= 0x00;
				}
				if(num==10){
					cmd[4]= 0x01;
					SmartHomeActivity.bFlgpwm=true;
					//��
					try {
						SmartHomeActivity.writer.write("kpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==20){
					cmd[4]= 0x02;
					SmartHomeActivity.bFlgpwm=true;
					//��
					try {
						SmartHomeActivity.writer.write("kpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==30){
					cmd[4]= 0x03;
					SmartHomeActivity.bFlgpwm=true;
					//��
					try {
						SmartHomeActivity.writer.write("kpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==40){
					cmd[4]= 0x04;
					SmartHomeActivity.bFlgpwm=true;
					//��
					try {
						SmartHomeActivity.writer.write("kpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==50){
					cmd[4]= 0x05;
					SmartHomeActivity.bFlgpwm=true;
					//��
					try {
						SmartHomeActivity.writer.write("kpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==60){
					cmd[4]= 0x06;
					SmartHomeActivity.bFlgpwm=true;
					//��
					try {
						SmartHomeActivity.writer.write("kpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==70){
					cmd[4]= 0x07;
					SmartHomeActivity.bFlgpwm=true;
					//��
					try {
						SmartHomeActivity.writer.write("kpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==80){
					cmd[4]= 0x08;
					SmartHomeActivity.bFlgpwm=true;
					//��
					try {
						SmartHomeActivity.writer.write("kpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==90){
					cmd[4]= 0x09;
				}
			}
		}
		else if(v==btnpwmjj){
			isms=false;
			if(num>=-20)
			{
				System.out.println(num);
				num=num-10;
				//textCommand.setText(num+"");
				if(num<=0){
					cmd[4]= 0x00;
					SmartHomeActivity.bFlgpwm=false;
					//��
					try {
						SmartHomeActivity.writer.write("gpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==10){
					cmd[4]= 0x01;
					SmartHomeActivity.bFlgpwm=false;
					//��
					try {
						SmartHomeActivity.writer.write("gpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(num==20){
					cmd[4]= 0x02;
					SmartHomeActivity.bFlgpwm=false;
					//��
					try {
						SmartHomeActivity.writer.write("gpwm");
						SmartHomeActivity.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
			
		

		
	}//end onclick
	
	
	//�Զ���һ���㲥������
		class SmarthomeRec extends BroadcastReceiver//�㲥���գ����н�����ʾ
		{

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				int iType = intent.getIntExtra("type", 0);
				switch(iType)
				{
     				case 6://���
					{
						int engineStatus = intent.getIntExtra("engine", 0);
						Drawable image=null;
	            		if((engineStatus&0x08) == 0x08){//�������
	            			image = getResources().getDrawable(R.drawable.forward);
	            		}else if((engineStatus&0x04)==0x04){//�������
	            			image = getResources().getDrawable(R.drawable.for_back);
	            		}else if(((0xff&engineStatus)|0xf3)==0xf3){//���ֹͣ
	            			image = getResources().getDrawable(R.drawable.stop);
	            		}
	            		//stopCommand.setBackgroundDrawable(image);
	            		ivEngineStatus.setImageDrawable(image);
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
