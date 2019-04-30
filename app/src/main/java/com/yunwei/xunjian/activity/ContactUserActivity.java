package com.yunwei.xunjian.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

//人员
public class ContactUserActivity extends ItentButtonUtil {
    private ImageView imageView_back;
    private TextView title;
    private ListView listView;
    private List<ContactItem> list = new ArrayList<>();
    private AAComAdapter bookAdapter;
    private String phoneStr;
    private boolean isInput;
    private String ORGANIZ_CODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_user);
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);
        imageView_back=findViewById(R.id.back);
        imageView_back.setImageResource(R.mipmap.back);
        title=findViewById(R.id.title);
        title.setText("通讯录");
        back(imageView_back);
        //部门编号
        ORGANIZ_CODE=getIntent().getExtras().getString("ORGANIZ_CODE");
        initView();
        listView=findViewById(R.id.listitem);
        adapter();
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ContactUserActivity.this,list.get(position).getPHONE_NUM1(),Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(DepartmentActivity.this, );
                //  intent.putExtra("Organiz_code", list.get(position).getOrganiz_code());
                //intent.putExtra("userName", userName);
                //intent.putExtra("areaCode", list.get(position).get("areaCode"));
                //startActivity(intent);
            }
        });*/
    }
    public void initView(){
       // GetDepart_Contact(ORGANIZ_CODE);
        GetDepart_Contact(ORGANIZ_CODE);
    }
    public void adapter(){
        bookAdapter =new AAComAdapter<ContactItem>(ContactUserActivity.this,R.layout.contact_list_user_item,list) {
            @Override
            public void convert(AAViewHolder holder, final ContactItem mt) {
                TextView name = holder.getTextView(R.id.user_name);
                TextView phone= holder.getTextView(R.id.phone);
                TextView call = holder.getTextView(R.id.call);
                name.setText(mt.getName());
                phone.setText(mt.getPHONE_NUM1());
            //
             //   Log.d("AAViewHolder", "convert: " + "***" + mt.getName() + "---" + mt.getPHONE_NUM1());
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        phoneStr = mt.getPHONE_NUM1();
                        if (ContextCompat.checkSelfPermission(ContactUserActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            Log.i(TAG, "需要授权 ");
                            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactUserActivity.this, Manifest.permission.CALL_PHONE)) {
//                Log.i(TAG, "拒绝过了");
                                Toast.makeText(ContactUserActivity.this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
                            } else {
//                Log.i(TAG, "进行授权");
                                ActivityCompat.requestPermissions(ContactUserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                            }
                        } else {
//            Log.i(TAG, "不需要授权 ");

                            Intent intent = new Intent(Intent.ACTION_CALL);
                        //    Log.d("mt.getPHONE_NUM1()", "onClick: "  + mt.getPHONE_NUM1());
                            Uri data = Uri.parse("tel:" + phoneStr);
                            intent.setData(data);
                            startActivity(intent);
                        }

                    }
                });
            }

        };
        listView.setAdapter(bookAdapter);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.i(TAG, "同意授权");
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phoneStr);
                intent.setData(data);
                startActivity(intent);
            } else {
//                Log.i(TAG, "拒绝授权");

            }
        }
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

}
