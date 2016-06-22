package com.healthybear.dingcan.consumer.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;

import com.healthybear.dingcan.consumer.R;
import com.healthybear.dingcan.consumer.constant.Constant.Position;



public class BaseActivity extends Activity{
	Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
	}
	
	//设置头部布局的方法
		public void setHeaderTitle(View headerView,String title,Position position){
			TextView tv = (TextView) headerView.findViewById(R.id.tv_header_title);
			if(title==null){
				tv.setText("TITLE");
			}else{
				tv.setText(title);
			}
			
			switch (position) {
			case LEFT:
				tv.setGravity(Gravity.LEFT);
				break;

			default:
				tv.setGravity(Gravity.CENTER);
				break;
			}
		}
		
		//重载方法，设置标题是不指定位置，就让标题居中显示
		public void setHeaderTitle(View headerView,String title){
			setHeaderTitle(headerView, title,Position.CENTER);
		}
		
		/**
		 * 
		 * @param headerView
		 * @param resId
		 * @param position		LEFT 是设置头部左侧的ImageView
		 *                 		RIGHT 、 CENTER均为设置头部右侧的ImageView
		 * @param listener
		 */
		public void setHeaderImage(View headerView,int resId,Position position,OnClickListener listener){
			ImageView iv = null;
			switch (position) {
			case LEFT:
				iv = (ImageView) headerView.findViewById(R.id.iv_header_left);
				break;

			default:
				iv = (ImageView) headerView.findViewById(R.id.iv_header_right);
				break;
			}
			
			iv.setImageResource(resId);
			iv.setColorFilter(Color.WHITE,Mode.SRC_ATOP);
			if(listener!=null){
				iv.setOnClickListener(listener);
			}
		}
		
		
		public void setHeaderImage(View headerView,int resId,Position position){
			setHeaderImage(headerView, resId, position,null);
		}

		public void setHeaderImage(View headerView,int resId){
			setHeaderImage(headerView, resId, Position.LEFT);
		}

		//写一些打印“吐司”和Log的方法

		public void toast(String text){

			if(TextUtils.isEmpty(text)){
				return;
			}
			toast.setText(text);
			toast.show();
		}

		public void log(String log){
			if(Bmob.DEBUG)
				Log.d("TAG",this.getClass().getSimpleName()+": "+log);
		}
		
		public void toastAndLog(String text,String log){
			toast(text);
			log(log);
		}

		//写一些界面跳转的方法
		//简单的界面跳转，不需要利用Intent传递参数
		public void jumpTo(Class<?> clazz,boolean isFinish){
			Intent intent = new Intent(this,clazz);
			startActivity(intent);
			if(isFinish){
				finish();
			}
		}
		
		//界面跳转时，需要Intent携带参数
		public void jumpTo(Intent intent,boolean isFinish){
			startActivity(intent);
			if(isFinish){
				finish();
			}
		}
		
		/**
		 * 判断用户在EditText中输入的信息是否完整
		 * @param editTexts
		 * @return false 意味着用户输入的是完整的
		 *         true  意味着至少有一个EditText用户未输入内容
		 */
		
		public boolean isEmpty(EditText... editTexts){
			
			for(EditText et:editTexts){
				if(TextUtils.isEmpty(et.getText().toString())){
					String string = "请输入完整!";
					//根据string创建一个SpannableString
					SpannableString ss = new SpannableString(string);
					
					ss.setSpan(new ForegroundColorSpan(Color.GREEN), 3,5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new BackgroundColorSpan(Color.BLACK), 0, 4, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new ImageSpan(this, R.drawable.ue059), 5, 6, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
					ss.setSpan(new AbsoluteSizeSpan(20, true), 3, 5, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
					
					et.setError(ss);
					return true;
				}
			}
			
			return false;
		}

}
