package com.healthybear.dingcan.consumer.ui;

import cn.bmob.v3.listener.SaveListener;

import com.dd.CircularProgressButton;
import com.healthybear.dingcan.consumer.R;
import com.healthybear.dingcan.consumer.bean.User;
import com.healthybear.dingcan.consumer.constant.Constant.Position;
import com.healthybear.dingcan.consumer.util.NetUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class LoginActivity extends BaseActivity {

	TextView tv_toregister;
	
	View headerView;
	
	EditText etPhone;
	EditText etPassword;
	
	CircularProgressButton button;
	
	Handler handler;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
		
		initHeaderView();
		handler = new Handler();
		tv_toregister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
			}
		});
		
		
		
	}
	
	

	private void initView() {
		tv_toregister = (TextView) findViewById(R.id.tv_login);
		headerView = findViewById(R.id.headerview);
		etPhone = (EditText) findViewById(R.id.et_login_userphone);
		etPassword = (EditText) findViewById(R.id.et_login_password);
		button = (CircularProgressButton) findViewById(R.id.btn_login_login);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//1)ÅÐ¿Õ
				if(isEmpty(etPhone,etPassword)){
					return;
				}
				//2)ÅÐÍø
				if(!NetUtil.isNetworkAvailable(LoginActivity.this)){
					toast("ÍøÂç²»¸øÁ¦");
					return;
				}
				//3)µÇÂ¼
				User user = new User();
				user.setMobilePhoneNumber(etPhone.getText().toString());
				user.setPassword(etPassword.getText().toString());
				button.setIndeterminateProgressMode(true);
				button.setProgress(50);
				user.login(LoginActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						button.setProgress(100);
						jumpTo(MainActivity.class, true);
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						button.setProgress(0);
						switch (arg0) {
						case 101:
							toast("ÓÃ»§Ãû»òÃÜÂë´íÎó£¡");
							break;

						default:
							toastAndLog("µÇÂ¼Ê§°Ü£¬ÇëÖØÊÔ", arg0+": "+arg1);
							break;
						}
						
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								button.setProgress(0);
							}
						}, 1500);
					}
				});
			}
		});
	}

	private void initHeaderView() {
		setHeaderTitle(headerView, "µÇÂ¼½çÃæ",Position.CENTER);
	}
	
	
	
	

}
