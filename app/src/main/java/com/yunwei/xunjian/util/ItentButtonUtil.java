package com.yunwei.xunjian.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.yunwei.xunjian.bean.ContactItem;

import java.util.ArrayList;
import java.util.List;

public class ItentButtonUtil extends AppCompatActivity {
    List<ContactItem> list1=new ArrayList<ContactItem>();

    public  List<ContactItem> getList1(){
        return this.list1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void back(ImageView imageView_back) {
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

    public void IntentNull(final Context context, View view, final Class clas) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, clas);
                startActivity(intent);
            }
        });
    }
 /*   public void GetDepart_Contact(String ORGANIZ_CODE, final List<ContactItem> list) {
        String URL = GET_DepartOrContact + "?organizCode=" + ORGANIZ_CODE;
        Log.i("URL","="+URL);
        HttpUtil.sendOkHttpRequest(URL, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              final   String responseData = response.body().string();

                if (!responseData.equals("[]")) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parseJSONWithJSONObject(responseData,list);
                        }
                    });


                }
            }
        });

    }
*/
   /* public void parseJSONWithJSONObject(String jsonData, List<ContactItem> list) {
        try {
            String ORGANIZ_CODE, name,phone,type;
            JSONObject jsonOb = new JSONObject(jsonData);
            String a = jsonOb.get("extend").toString();
            if (!a.equals("{}")) {
                jsonOb = new JSONObject(a);
                a = jsonOb.get("organizInfo").toString();
            }
            JSONArray jsonArray = new JSONArray(a);
            Log.d("organizInfo", "Length of jason array is : " + jsonArray.length());
            type=jsonArray.getJSONObject(0).getString("TYPE");
            if(type.equals("1")){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ORGANIZ_CODE = jsonObject.getString("ORGANIZ_CODE");
                    name = jsonObject.getString("ORGANIZ_POST_NAME");
                    ContactItem con = new ContactItem();
                    con.setName(name);
                    con.setORGANIZ_CODE(ORGANIZ_CODE);
                    list.add(con);
                }
            }
            if(type.equals("2")){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    phone= jsonObject.getString("PHONE_NUM1");
                    name = jsonObject.getString("NAME");
                    ContactItem con = new ContactItem();
                    con.setName(name);
                    con.setPHONE_NUM1(phone);
                    list.add(con);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/





}