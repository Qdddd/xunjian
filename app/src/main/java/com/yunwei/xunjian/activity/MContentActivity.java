package com.yunwei.xunjian.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.ItentButtonUtil;
import com.yunwei.xunjian.util.StatusBarUtil;

public class MContentActivity extends ItentButtonUtil {
private TextView title;
private ImageView imageView_back;
private TextView unfinshed;
private TextView finshed;
    @Override//任务内容
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcontent);
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);
        title=findViewById(R.id.title);
        imageView_back=findViewById(R.id.back);
        title.setText("任务内容");
        imageView_back.setImageResource(R.mipmap.return1);
        back(imageView_back);
        unfinshed=findViewById(R.id.unfinished);
        finshed=findViewById(R.id.finished);
        IntentNull(MContentActivity.this,unfinshed,WorkDetailActivity.class);
        IntentNull(MContentActivity.this,finshed,WorkDetailActivity.class);



    }

}
