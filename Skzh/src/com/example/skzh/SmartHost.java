
package com.example.skzh;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

public class SmartHost extends TabActivity implements OnTabChangeListener{
	
	TabHost tabhost;//�൱��Tab�ļ���
	TabSpec tsVideo;//�൱��ÿ��Tab�еķ�ҳ�棬����ҳ��һ��������Ϊ ũ������
	TabSpec tsHome;//����ҳ�����������Ϊ ���ƹ���
	public static TabWidget tabget;//��TabHost��һ�������TabWidget�������Ҫ����������tab��λ�á����Ե�
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		
		tabhost = this.getTabHost();
		tabget=tabhost.getTabWidget();
		LayoutInflater.from(this).inflate(R.layout.tabhost, tabhost.getTabContentView(),true);
		
		tsHome = tabhost.newTabSpec("TS_HOME").setIndicator("")
				.setContent(new Intent(this,SmartHomeActivity.class));
		tabhost.addTab(tsHome);
		
		//tabget.getChildAt(0).getLayoutParams().height=53;
		//tabget.getChildAt(0).getLayoutParams().width=295;
		
		tsVideo = tabhost.newTabSpec("TS_VIDEO").setIndicator("")
				.setContent(new Intent(this,SmartVideo.class));
		tabhost.addTab(tsVideo);
		
		//tabget.getChildAt(1).getLayoutParams().height=53;
		//tabget.getChildAt(1).getLayoutParams().width=295;
		//tabget.set
		tabhost.getTabWidget().getChildAt(1).setBackgroundDrawable(getResources().getDrawable(R.drawable.videounfocus));//��ͼƬ����Tabλ��
		tabhost.getTabWidget().getChildAt(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.homefocus));
		tabhost.setOnTabChangedListener(this);
		//tabhost.setCurrentTab(0);
		
	}
	@Override
	public void onTabChanged(String arg0) {//��������ʵ�ֵĽ�Ϊ��TabͼƬ�仯
		// TODO Auto-generated method stub
		
		if(arg0.equals("TS_VIDEO")){
			//tsVideo.setIndicator("��Ƶ���",getResources().getDrawable(R.drawable.tabvideo));
			//tabget.getChildAt(1).setBackground(getResources().getDrawable(R.drawable.tabvideo));
			tabhost.getTabWidget().getChildAt(1).setBackgroundDrawable(getResources().getDrawable(R.drawable.videofocus));
			tabhost.getTabWidget().getChildAt(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.homeunfocus));
			//tsHome.setIndicator("",null);
		}else if(arg0.equals("TS_HOME")){
			tabhost.getTabWidget().getChildAt(1).setBackgroundDrawable(getResources().getDrawable(R.drawable.videounfocus));
			tabhost.getTabWidget().getChildAt(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.homefocus));
		}
	}
}
