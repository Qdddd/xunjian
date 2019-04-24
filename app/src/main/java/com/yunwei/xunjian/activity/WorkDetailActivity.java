package com.yunwei.xunjian.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.fragment.FragmentTabBook;
import com.yunwei.xunjian.util.ItentButtonUtil;

public class WorkDetailActivity extends ItentButtonUtil {
    private TextView title;
    private ImageView imageView_back;
    private Button soso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail);
        title=findViewById(R.id.title);
        imageView_back=findViewById(R.id.back);
        title.setText("工单详情");
        imageView_back.setImageResource(R.mipmap.return1);
        back(imageView_back);
        soso=findViewById(R.id.soso);
        IntentNull(WorkDetailActivity.this,soso, FragmentTabBook.class);
    }
}
