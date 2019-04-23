package com.yunwei.xunjian.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunwei.xunjian.R;

public class FinishedWorkDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_work_detail);

        TextView textView_title = this.findViewById(R.id.title);
        textView_title.setText("已办工单详情");
        ImageView imageView=findViewById(R.id.back);
        imageView.setImageResource(R.mipmap.back);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String workNum=bundle.getString("workNum");
        String workName=bundle.getString("workName");
        String line=bundle.getString("line");
        String sendOrderPerson=bundle.getString("sendOrderPerson");
        String sendOrderTime=bundle.getString("sendOrderTime");
        String handler=bundle.getString("handler");
        String accomplishTime=bundle.getString("accomplishTime");

        TextView workNumTextView=findViewById(R.id.workNum);
        TextView workNameTextView=findViewById(R.id.workName);
        TextView lineTextView=findViewById(R.id.line);
        TextView sendOrderPersonTextView=findViewById(R.id.sendOrderPerson);
        TextView sendOrderTimeTextView=findViewById(R.id.sendOrderTime);
        TextView handlerTextView=findViewById(R.id.handler);
        TextView accomplishTimeTextView=findViewById(R.id.accomplishTime);


        workNumTextView.setText(workNum);
        workNameTextView.setText(workName);
        lineTextView.setText(line);
        sendOrderPersonTextView.setText(sendOrderPerson);
        sendOrderTimeTextView.setText(sendOrderTime);
        handlerTextView.setText(handler);
        accomplishTimeTextView.setText(accomplishTime);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
