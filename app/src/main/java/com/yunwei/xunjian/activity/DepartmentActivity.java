package com.yunwei.xunjian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.adapter.AAComAdapter;
import com.yunwei.xunjian.adapter.AAViewHolder;
import com.yunwei.xunjian.bean.ContactItem;
import com.yunwei.xunjian.util.HttpUtil;
import com.yunwei.xunjian.util.ItentButtonUtil;
import com.yunwei.xunjian.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.yunwei.xunjian.util.Constants.GET_DepartOrContact;

//部门
public class DepartmentActivity  extends ItentButtonUtil {
   private TextView title;
    private ImageView imageView_back;
    private String ORGANIZ_CODE;
    private ListView listView;
    private List<ContactItem> list = new ArrayList<>();
    private AAComAdapter bookAdapter;
    private Boolean flag=false;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);
        imageView_back=findViewById(R.id.back);
        imageView_back.setImageResource(R.mipmap.back);
        title=findViewById(R.id.title);
        title.setText("通讯录");
        back(imageView_back);

        //公司编号或部门编号
        ORGANIZ_CODE=getIntent().getExtras().getString("ORGANIZ_CODE");
        initView();
       listView=findViewById(R.id.listdepartment);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(DepartmentActivity.this,list.get(position).getName(),Toast.LENGTH_LONG).show();
                isGetDepart_Contact(list.get(position).getORGANIZ_CODE());
               ORGANIZ_CODE=  list.get(position).getORGANIZ_CODE();


            }
        });
    }
    public void initView(){
   //   list.clear();
        GetDepart_Contact(ORGANIZ_CODE);
        Log.i("list","-=="+list);
    }




    public void adapter(){
        bookAdapter =new AAComAdapter<ContactItem>(DepartmentActivity.this,R.layout.contact_list_item,list) {
            @Override
            public void convert(AAViewHolder holder, ContactItem mt) {
                TextView name = holder.getTextView(R.id.name);
                name.setText(mt.getName());
            }
        };
        listView.setAdapter(bookAdapter);
    }


    public void GetDepart_Contact(String ORGANIZ_CODE) {
        String URL = GET_DepartOrContact + "?organizCode=" + ORGANIZ_CODE;
        Log.i("URL","="+URL);
        HttpUtil.sendOkHttpRequest(URL, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (!responseData.equals("[]")) {
                    parseJSONWithJSONObject(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter();
                        }
                    });


                }
            }
        });

    }

    public void parseJSONWithJSONObject(String jsonData) {
        list.clear();
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
    }


    public void isGetDepart_Contact(String ORGANIZ_CODE) {
        String URL = GET_DepartOrContact + "?organizCode=" + ORGANIZ_CODE;
        HttpUtil.sendOkHttpRequest(URL, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (!responseData.equals("[]")) {
                    isparseJSONWithJSONObject(responseData);
                }
            }
        });
    }
    public void isparseJSONWithJSONObject(String jsonData) {
        try {
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
                Intent intent = new Intent(DepartmentActivity.this,DepartmentActivity.class );
                intent.putExtra("ORGANIZ_CODE", ORGANIZ_CODE);
                startActivity(intent);
            }
           else{
                Intent intent = new Intent(DepartmentActivity.this,ContactUserActivity.class );
                intent.putExtra("ORGANIZ_CODE", ORGANIZ_CODE);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
