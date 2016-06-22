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
	
	private TextView tvTotalPrice;		//����ļ۸�
	private ImageView ivDelete;			//ɾ��
	private Button btnCommitOrder;		//����
	private LinearLayout ll_layout;		//���㲼��
	
	private FoodAdapter adapter;		//�Զ���������
	private List<Food> list;		//���ﳵ���ݼ���

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
		setHeaderTitle(headerView, "��Ʒ����");
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
				refreshView.getLoadingLayoutProxy().setPullLabel("����������");
				refreshView.getLoadingLayoutProxy().setRefreshingLabel("ˢˢˢˢˢ");
				refreshView.getLoadingLayoutProxy().setReleaseLabel("����������");
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
	
					list.add(Food);

                }
			
				Log.i("TAG", "list�Ľ����"+list);
				
				adapter = new FoodAdapter(MainActivity.this, list);
				listView.setAdapter(adapter);
			}
		});
	}
	
    
}
