package com.guo.demoserver.adapter;

import java.util.List;

import com.guo.demoserver.R;
import com.guo.demoserver.bean.Food;
import com.guo.demoserver.bean.Message;
import com.guo.demoserver.listener.OnLoadFoodNameFinishListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CountAdapter extends BaseAdapter{

	Context context;
	List<Food> list; 
	
	LayoutInflater inflater;
	
	public CountAdapter(Context context,List<Food> list) {
		super();
		this.context = context;
		this.list = list;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView==null){
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.message, parent, false);
			vh.tvmsgName = (TextView) convertView.findViewById(R.id.msgname);
			vh.tvmsgcount = (TextView) convertView.findViewById(R.id.msgcount);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tvmsgName.setText(list.get(position).getName());
		vh.tvmsgcount.setText(list.get(position).getCount());
		
		return convertView;
	}

	
	
	class ViewHolder{
		TextView tvmsgName,tvmsgcount;
	}
}
