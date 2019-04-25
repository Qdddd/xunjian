package com.yunwei.xunjian.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.yunwei.xunjian.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TaskDispose extends AppCompatActivity {

    //定义返回按钮
    private ImageView back;

    //定义输入名称
    private EditText et_name;

    //定义点击选中的类型
    private RadioGroup cho_type;

    //定义选择的杆塔
    private Spinner cho_ganta;

    //定义经度的属性
    TextView longitude;

    //定义纬度的属性
    TextView latitude;

    //定义获取上级设备的杆塔
    private Spinner sup_ganta;

    //定义是否为分支点
    private Spinner spinner_isbranch;

    //定义是否是主线的属性
    private RadioGroup cho_ismain;

    //定义是否是同杆架设
    private Spinner spinner_istwo;

    //定义修改的属性
    private RadioButton fl_update;

    //定义点击位置的属性
    private RadioButton weizhi;

    //定义保存的属性
    private RadioButton fl_baocun;

    //判断是否保存
    private int flag=0;

    public LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_dispose);
        //获取每个id的控件
        back=(ImageView)findViewById(R.id.back);
        et_name = (EditText)findViewById(R.id.et_name);
        cho_type=(RadioGroup)findViewById(R.id.cho_type);
        cho_ganta=(Spinner)findViewById(R.id.cho_ganta);
        longitude = (TextView)findViewById(R.id.et_longitude);
        latitude = (TextView)findViewById(R.id.et_latitude);
        sup_ganta=(Spinner)findViewById(R.id.sup_ganta);
        spinner_isbranch=(Spinner)findViewById(R.id.spinner_isbranch);
        cho_ismain=(RadioGroup)findViewById(R.id.cho_ismain);
        spinner_istwo=(Spinner)findViewById(R.id.spinner_istwo);
        fl_update=(RadioButton)findViewById(R.id.fl_update);
        weizhi = (RadioButton)findViewById(R.id.fl_weizhi);
        fl_baocun = (RadioButton)findViewById(R.id.fl_baocun);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击修改时，改变输入框中的状态
        fl_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_name.setEnabled(true);
                cho_type.getChildAt(0).setEnabled(true);           //设置type_RadioButton中的第一个按钮为可编辑状态
                cho_type.getChildAt(1).setEnabled(true);
                cho_ganta.setEnabled(true);
                sup_ganta.setEnabled(true);
                spinner_isbranch.setEnabled(true);
                cho_ismain.getChildAt(0).setEnabled(true);           //设置ismain_RadioButton中的第一个按钮为可编辑状态
                cho_ismain.getChildAt(1).setEnabled(true);
                spinner_istwo.setEnabled(true);
                flag=0;
            }
        });

        //点击位置时获取当前位置坐标
        weizhi.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                        mLocationClient = new LocationClient(getApplicationContext());
                        mLocationClient.registerLocationListener(new MyLocationListener());
                        List<String> perminssionList = new ArrayList<>();
                        if (ContextCompat.checkSelfPermission(TaskDispose.this,Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED){
                            perminssionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                        if (ContextCompat.checkSelfPermission(TaskDispose.this , Manifest.permission.READ_PHONE_STATE) !=
                                PackageManager.PERMISSION_GRANTED){
                            perminssionList.add(Manifest.permission.READ_PHONE_STATE);
                        }
                        if (ContextCompat.checkSelfPermission(TaskDispose.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED){
                            perminssionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        if (!perminssionList.isEmpty()){
                            String[] permissions = perminssionList.toArray(new String [perminssionList.size()]);
                            ActivityCompat.requestPermissions(TaskDispose.this, permissions , 1);
                        }else {
                            requestLocation();
                        }
            }
        });


        //点击保存时  改变输入状态  并提示保存成功
        fl_baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果没有填内容，则不能保存成功
                if (et_name.getText().toString().equals("") || longitude.getText().toString()==""){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(TaskDispose.this)
                            .setTitle("提示")
                            .setMessage("名称或经纬度不能为空")
                            //.setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })

                            .create();
                    fl_update.setChecked(true);
                    alertDialog2.show();
                }else {
                    if (flag == 1) {
                        Toast.makeText(TaskDispose.this, "已保存", Toast.LENGTH_SHORT).show();
                    } else {
                        final Map<String,Object> map = new HashMap<>();
                        final String bc_name = et_name.getText().toString(); //获取输入的名称
                        RadioButton type_radioButton = (RadioButton) findViewById(cho_type.getCheckedRadioButtonId());
                        final String bc_run = type_radioButton.getText().toString();           //定义一个变量接收所选类型
                        final String bc_cho_ganta = (String) cho_ganta.getSelectedItem();       //获取当前选择的杆塔
                        final String bc_longitude = longitude.getText().toString();
                        final String bc_latitude = latitude.getText().toString();
                        final String bc_sup_ganta = (String) sup_ganta.getSelectedItem();       //获取是上级设备
                        final String bc_spinner_isbranch = (String) spinner_isbranch.getSelectedItem();//获取是否是分支点
                        final RadioButton ismain_radioButton = (RadioButton) findViewById(cho_ismain.getCheckedRadioButtonId());
                        final String bc_cho_ismain = ismain_radioButton.getText().toString();       //是否是主线
                        final String bc_spinner_istwo = (String) spinner_istwo.getSelectedItem();    //是否是同杆架设
                        //设置内容不可编辑
                        et_name.setEnabled(false);
                        cho_type.getChildAt(0).setEnabled(false);           //设置type_RadioButton中的第一个按钮为不可编辑状态
                        cho_type.getChildAt(1).setEnabled(false);
                        cho_ganta.setEnabled(false);
                        sup_ganta.setEnabled(false);
                        spinner_isbranch.setEnabled(false);
                        cho_ismain.getChildAt(0).setEnabled(false);           //设置ismain_RadioButton中的第一个按钮为不可编辑状态
                        cho_ismain.getChildAt(1).setEnabled(false);
                        spinner_istwo.setEnabled(false);
                        map.put("name",bc_name);
                        map.put("type",bc_run);
                        map.put("ganta",bc_cho_ganta);
                        map.put("longitude",bc_longitude);
                        map.put("latitude",bc_latitude);
                        map.put("sup_ganta",bc_sup_ganta);
                        map.put("spinner_isbranch",bc_spinner_isbranch);
                        map.put("cho_ismain",bc_cho_ismain);
                        map.put("spinner_istwo",bc_spinner_istwo);
                        Log.d("输入的名字", "" + bc_name);
                        Log.d("选择的类型", "" + bc_run);
                        Log.d("选择的杆塔", "" + bc_cho_ganta);
                        Log.d("经度", "" + bc_longitude);
                        Log.d("纬度", "" + bc_latitude);
                        Log.d("选择的上级设备", "" + bc_sup_ganta);
                        Log.d("是否是分支点", "" + bc_spinner_isbranch);
                        Log.d("是否是主线", "" + bc_cho_ismain);
                        Log.d("是否是同杆架设", "" + bc_spinner_istwo);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    OkHttpClient client = new OkHttpClient();

                                    FormBody formBody = new FormBody
                                            .Builder()
                                            .add("name", ""+bc_name)
                                            .add("type", ""+bc_run)
                                            .add("ganta", ""+bc_cho_ganta)
                                            .add("longitude", ""+bc_longitude)
                                            .add("latitude", ""+bc_latitude)
                                            .add("sup_ganta", ""+bc_sup_ganta)
                                            .add("spinner_isbranch", ""+bc_spinner_isbranch)
                                            .add("cho_ismain", "" + bc_cho_ismain)
                                            .add("spinner_istwo", "" + bc_spinner_istwo)
                                            .build();
                                    Request request = new Request.Builder()
                                            //指定访问的服务器地址是电脑本机
                                            .url("http://192.168.1.115:8080/yunwei2/app/workList/testApp")
                                            .post(formBody)
                                            .build();
                                   Response response = client.newCall(request).execute();
                                   String responseData = response.body().string();

                                    //showResponse(resonseData);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                        //final String URL="http://192.168.1.105:8080/yunwei2/workList/testApp?map="+map;

                        AlertDialog alertDialog1 = new AlertDialog.Builder(TaskDispose.this)
                                //.setTitle("这是标题")//标题
                                .setMessage("保存成功")//内容

                                //.setIcon(R.mipmap.ic_launcher)//图标
                                .create();
                        alertDialog1.show();
                        flag = 1;
                    }
                }
            }
        });

    }


    private void requestLocation(){
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition1 = new StringBuilder();
                    StringBuilder currentPosition2 = new StringBuilder();
                    currentPosition1.append("").append(location.getLatitude()).append("\n");
                    currentPosition2.append("").append(location.getLongitude()).append("\n");
                    latitude.setText(currentPosition1);
                    latitude.setTextSize(18);
                    longitude.setText(currentPosition2);
                    longitude.setTextSize(18);
                }
            });
        }



    }



}
