package com.healthybear.dingcan.consumer.app;

import com.healthybear.dingcan.consumer.constant.Constant;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import android.app.Application;
import android.content.Context;

public class MyApp extends Application{

	//声明一个全局性的上下文对象
	//（注：确实在波不得已的情况使用）
	public static MyApp context;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = this;
		
		Bmob.DEBUG = true;
		
//		Bmob.getInstance(getApplicationContext()).init(Constant.BMOBKEY);
		
	}
}
