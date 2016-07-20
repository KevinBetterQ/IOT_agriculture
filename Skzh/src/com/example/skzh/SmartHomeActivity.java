
package com.example.skzh;

import com.example.skzh.SmartVideo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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
import android.view.ViewDebug.FlagToString;
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
	
	//与smartvideo共享的变量值
	public static int sh_wen;
	public static int sh_shi;
	public static double sh_guang;
	
	
	private ImageView ivSomke;
	private ImageView ivHuman;
	private ImageView ivHumamPic;
	private ImageView ivEngineStatus;
	
	private EditText etAlarmphone;
	private LinearLayout dlglayout;
	SharedPreferences alarmsetsp;
	protected OutputStream mOutputStream;
	public static boolean bFlgContrlcmd;//电机开启标志
	public static boolean bFlgpwm;//pwm开启标志
	SmarthomeRec smarthomeRec;
	byte cmd[] = {(byte)0x40, (byte)0x06, (byte)0x01, (byte)0x06, 
			(byte)0x0c,(byte)0x05};//传递给串口的指令 电机
	byte cmd2[] = {(byte)0x40, (byte)0x06, (byte)0x01, (byte)0x09, 
			(byte)0x09,(byte)0x05};//传递给串口的指令 pwm
	
	Socket socket = null;
	public static BufferedWriter writer = null;
    BufferedReader reader = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smarthome);
		bFlgContrlcmd = false;//控制电机指令下发的标识
		bFlgpwm = false;//pwm控制下发指令
		alarmsetsp = getSharedPreferences("alarmphone", Activity.MODE_PRIVATE);
		
		tvTemp = (TextView)findViewById(R.id.smarthome_num1);
		tvHumi = (TextView)findViewById(R.id.smarthome_num2);
		tvCandela = (TextView)findViewById(R.id.smarthome_num3);
		ivSomke = (ImageView)findViewById(R.id.smarthome_smoketext);
		ivHuman = (ImageView)findViewById(R.id.smarthome_reshidian);
		//ivHumamPic = (ImageView)findViewById(R.id.smarthome_reshidianpic);
		ivEngineStatus = (ImageView)findViewById(R.id.smarthomeengin);
		
		
		
		
		//创建广播接收器
		smarthomeRec = new SmarthomeRec();
		IntentFilter filter = new IntentFilter("com.skzh.iot.smarthome");
		registerReceiver(smarthomeRec, filter);//当意图过滤器和系统当前发送的广播吻合时，就会直接执行广播接受者，这里也就是smarthomeRec
		mOutputStream = MainActivity.mOutputStream;
		
		//连接socket
		connect();
		
		
	}
	
	//开了一个线程进行socket连接
	private void connect() {
		
		AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				
					try {
						
						socket = new Socket("182.254.130.103", 2348);
						writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						publishProgress("connect success");
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(SmartHomeActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				
				
				//读取socket数据
                try {
                	String line;
					while ((line = reader.readLine())!= null) {
					    publishProgress(line);
					    System.out.println(line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
			
			@Override
			protected void onProgressUpdate(String... values) {
				//System.out.println("test");
				
				//智能模式的判断
				if(SmartVideo.isms == true){
					//智能温度
					if(sh_wen >= SmartVideo.sz_wendu){
						
						if(bFlgContrlcmd == false){
							System.out.println("zidongkaidianji");
							cmd[4] = 0x0a;
							//下发控制指令标志
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
								
							bFlgContrlcmd = true;
							 //socket发送
		            	    try {
								writer.write("kmotor");
								writer.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					//自动光照
					if(sh_guang <= SmartVideo.sz_guang){
						
						//判断pwm是否是关的
						if(bFlgpwm == false){
							System.out.println("kgkgkgkgkgkgkgkg");
							cmd2[4] = 0x08;
							//下发控制指令标志
					        byte suma=0;
					        for (int i = 0; i < cmd2.length-1; i++) 
					         {
					     	 suma+= cmd2[i];
					         }
					        cmd2[cmd2.length-1] = suma; 
					       
								try {
									mOutputStream.write(cmd2);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							bFlgpwm = true;
							 //socket发送
		            	    try {
								writer.write("kpwm");
								writer.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}					
					}else{
						//判断pwm是否是开的
						if(bFlgpwm == true){
							cmd2[4] = 0x00;
							//下发控制指令标志
					        byte suma=0;
					        for (int i = 0; i < cmd2.length-1; i++) 
					         {
					     	 suma+= cmd2[i];
					         }
					        cmd2[cmd2.length-1] = suma; 
					       
								try {
									mOutputStream.write(cmd2);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							bFlgpwm = false;
							 //socket发送
		            	    try {
								writer.write("gpwm");
								writer.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}				
					}
				}//模式之中
				
				
				
				if (values[0].equals("connect success")) {
                    Toast.makeText(SmartHomeActivity.this, "连接服务器成功", Toast.LENGTH_SHORT).show();

                }
				else if (values[0].equals("dianjikai")) {
					
					//判断电机是否是关的
					if(bFlgContrlcmd == false){
						cmd[4] = 0x0a;
						//下发控制指令标志
				        byte suma=0;
				        for (int i = 0; i < cmd.length-1; i++) 
				         {
				     	 suma+= cmd[i];
				         }
				        cmd[cmd.length-1] = suma; 
				       
							try {
								System.out.println("kkkkkkkkkkk\n");
								mOutputStream.write(cmd);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						bFlgContrlcmd = true;
					}
					
                }
				else if (values[0].equals("dianjiguan")) {
					
					//判断电机是否是开的
					if(bFlgContrlcmd == true){
						cmd[4] = 0x0c;
						//下发控制指令标志
				        byte suma=0;
				        for (int i = 0; i < cmd.length-1; i++) 
				         {
				     	 suma+= cmd[i];
				         }
				        cmd[cmd.length-1] = suma; 
				       
							try {
								System.out.println("gggggggggggggg\n");
								mOutputStream.write(cmd);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						bFlgContrlcmd = false;
					}					
                }
				else if (values[0].equals("pwmkai")) {
					
					//判断pwm是否是开的
					if(bFlgpwm == false){
						cmd2[4] = 0x06;
						//下发控制指令标志
				        byte suma=0;
				        for (int i = 0; i < cmd2.length-1; i++) 
				         {
				     	 suma+= cmd2[i];
				         }
				        cmd2[cmd2.length-1] = suma; 
				       
							try {
								System.out.println("pwmkkkkk\n");
								mOutputStream.write(cmd2);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						bFlgpwm = true;
					    SmartVideo.num=30;
					}					
                }
				else if (values[0].equals("pwmguan")) {
					
					//判断pwm是否是开的
					if(bFlgpwm == true){
						cmd2[4] = 0x00;
						//下发控制指令标志
				        byte suma=0;
				        for (int i = 0; i < cmd2.length-1; i++) 
				         {
				     	 suma+= cmd2[i];
				         }
				        cmd2[cmd2.length-1] = suma; 
				       
							try {
								System.out.println("pwmggggggg\n");
								mOutputStream.write(cmd2);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						bFlgpwm = false;
						SmartVideo.num=0;
					}					
                }
				
				super.onProgressUpdate(values);
			}
			
		};
		 read.execute();
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("test");
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
				case 2://温湿度光照
				{
					sh_wen = intent.getIntExtra("temp", 0);
					sh_shi = intent.getIntExtra("humi", 0);
					String wen = String.valueOf(intent.getIntExtra("temp", 0));
					String shi = String.valueOf(intent.getIntExtra("humi", 0));
        			tvTemp.setText(wen);
        			tvHumi.setText(shi);//字符串值
        			
        			//光照
        			sh_guang = intent.getDoubleExtra("light", 0.0);
        			double dGuangqiang = intent.getDoubleExtra("light", 0.0);
             	    DecimalFormat df = new DecimalFormat("0.0");
             	    String guang = String.valueOf(df.format(dGuangqiang));
            	    tvCandela.setText(guang);
            	    
            	  
        			
            	    //socket发送
            	    try {
						writer.write(wen+"/");
						writer.write(shi+"/");
						writer.write(guang+"\n");
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	    
       			
					break;
				}
				case 4://烟雾
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
				case 5://红外
				{
					int isdoppler = intent.getIntExtra("doppler", 0);
					String people=null;
					Drawable image=null;
            		if(isdoppler==(byte)0x01){
            			SmsManager manager_sms = SmsManager.getDefault();
            			String str = alarmsetsp.getString("phonenum", "");
            			people="youren";
            			image = getResources().getDrawable(R.drawable.hasone); 
            			
            		}else if(isdoppler==(byte)0x00){
            			image = getResources().getDrawable(R.drawable.noone);
            			people="meiren";
            			
            		}
            		ivHuman.setImageDrawable(image);
            		
            		//socket发送
            	    try {
						writer.write(people);
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		
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
