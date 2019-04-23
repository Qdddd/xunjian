package com.yunwei.xunjian.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ItentButtonUtil extends AppCompatActivity {

    public void back(ImageView imageView_back){
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }
    public  void  IntentNull(final Context context, View view,final Class clas){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,clas);
                startActivity(intent);
            }
        });


    }

}
