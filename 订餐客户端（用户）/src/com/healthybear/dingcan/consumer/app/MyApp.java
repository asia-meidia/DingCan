package com.healthybear.dingcan.consumer.app;

import com.healthybear.dingcan.consumer.constant.Constant;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import android.app.Application;
import android.content.Context;

public class MyApp extends Application{

	//����һ��ȫ���Ե������Ķ���
	//��ע��ȷʵ�ڲ������ѵ����ʹ�ã�
	public static MyApp context;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = this;
		
		Bmob.DEBUG = true;
		
//		Bmob.getInstance(getApplicationContext()).init(Constant.BMOBKEY);
		
	}
}
