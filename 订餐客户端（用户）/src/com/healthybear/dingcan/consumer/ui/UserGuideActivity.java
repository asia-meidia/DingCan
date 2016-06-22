package com.healthybear.dingcan.consumer.ui;

import java.util.ArrayList;
import java.util.List;

import com.healthybear.dingcan.consumer.R;
import com.healthybear.dingcan.consumer.R.layout;
import com.healthybear.dingcan.consumer.R.menu;
import com.healthybear.dingcan.consumer.ui.fragment.AFragment;
import com.healthybear.dingcan.consumer.ui.fragment.BFragment;
import com.healthybear.dingcan.consumer.ui.fragment.CFragment;
import com.healthybear.dingcan.consumer.ui.fragment.DFragment;
import com.viewpagerindicator.CirclePageIndicator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

public class UserGuideActivity extends FragmentActivity{

	ViewPager vp;
	MyPagerAdapter adapter;
	CirclePageIndicator cpi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_guide);
		
		
		vp = (ViewPager) findViewById(R.id.vp_guide);
		cpi = (CirclePageIndicator) findViewById(R.id.cpi_guide);
		vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		//cpi.setViewPagerһ��Ҫд��ViewPager.setAdapter���·�
		cpi.setViewPager(vp);
		cpi.setFillColor(Color.parseColor("#ffff6633"));
		cpi.setRadius(8);
		//��ʹ����viewpagerindicator��ʱ�򣬲��ܸ�viewpager���û�������
		//���Ҫʹ�û���������ֻ����Ӹ�viewpagerindicator
		cpi.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(arg0==3){
					cpi.setVisibility(View.INVISIBLE);
				}else{
					cpi.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private class MyPagerAdapter extends FragmentPagerAdapter{
		List<Fragment> fragments;
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new ArrayList<Fragment>();
			fragments.add(new AFragment());
			fragments.add(new BFragment());
			fragments.add(new CFragment());
			fragments.add(new DFragment());
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
		
	}

}
