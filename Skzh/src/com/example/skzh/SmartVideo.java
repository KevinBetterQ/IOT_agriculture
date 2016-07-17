
package com.example.skzh;

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
	private Button btnForward;//�����ת��ť
	private Button btnStop;//���ֹͣ��ť
	private Button btnReverse;//�����ת��ť
	private ImageButton ibsetting;//��������ð�ť
	
	private ImageView ivEngineStatus;
	private EditText etAlarmphone;
	private LinearLayout dlglayout;
	SharedPreferences alarmsetsp;
	protected OutputStream mOutputStream;
	private boolean bFlgContrlcmd;
	SmarthomeRec smarthomeRec;
	byte cmd[] = {(byte)0x40, (byte)0x06, (byte)0x01, (byte)0x06, 
			(byte)0x0c,(byte)0x05};//���ݸ����ڵ�ָ��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smartvideo);
		
		bFlgContrlcmd = false;//����ָ���·��ı�ʶ
		alarmsetsp = getSharedPreferences("alarmphone", Activity.MODE_PRIVATE);
		
		btnForward = (Button)findViewById(R.id.smart_btnforward);
		btnStop = (Button)findViewById(R.id.smart_btnstop);
		btnReverse = (Button)findViewById(R.id.smart_btnback);
		btnForward.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnReverse.setOnClickListener(this);
		
		ivEngineStatus = (ImageView)findViewById(R.id.smarthomeengin);
		
		
		
		ibsetting = (ImageButton)findViewById(R.id.alarmsetting);
		ibsetting.setOnClickListener(this);
		
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
			cmd[4] = 0x0a;
		}else if(v==btnReverse){//����ָ���õ����ת
			cmd[4] = 0x0b;
		}else if(v==btnStop){//����ָ���õ��ֹͣ
			cmd[4] = 0x0c;
		}else if(v==ibsetting){//���ñ����ֻ���
			dlglayout = (LinearLayout)getLayoutInflater().inflate(R.layout.alarmset, null);
			etAlarmphone = (EditText)dlglayout.findViewById(R.id.etalarmset);
			new AlertDialog.Builder(this).setTitle("�����ֻ�������").setView(dlglayout).setPositiveButton("����",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							SharedPreferences.Editor editor = alarmsetsp.edit();
							editor.putString("phonenum", etAlarmphone.getText().toString());
							editor.commit();
							Toast.makeText(SmartVideo.this, "����ɹ�.", Toast.LENGTH_SHORT).show();
						}
					}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
						
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
		bFlgContrlcmd = true;//�·�����ָ���־
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
