package com.example.demo.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.R;
import com.example.demo.R.id;
import com.example.demo.R.layout;
import com.example.demo.R.menu;
import com.example.demo.adapter.ListAdapter;
import com.example.demo.adapter.OrderAdapter;
import com.example.demo.bean.Food;
import com.example.demo.bean.Order;
import com.example.demo.bean.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class PersonActivity extends BaseActivity {
	View headerView;
	ListView lv;
	OrderAdapter adapter;
	String phone;
	List<Order> list;
//	String name;
//	User user = new User();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		headerView = findViewById(R.id.headerview);
		setHeaderTitle(headerView, "��������");
		
		Intent intent = getIntent();
		phone = intent.getStringExtra("phone");
		Log.d("TAG", "person�õ���phone��ֵ��"+phone);
//		name = user.getUsername();
//		Log.d("TAG", "��user�еõ���name��"+name);
		lv = (ListView) findViewById(R.id.person_list);
		BmobQuery<Order> query = new BmobQuery<Order>();
		query.addWhereEqualTo("phone", phone);

		query.order("-createdAt");
		query.findObjects(this, new FindListener<Order>() {
			
			@Override
			public void onSuccess(List<Order> arg0) {
				Log.i("TAG", "��ѯ�ɹ���"+arg0);
				Order orders = new Order();
				list = new ArrayList<Order>();
				for (Order Order : arg0) {
					list.add(Order);
              }

				Log.i("TAG", "list�Ľ����"+list);

				adapter = new OrderAdapter(PersonActivity.this, list);
				lv.setAdapter(adapter);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.i("TAG", "��ѯʧ�ܣ�"+arg1);
				Toast.makeText(PersonActivity.this, "��ѯʧ�ܣ�"+arg1, 1).show();
			}
		});
	}
}
