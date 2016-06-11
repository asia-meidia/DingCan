package guo.com.appdemo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import guo.com.appdemo.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //延时跳转  判断是否第一次打开软件
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("isFirst",MODE_PRIVATE);
                Boolean isFirst = sp.getBoolean("isFirst",true);
                if(isFirst){
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean("isFirst",false);//第一次打开软件就修改isFirst的值
                    e.commit();//提交新的isFirst的值
                    startActivity(new Intent(SplashActivity.this,UserGuideActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }

        },3000);//延迟3秒跳转
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
