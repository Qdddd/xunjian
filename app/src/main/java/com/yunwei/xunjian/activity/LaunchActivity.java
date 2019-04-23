package com.yunwei.xunjian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.StatusBarUtil;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //设置状态栏背景色和字体颜色
     //   StatusBarUtil.setStatusBarMode(this, true, android.R.color.transparent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
