package com.guo.demoserver.ui;

import java.util.ArrayList;
import java.util.List;

import com.guo.demoserver.R;
import com.guo.demoserver.R.id;
import com.guo.demoserver.R.layout;
import com.guo.demoserver.R.menu;
import com.guo.demoserver.adapter.CountAdapter;
import com.guo.demoserver.bean.Food;
import com.guo.demoserver.bean.Message;
import com.guo.demoserver.bean.Order;
import com.guo.demoserver.listener.OnLoadFoodNameFinishListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class PersonActivity extends Activity {
	List<Food> list;
	Food foods;
//	Message msg;
	ListView listView;
	CountAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		list = new ArrayList<Food>();
		foods = new Food();
		listView = (ListView) findViewById(R.id.CountListView);
		
		
		queryfoodname(new OnLoadFoodNameFinishListener() {
			

			

			@Override
			public void OnLoadFoodNameFinish(List<Food> list) {
				getCount(list);
			}

			
		});
	}


	protected void getCount(List<Food> list2) {
		int a = list2.size();
		BmobQuery<Order> query = new BmobQuery<Order>();
		for(int i=0;i<=a;i++){
			query.addWhereEqualTo("name", "list2.get(i)");
			query.count(PersonActivity.this, Order.class, new CountListener() {
				
				@Override
				public void onSuccess(int arg0) {
					Log.i("TAG", "订单的计数查询成功："+arg0);
					foods.setCount(arg0);
					list.add(foods);
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					Log.i("TAG", "订单的计数查询失败："+arg1);
					
				}
			});
		}
		
		adapter = new CountAdapter(this, list);
		listView.setAdapter(adapter);
		
	}


	private void queryfoodname(final OnLoadFoodNameFinishListener listener) {
		BmobQuery<Food> query = new BmobQuery<Food>();
		query.findObjects(this, new FindListener<Food>() {
			
			@Override
			public void onSuccess(List<Food> arg0) {
				Log.i("TAG", "person的查询成功："+arg0);
				for (Food Food : arg0) {
					
					foods.setName(Food.getName());
					list.add(foods);
              }
				listener.OnLoadFoodNameFinish(list);
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.i("TAG", "查询失败："+arg1);
				Toast.makeText(PersonActivity.this, "查询失败："+arg1, 1).show();
			}
		});
	}
	
	
	
	
}
