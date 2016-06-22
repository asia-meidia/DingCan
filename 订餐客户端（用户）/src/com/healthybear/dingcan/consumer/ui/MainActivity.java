package com.healthybear.dingcan.consumer.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.healthybear.dingcan.consumer.R;
import com.healthybear.dingcan.consumer.R.layout;
import com.healthybear.dingcan.consumer.R.menu;
import com.healthybear.dingcan.consumer.adapter.FoodAdapter;
import com.healthybear.dingcan.consumer.bean.Food;

import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	View headerView;
	
	PullToRefreshListView ptrListView;
	ListView listView;
	
	private TextView tvTotalPrice;		//结算的价格
	private ImageView ivDelete;			//删除
	private Button btnCommitOrder;		//结算
	private LinearLayout ll_layout;		//结算布局
	
	private FoodAdapter adapter;		//自定义适配器
	private List<Food> list;		//购物车数据集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHeaderView();
        initView();
        initListView();
    }

	private void initHeaderView() {
		headerView = findViewById(R.id.headerview);
		setHeaderTitle(headerView, "菜品界面");
	}

	private void initView() {
		tvTotalPrice = (TextView) findViewById(R.id.tv_main_totalprice);
		ivDelete = (ImageView) findViewById(R.id.iv_main_delete);
		btnCommitOrder = (Button) findViewById(R.id.btn_main_commitorder);
		ll_layout = (LinearLayout) findViewById(R.id.ll_price_commit);
	}

	private void initListView() {
		ptrListView = (PullToRefreshListView) findViewById(R.id.listview_main_food);
		listView = ptrListView.getRefreshableView();
		ptrListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				refreshView.getLoadingLayoutProxy().setPullLabel("拉拉拉拉拉");
				refreshView.getLoadingLayoutProxy().setRefreshingLabel("刷刷刷刷刷");
				refreshView.getLoadingLayoutProxy().setReleaseLabel("松松松松松");
				refreshView.getLoadingLayoutProxy().setLoadingDrawable(getResources().getDrawable(R.drawable.ic_launcher));
				
				refresh();
			}
		});
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	protected void refresh() {
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
	
					list.add(Food);

                }
			
				Log.i("TAG", "list的结果："+list);
				
				adapter = new FoodAdapter(MainActivity.this, list);
				listView.setAdapter(adapter);
			}
		});
	}
	
    
}
