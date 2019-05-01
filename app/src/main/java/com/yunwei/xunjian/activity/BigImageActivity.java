package com.yunwei.xunjian.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;
import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.StatusBarUtil;

public class BigImageActivity extends Activity {
    private TextView textView_exit;
    private TouchImageView imageView;

    private String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);
        imageView = findViewById(R.id.image);
        textView_exit = findViewById(R.id.exit);
        textView_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        imgURL=bundle.getString("imgURL");
        Glide.with(this).load(imgURL).into(imageView);
    }
}
