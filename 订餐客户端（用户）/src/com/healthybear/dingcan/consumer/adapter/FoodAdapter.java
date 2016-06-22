package com.healthybear.dingcan.consumer.adapter;

import java.util.List;

import com.healthybear.dingcan.consumer.bean.Food;
import com.healthybear.dingcan.consumer.util.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FoodAdapter extends BaseAdapter{
	
	Context context;
	List<Food> list; 
	
	LayoutInflater inflater;
	
	public FoodAdapter(Context context,List<Food> list) {
		super();
		Log.i("TAG", "看看有什么"+list);
		this.context = context;
		this.list = list;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageLoader.init(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
