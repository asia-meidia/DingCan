package com.example.demo.adapter;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.example.demo.R;
import com.example.demo.bean.Bread;
import com.example.demo.bean.Food;
import com.example.demo.listener.OnLoadImageFinishListener;
import com.example.demo.util.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{
	Context context;
//	List<Bread> list;
	List<Food> list; 
	
	LayoutInflater inflater;
	
	public ListAdapter(Context context,List<Food> list) {
		super();
		Log.i("TAG", "看看有什么"+list);
		this.context = context;
		this.list = list;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageLoader.init(context);
	}

//	public ListAdapter(OnLoadImageFinishListener listener, List<Bread> list2) {
//		// TODO Auto-generated constructor stub
//		super();
//		Log.i("TAG", "看看有什么"+list);
//		this.context = context;
//		this.list = list;
//		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		ImageLoader.init(context);
//	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Food getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_list_layout, parent, false);
			vh.iv = (ImageView) convertView.findViewById(R.id.foodPic);
			vh.tvfoodName = (TextView) convertView.findViewById(R.id.foodName);
			vh.tvfoodprice = (TextView) convertView.findViewById(R.id.foodPrice);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
//		Bread bread = getItem(position);
		ImageLoader.loadImage(list.get(position).getFoodpic().getFileUrl(context), vh.iv);
//		vh.iv.setImageResource(R.drawable.aaa);
//		vh.tvfoodName.setText("菜名："+bread.getName());
//		vh.tvfoodprice.setText("价格："+bread.getPrice());
		vh.tvfoodName.setText(list.get(position).getFoodname());
		vh.tvfoodprice.setText(list.get(position).getFoodprice());
		
		return convertView;
	}



	public class ViewHolder{
		ImageView iv;
		TextView tvfoodName,tvfoodprice;
	}
	
	
}
