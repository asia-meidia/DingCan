package com.healthybear.dingcan.consumer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;





import cn.bmob.v3.Bmob;

import com.healthybear.dingcan.consumer.R;
import com.healthybear.dingcan.consumer.bean.User;
import com.healthybear.dingcan.consumer.constant.Constant;


public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Bmob.initialize(this, Constant.BMOBKEY);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
				boolean isFirst = sp.getBoolean("isFirst", true);
				if (isFirst) {
					Editor edit = sp.edit();
					edit.putBoolean("isFirst", false);
					edit.commit();
					Intent intent = new Intent();
					intent.setClass(SplashActivity.this, UserGuideActivity.class);
					startActivity(intent);
				} else {
						
						Intent intent = new Intent();
						intent.setClass(SplashActivity.this, LoginActivity.class);
						startActivity(intent);
					
				}
				finish();
			}
		}, 3000);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
