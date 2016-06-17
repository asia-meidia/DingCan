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
		
		
		//�����AppLication ID д���Լ�������Ŀ�õ����Ǹ�AppLication ID
        Bmob.initialize(this, "830504d55b2c3873eae095cdf340c0ca");
        // ʹ�����ͷ���ʱ�ĳ�ʼ������
        BmobInstallation.getCurrentInstallation(this).save();
        // �������ͷ���
        BmobPush.startWork(this);
		
		iv =(ImageView) findViewById(R.id.iv_headerview_right);
		headerView = findViewById(R.id.headerview);
		setHeaderTitle(headerView, "�˵��б�");
		
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
				//����һ�����棬��ʾ������Ĳ˵�����ϸ����Ϣ
				//������Ĳ͵�ȡ����
				Food foods = adapter.getItem(position);//��adapter�еõ���ϵ�˵���Ϣ
				showDetail(foods,position);
				
				
			}
		});
	}
	/********�Զ���һ���Ի���
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
		tv1.setText("������"+foods.getFoodname());
		tv2.setText("�۸�"+foods.getFoodprice());
		tv3.setText("��ϸ��Ϣ��"+foods.getFooddetailed());
		
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

	
	/***********��ѯ����*******************/
	private void query() {

		
		BmobQuery<Food> query = new BmobQuery<Food>();
		query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//�˱�ʾ����һ��
		//�ж��Ƿ��л��棬
		boolean isCache = query.hasCachedResult(this,Food.class);
		if(isCache){
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // ����л���Ļ����ȴӻ����ȡ���ݣ����û�У��ٴ������ȡ��
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // ���û�л���Ļ����ȴ������ȡ���ݣ����û�У��ٴӻ����л�ȡ��
		}
		
//		BmobQuery.clearAllCachedResults(this);
		
		query.findObjects(this, new FindListener<Food>() {

			@Override
			public void onError(int arg0, String object) {
				Log.i("TAG", "��ѯʧ�ܣ�"+object);
				Toast.makeText(MainActivity.this, "��ѯʧ�ܣ�"+object, 1).show();
			}

			@Override
			public void onSuccess(List<Food> object) {
				Log.i("TAG", "��ѯ�ɹ���"+object);
				//Toast.makeText(MainActivity.this, "��ѯ�ɹ���"+object, 1).show();
//				Bread bread = new Bread();
//				list = new ArrayList<Bread>();
				Food foods = new Food();
				list = new ArrayList<Food>();
				for (Food Food : object) {  
					
//                    //��ò˵�����
//					foods.setFoodname(Food.getFoodname());
//					//��ò˵ļ۸�
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
			
				Log.i("TAG", "list�Ľ����"+list);
				
				adapter = new ListAdapter(MainActivity.this, list);
				listView.setAdapter(adapter);
			}
		});
	}



}
