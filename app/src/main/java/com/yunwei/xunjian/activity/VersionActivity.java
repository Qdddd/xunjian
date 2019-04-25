package com.yunwei.xunjian.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.StatusBarUtil;
import com.yunwei.xunjian.util.ToastUtil;
import com.yunwei.xunjian.util.Tools;

public class VersionActivity extends AppCompatActivity {

    private ImageView imageView_back;
    private TextView textView_title;
    private TextView textView_currentVersionName;
    private TextView textView_newVersionName;
    private TextView textView_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);

        imageView_back = (ImageView) findViewById(R.id.back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textView_title = (TextView)findViewById(R.id.title);
        textView_title.setText("当前版本");

        textView_check = (TextView)findViewById(R.id.check);
        imageView_back.setImageResource(R.mipmap.back);
        textView_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(getApplicationContext(), "当前已是最新版本", Toast.LENGTH_SHORT);
            }
        });

        textView_currentVersionName = (TextView)findViewById(R.id.currentVersionName);
        textView_newVersionName = (TextView)findViewById(R.id.newVersionName);

        init();
    }

    private void init(){
        String appVersionName = Tools.getAppVersionName(this);
        textView_currentVersionName.setText("当前版本：V" + appVersionName);
    }
}
