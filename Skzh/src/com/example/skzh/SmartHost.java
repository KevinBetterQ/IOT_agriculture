
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
	
	TabHost tabhost;//相当于Tab的集合
	TabSpec tsVideo;//相当于每个Tab中的分页面，这是页面一，本程序定为 农场环境
	TabSpec tsHome;//这是页面二，本程序定为 控制管理
	public static TabWidget tabget;//在TabHost中一般必须有TabWidget，这个主要是用来处理tab的位置、属性等
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		
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
		tabhost.getTabWidget().getChildAt(1).setBackgroundDrawable(getResources().getDrawable(R.drawable.videounfocus));//用图片放在Tab位置
		tabhost.getTabWidget().getChildAt(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.homefocus));
		tabhost.setOnTabChangedListener(this);
		//tabhost.setCurrentTab(0);
		
	}
	@Override
	public void onTabChanged(String arg0) {//本监听里实现的仅为将Tab图片变化
		// TODO Auto-generated method stub
		
		if(arg0.equals("TS_VIDEO")){
			//tsVideo.setIndicator("视频监控",getResources().getDrawable(R.drawable.tabvideo));
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
