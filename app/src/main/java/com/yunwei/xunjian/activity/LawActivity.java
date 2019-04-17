package com.yunwei.xunjian.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.StatusBarUtil;

public class LawActivity extends BaseActivity {

    private ImageView imageView_back;
    private TextView textView_title;
    private TextView textView_law_title;
    private TextView textView_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);

        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);

        imageView_back = (ImageView)findViewById(R.id.back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textView_title = (TextView)findViewById(R.id.title);
        textView_title.setText(R.string.law_title);

        textView_law_title = (TextView)findViewById(R.id.law_title);
        String title = getResources().getString(R.string.law_title);
        textView_law_title.setText("\n" + title + "\n");

        textView_content = (TextView)findViewById(R.id.law_content);
        String context = getResources().getString(R.string.law_content);
        textView_content.setText(Html.fromHtml(context));
    }
}
