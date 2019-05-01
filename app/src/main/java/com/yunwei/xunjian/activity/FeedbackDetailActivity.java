package com.yunwei.xunjian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunwei.xunjian.R;
import com.yunwei.xunjian.bean.HistoryFeedback;
import com.yunwei.xunjian.util.StatusBarUtil;

import static com.yunwei.xunjian.util.Constants.BASIC_IMAGE_URL;
/**
* FileName: FeedbackDetailActivity.java
* Description:历史反馈详情界面
* @author  Monica J
* @Date    2019/4/30 0030  15:45
* @version 1.00
*/

public class FeedbackDetailActivity extends AppCompatActivity {
    private TextView feedback_time;
    private TextView feedback_content;
    private ImageView feedback_img;
    private TextView feedback_reply_time;
    private TextView feedback_reply_content;
    private TableRow feedback_img_row,feedback_reply_time_row,feedback_reply_content_row,null_data_row;
    private String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_detail);
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);
        //根据id获取组件
        feedback_img_row=findViewById(R.id.feedback_img_row);
        feedback_reply_time_row=findViewById(R.id.feedback_reply_time_row);
        feedback_reply_content_row=findViewById(R.id.feedback_reply_content_row);
        null_data_row=findViewById(R.id.null_data_row);
        feedback_time=findViewById(R.id.feedback_time);
        feedback_content=findViewById(R.id.feedback_content);
        feedback_img=findViewById(R.id.feedback_img);
        feedback_reply_time=findViewById(R.id.feedback_reply_time);
        feedback_reply_content=findViewById(R.id.feedback_reply_content);
        //设置导航栏信息及返回按键
        TextView textView_title = this.findViewById(R.id.title);
        textView_title.setText("反馈详情");
        ImageView imageView_back=findViewById(R.id.back);
        imageView_back.setImageResource(R.mipmap.back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取前一个Activity传来的信息并进行判断后发放入组件中
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle != null) {
            HistoryFeedback historyFeedback= (HistoryFeedback) bundle.getSerializable("historyFeedback");
            feedback_time.setText(historyFeedback.getFeedbackDateTime());
            feedback_content.setText(historyFeedback.getFeedbackContent());
            Log.i("FeedbackDetail","picPath:"+historyFeedback.getFeedbackPicPath());
            if(historyFeedback.getFeedbackPicPath()!=null&&!historyFeedback.getFeedbackPicPath().equals("")&&historyFeedback.getFeedbackPicPath()!="null") {
                imgURL=BASIC_IMAGE_URL+historyFeedback.getFeedbackPicPath();
                Glide.with(this).load(imgURL).into(feedback_img);
                feedback_img_row.setVisibility(View.VISIBLE);
            }else{
                feedback_img_row.setVisibility(View.GONE);
            }
            if (historyFeedback.getReply()!=null&&historyFeedback.getReply().equals("1")) {
                null_data_row.setVisibility(View.GONE);
                feedback_reply_time_row.setVisibility(View.VISIBLE);
                feedback_reply_content_row.setVisibility(View.VISIBLE);
                feedback_reply_time.setText(historyFeedback.getReplyDateTime());
                feedback_reply_content.setText(historyFeedback.getReplyContent());
            }else if(historyFeedback.getReply()!=null&&historyFeedback.getReply().equals("0")){
                null_data_row.setVisibility(View.VISIBLE);
                feedback_reply_time_row.setVisibility(View.GONE);
                feedback_reply_content_row.setVisibility(View.GONE);
            }
        }

        feedback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackDetailActivity.this, BigImageActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("imgURL",imgURL);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
