package com.guo.demoserver.ui;

import com.guo.demoserver.R;
import com.guo.demoserver.R.id;
import com.guo.demoserver.R.layout;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener {

	View headerView;
	private EditText etusername;
    private EditText etpassword;
    private Button login;
    private Button sign;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//�����AppLication ID д���Լ�������Ŀ�õ����Ǹ�AppLication ID
        Bmob.initialize(this, "830504d55b2c3873eae095cdf340c0ca");
        //ʹ�����ͷ���ʱ�ĳ�ʼ������
        BmobInstallation.getCurrentInstallation(this).save();
        // �������ͷ���
        BmobPush.startWork(this);
		
        initView();
	}
	private void initView() {
		headerView = findViewById(R.id.headerview);
		setHeaderTitle(headerView, "��¼����");
		etusername = (EditText) findViewById(R.id.et_username);
        etpassword = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
        case R.id.login:
            final String username = etusername.getText().toString();
            String password = etpassword.getText().toString();

            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                final BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(username);
                bmobUser.setPassword(password);

                bmobUser.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        //��ȡ����ǰ�û�����Ϣ
                        //��¼�ɹ�
                        Toast.makeText(LoginActivity.this, "��¼�ɹ�", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                    	Toast.makeText(LoginActivity.this, "��¼ʧ��"+s, Toast.LENGTH_SHORT).show();
                    }
                });

            }
            break;
        case R.id.sign:
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
            break;
    }
	}

}
