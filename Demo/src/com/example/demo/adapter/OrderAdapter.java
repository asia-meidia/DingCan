package com.example.demo.adapter;

import java.util.List;

import com.example.demo.R;
import com.example.demo.bean.Food;
import com.example.demo.bean.Order;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter{

	Context context;
	List<Order> list; 
	
	LayoutInflater inflater;
	public OrderAdapter(Context context, List<Order> list) {
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
			convertView = inflater.inflate(R.layout.item_order_list, parent, false);
			vh.tvname = (TextView) convertView.findViewById(R.id.item_order_name);
			vh.tvprice = (TextView) convertView.findViewById(R.id.item_order_price);
			vh.tvphone = (TextView) convertView.findViewById(R.id.item_order_phone);
			vh.tvaddress = (TextView) convertView.findViewById(R.id.item_order_address);
			vh.tvtime = (TextView) convertView.findViewById(R.id.item_order_time);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.tvname.setText("菜名："+list.get(position).getName().toString());
		vh.tvprice.setText("价格："+list.get(position).getPrice().toString());
		vh.tvphone.setText("电话："+list.get(position).getPhone().toString());
		vh.tvaddress.setText("地址："+list.get(position).getAddress().toString());
		vh.tvtime.setText("下单时间："+list.get(position).getCreatedAt().toString());
		return convertView;
	}

	class ViewHolder{
		TextView tvname,tvprice,tvphone,tvaddress,tvtime;
	}
}
