package com.yunwei.xunjian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.StatusBarUtil;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageView_back;
    private TextView textView_title;
    private RelativeLayout layout_introduce, layout_law;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);

        imageView_back = (ImageView)findViewById(R.id.back);
        imageView_back.setOnClickListener(this);

        textView_title = (TextView)findViewById(R.id.title);
        textView_title.setText("关于我们");

        layout_introduce = (RelativeLayout)findViewById(R.id.introduce);
        layout_introduce.setOnClickListener(this);

        layout_law = (RelativeLayout)findViewById(R.id.law);
        layout_law.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch(v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.introduce:
                intent.setClass(this, IntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.law:
                intent.setClass(this, LawActivity.class);
                startActivity(intent);
                break;

        }
    }
}
