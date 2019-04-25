package com.yunwei.xunjian.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.bean.HistoryFeedback;
import com.yunwei.xunjian.adapter.FeedbackAdapter;
import com.yunwei.xunjian.util.HttpUtil;
import com.yunwei.xunjian.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.yunwei.xunjian.util.Constants.GET_FEEDBACK;

public class HistoryFeedbackActivity extends BaseActivity {

    private TextView textView_title, textView_null_feedback;
    private ImageView imageView_back;
    private SwipeRefreshLayout swipeRefresh;
    private ListView listView_feedback;

    private FeedbackAdapter feedbackAdapter;
    private List<HistoryFeedback> list;

    private static final int UPDATE_FEEDBACK = 3;
    private static final int UPDATE_NULL = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPDATE_FEEDBACK:
                    textView_null_feedback.setVisibility(View.GONE);
                    feedbackAdapter = new FeedbackAdapter(HistoryFeedbackActivity.this,R.layout.feedback_item,list);
                    listView_feedback.setAdapter(feedbackAdapter);
                    break;
                case UPDATE_NULL:
                    swipeRefresh.setVisibility(View.GONE);
                    textView_null_feedback.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_feedback);

        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);

        textView_title = (TextView)findViewById(R.id.title);
        textView_title.setText("历史反馈");
        imageView_back = (ImageView)findViewById(R.id.back);
        imageView_back.setImageResource(R.mipmap.back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.history_feedback);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });

        listView_feedback = (ListView)findViewById(R.id.list_feedback);
        textView_null_feedback = (TextView)findViewById(R.id.null_feedback);

        initView();
    }

    private void initView(){
        String URL = GET_FEEDBACK;
        HttpUtil.sendOkHttpRequest(URL, new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();

                if(!responseData.equals("") && !responseData.equals("[]")) {
                    parseJSONWithJSONObject(responseData);
                    Message msg = new Message();
                    msg.what = UPDATE_FEEDBACK;
                    handler.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    msg.what = UPDATE_NULL;
                    handler.sendMessage(msg);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void parseJSONWithJSONObject(String jsonData) {
        list = new ArrayList<>();
        Iterator<HistoryFeedback> iterator = list.iterator();
        while(iterator.hasNext()){
            iterator.next();
            iterator.remove();
        }

        try {
            Long timeStamp;
            String feedbsckPerson;
            String feedback;

            JSONArray jsonArray = new JSONArray(jsonData);
            Log.d("MainActivity", "Length of jason array is : " + jsonArray.length());
            for (int i=0; i<jsonArray.length(); i++) {
                HistoryFeedback historyFeedback = new HistoryFeedback();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                feedbsckPerson = jsonObject.getString("feedback_person");
                timeStamp  = jsonObject.getLong("feedback_date_time");
                feedback = jsonObject.getString("feedback_content");

                historyFeedback.setFeedbackPerson(feedbsckPerson);
                historyFeedback.setFeedbackDateTime(timeStamp);
                historyFeedback.setFeedbackContent(feedback);
                list.add(historyFeedback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
