package com.yunwei.xunjian.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.fragment.FragmentTabGtasks;
import com.yunwei.xunjian.fragment.FragmentTabMine;
import com.yunwei.xunjian.util.BDLocationUtils;
import com.yunwei.xunjian.util.StatusBarUtil;
import com.yunwei.xunjian.util.ToastUtil;

public class MainActivity extends BaseActivity {

//    public static String nickName;
    public static String userName;

    private RadioGroup radioGroup;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragmentTabMine fragmentTabMine;
    private FragmentTabGtasks fragmentTabGtasks;

    BDLocationUtils bdLocationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);

        userName = getIntent().getExtras().getString("userName");  //员工编号
//        nickName = bundle.getString("nowNickName");   //姓名

        fm = getSupportFragmentManager();  //获取Fragment
        ft = fm.beginTransaction();   //开启一个事务

        radioGroup = (RadioGroup) findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_gtasks:
                        displayFragment(0);
                        break;
                    case R.id.rb_mine:
                        displayFragment(1);
                        break;
                    /*case R.id.rb_add:
                        displayFragment(2);
                        break;*/
                    default:
                        break;
                }
            }
        });

        displayFragment(0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)  //ACCESS_COARSE_LOCATION访问CellID或WiFi,只要当前设备可以接收到基站的服务信号，便可获得位置信息
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  //
                != PackageManager.PERMISSION_GRANTED
                ) {

            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        /**
         * 定位相关
         */
        //开启定位
        bdLocationUtils = new BDLocationUtils(MainActivity.this);
        bdLocationUtils.doLocation();//开启定位
        bdLocationUtils.mLocationClient.start();//开始定位
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    /**
     * 解决fragment出现重影问题，重写该方法，注释掉super.onSaveInstanceState(outState);
     * @param outState
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //如果用以下这种做法则不保存状态，再次进来的话会显示默认的tab
        // super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]  grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            ToastUtil.showToast(getApplicationContext(), R.string.agree_with_permissions, Toast.LENGTH_SHORT);
                            return;
                        }
                    }
                } else {
                    ToastUtil.showToast(getApplicationContext(), R.string.unknown_error, Toast.LENGTH_SHORT);
                    return;
                }
                break;
            default:
        }
    }

    @Override
    protected  void onDestroy() {
        bdLocationUtils.mLocationClient.stop();  //停止定位
        super.onDestroy();
    }

    private void displayFragment(int i){
        ft = fm.beginTransaction();
        hideFragment(ft);
        switch(i){
            case 0:
                if(fragmentTabGtasks == null){
                    fragmentTabGtasks = new FragmentTabGtasks();
                    ft.add(R.id.fragment_content, fragmentTabGtasks, "fragmentTab1");
                }else{
                    ft.show(fragmentTabGtasks);
                }
                break;
            case 1:
                if(fragmentTabMine == null){
                    fragmentTabMine = new FragmentTabMine();
                    ft.add(R.id.fragment_content,fragmentTabMine,"fragmentTab2");
                }else{
                    ft.show(fragmentTabMine);
                }
                break;
            case 2:

                break;
            default:
                break;
        }
        ft.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction ft){
        //如果fragment不为空，就先隐藏起来
        if(fragmentTabGtasks != null){
            ft.hide(fragmentTabGtasks);
        }
        if(fragmentTabMine != null){
            ft.hide(fragmentTabMine);
        }

    }

}
