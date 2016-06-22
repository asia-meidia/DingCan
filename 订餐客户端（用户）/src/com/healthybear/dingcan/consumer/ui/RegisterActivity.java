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
		setHeaderTitle(headerView, "ע���û�");
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
				//1) �п�
				if(isEmpty(etPhone,etPassword,etRePassword)){
					return;
				}
				//2) ��������������Ƿ�һ��
				if(!etPassword.getText().toString().equals(etRePassword.getText().toString())){
					toast("�����������벻һ�£�����������");
					etRePassword.setText("");
					return;
				}
				//3) ����
				if(!NetUtil.isNetworkAvailable(RegisterActivity.this)){
					toast("��ǰ���粻����");
					return;
				}
				//4) ע���û�
				User user = new User();
				user.setUsername(etName.getText().toString());
				user.setMobilePhoneNumber(etPhone.getText().toString());
				user.setPassword(etPassword.getText().toString());
				user.setInstallId(BmobInstallation.getInstallationId(RegisterActivity.this));
				
				user.signUp(RegisterActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						//ע��ɹ�
						//user�û��Ѿ���Ϊ��ǰ�豸�ĵ�¼�û�
						toast("ע��ɹ�");
						jumpTo(LoginActivity.class, true);
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(RegisterActivity.this, arg1, 1).show();
						switch (arg0) {
						//1602A���ذ��׷�ͬѧ˵���û����ظ�ע��ʧ��ʱ
						//     arg0���ص���202(�ײ���Ч��)
						case 202:
							toast("�û����ظ���������û���");
							break;
						default:
							toastAndLog("ע��ʧ�ܣ�������", arg0+": "+arg1);
							break;
						}
					}
				});
				
			}
		});
	}
	

}
