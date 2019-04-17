package com.yunwei.xunjian.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.StatusBarUtil;

public class IntroduceActivity extends BaseActivity {

    private ImageView imageView_back;
    private TextView textView_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);

        imageView_back = (ImageView)findViewById(R.id.back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textView_title = (TextView)findViewById( R.id.title);
        textView_title.setText("公司介绍");
    }
}
