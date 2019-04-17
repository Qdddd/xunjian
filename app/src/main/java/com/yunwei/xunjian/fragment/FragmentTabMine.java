package com.yunwei.xunjian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.activity.AboutActivity;
import com.yunwei.xunjian.activity.LoginActivity;
import com.yunwei.xunjian.activity.MyInfoActivity;
import com.yunwei.xunjian.activity.ServiceCenterActivity;
import com.yunwei.xunjian.activity.VersionActivity;
import com.yunwei.xunjian.util.HttpUtil;
import com.yunwei.xunjian.util.StatusBarUtil;
import com.yunwei.xunjian.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yunwei.xunjian.activity.MainActivity.userName;
import static com.yunwei.xunjian.util.Constants.MY_INFO;

public class FragmentTabMine extends Fragment implements View.OnClickListener {

    private TextView textView_name, textView_department;
    private ConstraintLayout layout_myInfo;
    private RelativeLayout layout_service, layout_version, layout_about, exit;

    private String nickName, userUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("", "onCreate: ");

        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(getActivity(), false, R.color.lan);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_mine,container,false);
        textView_name = (TextView) view.findViewById(R.id.name);
        textView_department = (TextView) view.findViewById(R.id.department);

        layout_myInfo = (ConstraintLayout) view.findViewById(R.id.myInfo);
        layout_myInfo.setOnClickListener(this);

/*
        imageView_experience = (ImageView) view.findViewById(R.id.experience);
        imageView_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ExperienceActivity.class);
                startActivity(intent);
            }
        });
*/
        layout_service = (RelativeLayout) view.findViewById(R.id.service);
        layout_service.setOnClickListener(this);

        layout_version = (RelativeLayout)view.findViewById(R.id.version);
        layout_version.setOnClickListener(this);

        layout_about = (RelativeLayout)view.findViewById(R.id.about);
        layout_about.setOnClickListener(this);

        exit = (RelativeLayout) view.findViewById(R.id.exit);
        exit.setOnClickListener(this);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d("", "onActivityCreated: ");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("", "onStart: ");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("", "onResume: ");
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void initView(){
        String URL = MY_INFO + "?userName=" + userName;
        HttpUtil.sendOkHttpRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getContext(), R.string.connection_fail, Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("responseData", "onResponse: " + responseData);

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    nickName = jsonObject.getString("nickname");
                    userUnit = jsonObject.getString("userunit");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_name.setText(nickName);
                        textView_department.setText(userUnit);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.myInfo:
                bundle.putString("userName", userName);
                intent.setClass(getActivity(), MyInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.service:
                bundle.putString("userName", userName);
                bundle.putString("nickName", nickName);
                intent.setClass(getActivity(), ServiceCenterActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.version:
                intent.setClass(getActivity(),VersionActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent.setClass(getActivity(),AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
