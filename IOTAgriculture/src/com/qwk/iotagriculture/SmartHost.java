/** Copyright 2013 shanke
 * All right reserved.
 * filename: SmartHost.java
 * todo: tabhost of the activitis SmartHomeActivity and SmartVideoActivity;
 * auther: jiangweijie
 * time: 2013 2013-12-18 上午11:10:29
 * version: V1.0
 *
 */
package com.qwk.iotagriculture;


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
	
	TabHost tabhost;
	TabSpec tsVideo;
	TabSpec tsHome;
	public static TabWidget tabget;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		
		tabhost = this.getTabHost();
		tabget=tabhost.getTabWidget();

		LayoutInflater.from(this).inflate(R.layout.tabhost, tabhost.getTabContentView(),true);
		
		
		
		
		
		/*tsHome = tabhost.newTabSpec("TS_HOME").setIndicator("")
				.setContent(new Intent(this,SmartHomeActivity.class));*/
		
		
		

		tabhost.addTab(tsHome);
		//tabget.getChildAt(0).getLayoutParams().height=53;
		//tabget.getChildAt(0).getLayoutParams().width=295;
		//tsVideo = tabhost.newTabSpec("TS_VIDEO").setIndicator("").setContent(new Intent(this,SmartVideo.class));//setIndicator("视频监控",getResources().getDrawable(R.drawable.tabvideo))
		tabhost.addTab(tsVideo);
		//tabget.getChildAt(1).getLayoutParams().height=53;
		//tabget.getChildAt(1).getLayoutParams().width=295;
		//tabget.set
		tabhost.getTabWidget().getChildAt(1).setBackgroundDrawable(getResources().getDrawable(R.drawable.videounfocus));
		tabhost.getTabWidget().getChildAt(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.homefocus));
		tabhost.setOnTabChangedListener(this);
		//tabhost.setCurrentTab(0);
		
	}
	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		//Log.v("skzh","clicked "+arg0);
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
