package com.yunwei.xunjian.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Tools {

    private static final String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";
    private static final String GAODE_PACKAGE_NAME = "com.autonavi.minimap";
    private static final String TENCENT_PACKAGE_NAME = "com.tencent.map";

    /**
     * 拨打电话，跳转到拨号界面
     * @param context, phoneNum
     */
    public static void callPhone(Context context, String phoneNum){

        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }


    /*
     * 将时间戳转换为时间
     * 将距离1970年的数字时间转换成正常的字符串格式时间；
     * 比如数字时间："1513345743"
     * 转换后："2017-12-15 21:49:03"
     * @param time
     * @return9
     */
    public static String stampToDate(Long timestamp){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timestamp*1000);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    private static boolean isInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }

    /**
     * 跳转百度地图
     */
    public static void goToBaiduMap(Context context, Double longitude, Double latitude) {
        if (!isInstalled(context , BAIDU_PACKAGE_NAME)) {
            ToastUtil.showToast(context, "请先安装百度地图客户端", Toast.LENGTH_SHORT);
            return;
        }else {
            Intent intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/direction?destination="
                    + latitude + "," + longitude
//                + "|name:" + mAddressStr  // 终点
                    + "&coord_type=wgs84"
                    + "&mode=driving"  // 导航路线方式
                    + "&src=" + "com.laosong.zzdyyw"));
            //               + "&src=#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"));
            intent.setPackage("com.baidu.BaiduMap");
            context.startActivity(intent); // 启动调用
        }
    }

    /**
     * 跳转高德地图
     */
    public static void goToGaodeMap(Context context, Double longitude, Double latitude) {
        if (!isInstalled(context, GAODE_PACKAGE_NAME)) {
            ToastUtil.showToast(context, "请先安装高德地图客户端", Toast.LENGTH_SHORT);
            return;
        }else {
            Uri URI = Uri.parse("amapuri://route/plan/?sourceApplication=amap"
                    + "&dlat=" + latitude
                    + "&dlon=" + longitude
                    + "&dev=1"
                    + "&t=0");
            Intent intent = new Intent("android.intent.action.VIEW", URI);
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        }
    }

    /**
     * 跳转到腾讯地图
     */
    public static void goToTentcentMap(Context context, Double longitude, Double latitude) {
        if (!isInstalled(context, TENCENT_PACKAGE_NAME)) {
            ToastUtil.showToast(context, "请先安装腾讯地图客户端", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse("qqmap://map/routeplan?" +
                "type=drive" +
//                "&from=" +
//                "&fromcoord=" +
//                "&to=" +
                "&tocoord=" + latitude + "," + longitude +
                "&coord_type=1" + //指定坐标类型为GPS
                "&policy=0" +
                "&referer="));
        context.startActivity(intent);
    }

    /**
     * 把double类型格式化未8位小数
     */
    public static double formatDouble(double arg1, int arg2){
        String[] format = {"0","0.0", "0.00", "0.000", "0.0000", "0.00000", "0.000000", "0.0000000", "0.00000000"};
        DecimalFormat df = new DecimalFormat(format[arg2]);
        double number = Double.parseDouble(df.format(arg1));
        return number;
    }

    /**
     * 返回当前程序版本号
     */
    public static String getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode + "";
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName=null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
