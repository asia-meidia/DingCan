package com.example.demo.ui;

import cn.bmob.push.BmobPush;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.demo.R;
import com.example.demo.R.id;
import com.example.demo.R.layout;
import com.example.demo.adapter.ListAdapter;
import com.example.demo.bean.Bread;
import com.example.demo.bean.Food;
import com.example.demo.listener.OnLoadImageFinishListener;
import com.example.demo.util.ImageLoader;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity  {
	View headerView;
	ListView listView;
	ImageView iv;
	
	ListAdapter adapter;
	
	
//	List<Bread> list;
	List<Food> list;
	String a;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BmobInstallation instal = new BmobInstallation(getApplication());
		a = instal.getInstallationId();
		Log.d("TAG", "installationId"+a);
		
		
		//这里的AppLication ID 写上自己创建项目得到的那个AppLication ID
        Bmob.initialize(this, "830504d55b2c3873eae095cdf340c0ca");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);
		
		iv =(ImageView) findViewById(R.id.iv_headerview_right);
		headerView = findViewById(R.id.headerview);
		setHeaderTitle(headerView, "菜单列表");
		
		setHeaderImage(headerView, R.drawable.mycenter, Position.RIGHT, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,PersonActivity.class));
			}
		});
		
		
		listView = (ListView) findViewById(R.id.foodList);
//		query(new OnLoadImageFinishListener() {
//			
//			@Override
//			public void OnLoadImageFinish() {
//				// TODO Auto-generated method stub
//				
//				adapter = new ListAdapter(this, list);
//				listView.setAdapter(adapter);
//			}
//		}); 
		query();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//弹出一个界面，显示被点击的菜单的详细的信息
				//被点击的餐单取出来
				Food foods = adapter.getItem(position);//从adapter中得到联系人的信息
				showDetail(foods,position);
				
				
			}
		});
	}
	/********自定义一个对话框
	 * @param position **********/
	protected void showDetail(Food foods, int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		dialog.show();
		Window window = dialog.getWindow();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.detail_layout, null);
		window.setContentView(view);
		
		ImageView iv = (ImageView) view.findViewById(R.id.detail_img);
		final TextView tv1 = (TextView) view.findViewById(R.id.detail_name);
		final TextView tv2 = (TextView) view.findViewById(R.id.detail_price);
		TextView tv3 = (TextView) view.findViewById(R.id.detail_msg);
		final TextView tv4 = (TextView) view.findViewById(R.id.detail_phonecall);
		Button btn1 = (Button) view.findViewById(R.id.detail_btn);
		Button btn2 = (Button) view.findViewById(R.id.detail_cancel);
		Button btn3 = (Button) view.findViewById(R.id.call);
		
		ImageLoader.loadImage(foods.getFoodpic().getFileUrl(this), iv);
		tv1.setText("菜名："+foods.getFoodname());
		tv2.setText("价格："+foods.getFoodprice());
		tv3.setText("详细信息："+foods.getFooddetailed());
		
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,BuyActivity.class);
				intent.putExtra("name", tv1.getText());
				intent.putExtra("price", tv2.getText());
				intent.putExtra("instal", a);
				
				startActivity(intent);
				dialog.dismiss();
			}
		});
		
		btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv4.getText().toString()));
				startActivity(intentPhone);
			}
		});
	}

	
	/***********查询数据*******************/
	private void query() {

		
		BmobQuery<Food> query = new BmobQuery<Food>();
		query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
		//判断是否有缓存，
		boolean isCache = query.hasCachedResult(this,Food.class);
		if(isCache){
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，先从缓存读取数据，如果没有，再从网络获取。
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，先从网络读取数据，如果没有，再从缓存中获取。
		}
		
//		BmobQuery.clearAllCachedResults(this);
		
		query.findObjects(this, new FindListener<Food>() {

			@Override
			public void onError(int arg0, String object) {
				Log.i("TAG", "查询失败："+object);
				Toast.makeText(MainActivity.this, "查询失败："+object, 1).show();
			}

			@Override
			public void onSuccess(List<Food> object) {
				Log.i("TAG", "查询成功："+object);
				//Toast.makeText(MainActivity.this, "查询成功："+object, 1).show();
//				Bread bread = new Bread();
//				list = new ArrayList<Bread>();
				Food foods = new Food();
				list = new ArrayList<Food>();
				for (Food Food : object) {  
					
//                    //获得菜的名字
//					foods.setFoodname(Food.getFoodname());
//					//获得菜的价格
//					foods.setFoodprice(Food.getFoodprice());
//					
//					foods.setFoodpic(Food.getFoodpic());
//				
					list.add(Food);
					
					/*bread.setName(Food.getFoodname());
					bread.setPrice(Food.getFoodprice());
					bread.setPic(Food.getFoodpic().getUrl());
					list.add(bread);*/
                }
			
				Log.i("TAG", "list的结果："+list);
				
				adapter = new ListAdapter(MainActivity.this, list);
				listView.setAdapter(adapter);
			}
		});
	}



}
