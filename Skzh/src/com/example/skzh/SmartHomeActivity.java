
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

public class SmartHomeActivity extends Activity implements OnClickListener{
	private TextView tvTemp;
	private TextView tvHumi;
	private TextView tvCandela;
	private Button btnForward;//�����ת��ť
	private Button btnStop;//���ֹͣ��ť
	private Button btnReverse;//�����ת��ť
	private ImageButton ibsetting;//��������ð�ť
	private ImageView ivSomke;
	private ImageView ivHuman;
	private ImageView ivHumamPic;
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
		setContentView(R.layout.smarthome);
		bFlgContrlcmd = false;//����ָ���·��ı�ʶ
		alarmsetsp = getSharedPreferences("alarmphone", Activity.MODE_PRIVATE);
		
		tvTemp = (TextView)findViewById(R.id.smarthome_num1);
		tvHumi = (TextView)findViewById(R.id.smarthome_num2);
		tvCandela = (TextView)findViewById(R.id.smarthome_num3);
		ivSomke = (ImageView)findViewById(R.id.smarthome_smoke);
		ivHuman = (ImageView)findViewById(R.id.smarthome_reshidian);
		ivHumamPic = (ImageView)findViewById(R.id.smarthome_reshidianpic);
		ivEngineStatus = (ImageView)findViewById(R.id.smarthomeengin);
		btnForward = (Button)findViewById(R.id.smart_btnforward);
		btnStop = (Button)findViewById(R.id.smart_btnstop);
		btnReverse = (Button)findViewById(R.id.smart_btnback);
		btnForward.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnReverse.setOnClickListener(this);
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
							Toast.makeText(SmartHomeActivity.this, "����ɹ�.", Toast.LENGTH_SHORT).show();
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
				case 2://��ʪ�ȹ���
				{
        			tvTemp.setText(String.valueOf(intent.getIntExtra("temp", 0)));
        			tvHumi.setText(String.valueOf(intent.getIntExtra("humi", 0)));//�ַ���ֵ
        			double dGuangqiang = intent.getDoubleExtra("light", 0.0);
             	    DecimalFormat df = new DecimalFormat("0.0");
            	    tvCandela.setText(String.valueOf(df.format(dGuangqiang)));
					break;
				}
				case 4://����
				{
					int isSmoke = intent.getIntExtra("smoke", 0);
					Drawable image1=null;
            		if(isSmoke==(byte)0x01){
            			image1 = getResources().getDrawable(R.drawable.hassmoke);                    			
            		}else if(isSmoke==(byte)0x00){
            			image1 = getResources().getDrawable(R.drawable.nosmoke);
            		}
    				ivSomke.setImageDrawable(image1);
					break;
				}
				case 5://����
				{
					int isdoppler = intent.getIntExtra("doppler", 0);
					Drawable image=null;
            		if(isdoppler==(byte)0x01){
            			SmsManager manager_sms = SmsManager.getDefault();
            			String str = alarmsetsp.getString("phonenum", "");
            			/*if(!str.isEmpty())//�����˱����ֻ��ŷ����ͱ�������
            			{
            				manager_sms.sendTextMessage(str, null, "��⵽�������֣���鿴��أ�", null, null);
            			}*/
            			image = getResources().getDrawable(R.drawable.hasone); 
            			ivHumamPic.setImageDrawable(getResources().getDrawable(R.drawable.somebody));
            		}else if(isdoppler==(byte)0x00){
            			image = getResources().getDrawable(R.drawable.noone);
            			ivHumamPic.setImageDrawable(getResources().getDrawable(R.drawable.nobody));
            		}
            		ivHuman.setImageDrawable(image);
					break;
				}
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
		
		super.onDestroy();
	}
}
