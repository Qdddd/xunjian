package com.yunwei.xunjian.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.fragment.FragmentTab1HistoryFeedback;
import com.yunwei.xunjian.fragment.FragmentTab2HistoryFeedback;
import com.yunwei.xunjian.fragment.FragmentTab3HistoryFeedback;
import com.yunwei.xunjian.fragment.FragmentTabMine;
import com.yunwei.xunjian.util.StatusBarUtil;

/**
 * 查看用户历史反馈的界面
 *
 */
public class HistoryFeedbackActivity extends BaseActivity {
    //    public static String nickName;
    public static String userName;
    private ImageView imageView_back;
    private RadioGroup radioGroup;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragmentTab1HistoryFeedback fragmentTab1HistoryFeedback;
    private FragmentTab2HistoryFeedback fragmentTab2HistoryFeedback;
    private FragmentTab3HistoryFeedback fragmentTab3HistoryFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_feedback);
        TextView textView_title =findViewById(R.id.title);
        textView_title.setText("历史反馈");
        imageView_back = (ImageView)findViewById(R.id.back);
        imageView_back.setImageResource(R.mipmap.back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);
//        Bundle bundle = getIntent().getExtras();
//        userName = bundle.getString("userName");

        fm = getSupportFragmentManager();  //获取Fragment
        ft = fm.beginTransaction();   //开启一个事务

        radioGroup = (RadioGroup) findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.feedback_tb1:
                        displayFragment(0);
                        break;
                    case R.id.feedback_tb2:
                        displayFragment(1);
                        break;
                    case R.id.feedback_tb3:
                        displayFragment(2);
                        break;
                    default:
                        break;
                }
            }
        });

        displayFragment(0);
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


    private void displayFragment(int i){
        ft = fm.beginTransaction();
        hideFragment(ft);
        switch(i){
            case 0:
                if(fragmentTab1HistoryFeedback == null){
                    fragmentTab1HistoryFeedback = new FragmentTab1HistoryFeedback();
                    ft.add(R.id.fragment_content, fragmentTab1HistoryFeedback, "fragmentTab1");
                }else{
                    ft.show(fragmentTab1HistoryFeedback);
                }
                break;
            case 1:
                if(fragmentTab2HistoryFeedback == null){
                    fragmentTab2HistoryFeedback = new FragmentTab2HistoryFeedback();
                    ft.add(R.id.fragment_content,fragmentTab2HistoryFeedback,"fragmentTab2");
                }else{
                    ft.show(fragmentTab2HistoryFeedback);
                }
                break;
            case 2:
                if(fragmentTab3HistoryFeedback == null){
                    fragmentTab3HistoryFeedback = new FragmentTab3HistoryFeedback();
                    ft.add(R.id.fragment_content,fragmentTab3HistoryFeedback,"fragmentTab2");
                }else{
                    ft.show(fragmentTab3HistoryFeedback);
                }
                break;
            default:
                break;
        }
        ft.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction ft){
        //如果fragment不为空，就先隐藏起来
        if(fragmentTab1HistoryFeedback != null){
            ft.hide(fragmentTab1HistoryFeedback);
        }
        if(fragmentTab2HistoryFeedback != null){
            ft.hide(fragmentTab2HistoryFeedback);
        }
        if(fragmentTab3HistoryFeedback != null){
            ft.hide(fragmentTab3HistoryFeedback);
        }
    }
}
