package com.guo.demoserver.ui;



import com.guo.demoserver.R;
import com.guo.demoserver.R.id;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


public class BaseActivity extends Activity{
	
	public enum Position{LEFT,RIGHT}
	
	public void setHeaderTitle(View headerView,String title){
		TextView tv = (TextView) headerView.findViewById(R.id.tv_headerview_title);
		if(TextUtils.isEmpty(title)){
			tv.setText("TITLE");
		}else{
			tv.setText(title);
		}
	}
	
	public ImageView setHeaderImage(View headerView,int resId,Position pos,OnClickListener listener){
		ImageView iv;
		if(pos == Position.LEFT){
			iv = (ImageView) headerView.findViewById(R.id.iv_headerview_left);
		}else{
			iv = (ImageView) headerView.findViewById(R.id.iv_headerview_right);
		}
		iv.setImageResource(resId);
		iv.setOnClickListener(listener);
		return iv;
		
	}
}
