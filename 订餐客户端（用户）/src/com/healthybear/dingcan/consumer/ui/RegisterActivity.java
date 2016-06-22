package com.healthybear.dingcan.consumer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

import com.healthybear.dingcan.consumer.R;
import com.healthybear.dingcan.consumer.bean.User;
import com.healthybear.dingcan.consumer.constant.Constant.Position;
import com.healthybear.dingcan.consumer.util.NetUtil;

public class RegisterActivity extends BaseActivity {

	View headerView;
	EditText etPhone;
	EditText etName;
	EditText etPassword;
	EditText etRePassword;
	Button button;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		initHeaderView();
		zhuce();
	}





	private void initView() {
		headerView = findViewById(R.id.headerview);
		etPhone = (EditText) findViewById(R.id.et_register_phone);
		etName = (EditText) findViewById(R.id.et_register_name);
		etPassword = (EditText) findViewById(R.id.et_register_password);
		etRePassword = (EditText) findViewById(R.id.et_register_password2);
		button = (Button) findViewById(R.id.btn_register);
	}


	private void initHeaderView() {
		setHeaderTitle(headerView, "注册用户");
		setHeaderImage(headerView, R.drawable.back_arrow_2, Position.LEFT, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	
	private void zhuce() {
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//1) 判空
				if(isEmpty(etPhone,etPassword,etRePassword)){
					return;
				}
				//2) 两次输入的密码是否一致
				if(!etPassword.getText().toString().equals(etRePassword.getText().toString())){
					toast("两次输入密码不一致，请重新输入");
					etRePassword.setText("");
					return;
				}
				//3) 判网
				if(!NetUtil.isNetworkAvailable(RegisterActivity.this)){
					toast("当前网络不给力");
					return;
				}
				//4) 注册用户
				User user = new User();
				user.setUsername(etName.getText().toString());
				user.setMobilePhoneNumber(etPhone.getText().toString());
				user.setPassword(etPassword.getText().toString());
				user.setInstallId(BmobInstallation.getInstallationId(RegisterActivity.this));
				
				user.signUp(RegisterActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						//注册成功
						//user用户已经成为当前设备的登录用户
						toast("注册成功");
						jumpTo(LoginActivity.class, true);
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(RegisterActivity.this, arg1, 1).show();
						switch (arg0) {
						//1602A本地班雷锋同学说，用户名重复注册失败时
						//     arg0返回的是202(亲测有效！)
						case 202:
							toast("用户名重复，请更换用户名");
							break;
						default:
							toastAndLog("注册失败，请重试", arg0+": "+arg1);
							break;
						}
					}
				});
				
			}
		});
	}
	

}
