package com.example.demo.ui;

import com.example.demo.R;
import com.example.demo.R.id;
import com.example.demo.R.layout;
import com.example.demo.R.menu;
import com.example.demo.bean.Feedback;
import com.example.demo.bean.Order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;

public class BuyActivity extends BaseActivity {
	View headerView;
	EditText et1,et2,et3,et4;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		
		headerView = findViewById(R.id.headerview);
		setHeaderTitle(headerView, "�¶���");
		
		initView();
		
	}


	private void initView() {
		et1 = (EditText) findViewById(R.id.buy_etfoodname);
		et2 = (EditText) findViewById(R.id.buy_etfoodprice);
		et3 = (EditText) findViewById(R.id.buy_etphone);
		et4 = (EditText) findViewById(R.id.buy_etaddress);
		btn = (Button) findViewById(R.id.buy_btn);
		
		Intent intent = getIntent();
		final String s1 = intent.getStringExtra("name");
		final String s2 = intent.getStringExtra("price");
		final String s3 = intent.getStringExtra("instal");
		et1.setText(s1);
		et2.setText(s2);
		
		btn.setOnClickListener(new OnClickListener() {
			
			/************�õ����ݲ������п�************/
			@Override
			public void onClick(View v) {
				String phone = et3.getText().toString();
				String address = et4.getText().toString();
				if(TextUtils.isEmpty(phone)||TextUtils.isEmpty(address)||TextUtils.isEmpty(s1)||TextUtils.isEmpty(s2)){
					Toast.makeText(BuyActivity.this, "���������ݲ���Ϊ��", 1).show();
					return;
				}
				final Order order = new Order();
				order.setName(s1);
				order.setPrice(s2);
				order.setPhone(phone);
				order.setAddress(address);
				order.setInstal(s3);
				
				String msg = order.toString();
				
				order.save(BuyActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						Log.d("TAG", "��������order��"+"ʱ���ǣ�"+order.getCreatedAt());
						Toast.makeText(BuyActivity.this, "��������order��"+"ʱ���ǣ�"+order.getCreatedAt(), 1).show();
						Intent intent = new Intent(BuyActivity.this,PersonActivity.class);
						intent.putExtra("phone", order.getPhone());
						startActivity(intent);
						Log.d("TAG", "�����ת���������Ĵ����phone��ֵ��"+order.getPhone());
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Log.d("TAG", "����ʧ���ˣ�ԭ���ǣ�"+arg1);
						Toast.makeText(BuyActivity.this, "����ʧ���ˣ�ԭ���ǣ�"+arg1, 1).show();
					}
				});
				
				
				
				
				
				
//				sendFeedbackMsg(msg);
				
			}
		});
	}


	/**
	 * ���ͷ�����Ϣ��������
	 * @param msg ������Ϣ
	 */
	protected void sendFeedbackMsg(String msg) {
		BmobPushManager bmobPush = new BmobPushManager(this);
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("isDeveloper", true);
		bmobPush.setQuery(query);
		bmobPush.pushMessage(msg);
		
		saveFeedbackMsg(msg);
	}


	/**
	 * ���淴����Ϣ��������
	 * @param msg ������Ϣ
	 */
	protected void saveFeedbackMsg(String msg) {
		Feedback feedback = new Feedback();
		 feedback.setContent(msg);
		 feedback.save(this, new SaveListener() {
			
			
			@Override
			public void onSuccess() {
				Log.i("TAG", "������Ϣ�ѱ��浽������");
                //����������Ϣ
                Toast.makeText(BuyActivity.this, "������Ϣ�ѱ��浽������", 1).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				 // TODO Auto-generated method stub
                Log.i("TAG", "���淴����Ϣʧ�ܣ�"+arg1);
                Toast.makeText(BuyActivity.this, "���淴����Ϣʧ�ܣ�"+arg1, 1).show();
			}
		});
	}
	
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
