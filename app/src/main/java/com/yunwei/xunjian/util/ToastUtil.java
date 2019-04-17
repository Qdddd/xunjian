package com.yunwei.xunjian.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {

    public static void showToast(Context context, String string, int duration){
        Toast mToast = Toast.makeText(context, null, duration);
        mToast.setText(string);
        mToast.show();
    }

    public static void showToast(Context context, int stringId, int duration){
        String string = context.getResources().getString(stringId);
        showToast(context, string, duration);
    }

    public static void showToastWithImage(Context context, String string, int duration, int resId){
        Toast mToast = Toast.makeText(context, null, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(string);

        //使用 getView() 方法得到 Toast 的 View
        LinearLayout toastView = (LinearLayout)mToast.getView();
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(resId);
        toastView.setOrientation(LinearLayout.VERTICAL);
        //ViewGroup 的 addView() 方法用于添加子 View，第一个参数是 要添加的 View， 第二个是添加的位置
        toastView.addView(imageView, 0);

        mToast.show();
    }

    public static void showToastWithImage(Context context, int stringId, int duration, int resId) {
        String string = context.getResources().getString(stringId);
        showToastWithImage(context, string, duration, resId);
    }
}
