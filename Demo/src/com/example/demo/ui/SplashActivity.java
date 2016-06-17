package com.example.demo.ui;

import com.example.demo.R;
import com.example.demo.R.anim;
import com.example.demo.R.id;
import com.example.demo.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {
	RelativeLayout animContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initView();
		initAnim();
	}

	private void initAnim() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
		animContainer.startAnimation(anim);
		
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				startActivity(new Intent(SplashActivity.this,LoginActivity.class));
				finish();
			}
		});
	}

	private void initView() {
		animContainer = (RelativeLayout) findViewById(R.id.rl_splash_animcontainer);
	}

}
