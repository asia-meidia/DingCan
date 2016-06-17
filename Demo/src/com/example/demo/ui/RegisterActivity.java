package com.example.demo.ui;

import java.io.File;

import com.example.demo.R;
import com.example.demo.R.id;
import com.example.demo.R.layout;
import com.example.demo.bean.User;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	View headerView;
	private EditText etusername;
    private EditText etpassword;
    private EditText etphone;
    private Button register;
    private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initialize();
	}

	private void initialize() {
		headerView = findViewById(R.id.headerview);
		setHeaderTitle(headerView, "注册界面");
		
		etusername = (EditText) findViewById(R.id.et_username);
        etpassword = (EditText) findViewById(R.id.et_password);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
        case R.id.register:
            registerData();
            break;
        case R.id.cancel:
        	finish();
            break;
    }
	}

	private void registerData() {
		final String name = etusername.getText().toString();
        final String password = etpassword.getText().toString();
                User user = new User();
                user.setUsername(name);
                user.setPassword(password);
                //注册(这个是已经提供好的接口)
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
                	etpassword.setText("");
                	Toast.makeText(this, "输入框内容不能为空", 1).show();
                	return;
                	
                }
                onRegister(user);
            
    }

	private void onRegister(User user) {
		user.signUp(RegisterActivity.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(RegisterActivity.this, "注册失败"+arg1, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
