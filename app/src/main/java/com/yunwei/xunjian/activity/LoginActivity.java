package com.yunwei.xunjian.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.yunwei.xunjian.util.Constants.LOGIN_URL;

public class LoginActivity extends BaseActivity {

    private SharedPreferences pref;
    private  SharedPreferences.Editor editor;
    private EditText editText_username, editText_password;
    private CheckBox checkBox_rememberPsd;
    private TextView textView_warn;
    private Button btn_load;
    private CheckBox checkBox_eye;

    private boolean isChecked = false;

    private static final int USER_NAME_WRONG = 1;
    private static final int USER_NAME_OK = 2;
    private static final int NET_ERROR = 3;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case USER_NAME_WRONG:
                    textView_warn.setText("用户名或密码错误！");
                    textView_warn.setVisibility(View.VISIBLE);
                    break;
                case USER_NAME_OK:
                    textView_warn.setVisibility(View.INVISIBLE);
                    break;
                case NET_ERROR:
                    textView_warn.setText("网络或服务器故障！");
                    textView_warn.setVisibility(View.VISIBLE);
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, true, R.color.white);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        editText_username = (EditText) findViewById(R.id.username);
        editText_password = (EditText) findViewById(R.id.password);
        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int size = editText_password.getText().toString().trim().length();
                if(size < 6){
                    btn_load.setEnabled(false);
                    btn_load.setBackgroundResource(R.drawable.round_corner_gray);
                }else{
                    btn_load.setEnabled(true);
                    btn_load.setBackgroundResource(R.drawable.round_corner_lan);
                }
            }
        });
        textView_warn = (TextView) findViewById(R.id.warn);
        textView_warn.setVisibility(View.INVISIBLE);

        checkBox_rememberPsd = findViewById(R.id.remember_psd);

        btn_load = (Button) findViewById(R.id.load);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String inputUser = editText_username.getText().toString();
                final String inputPwd = editText_password.getText().toString();
                final String URL = LOGIN_URL + "?name=" + inputUser + "&password=" + inputPwd;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(URL)
//                            .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();

                            if (!responseData.equals("")) {

                                editor = pref.edit();
                                if (checkBox_rememberPsd.isChecked()) {
                                    editor.putBoolean("remember_password", true);
                                    editor.putString("account", inputUser);
                                    editor.putString("password", inputPwd);
                                } else {
                                    editor.clear();
                                }
                                editor.apply();

                                JSONObject jsonData = new JSONObject(responseData);
                                String msge = jsonData.getString("msg");
                                if (msge.equals("处理成功")) {

                                    //String phone = inputUser;
                                    //String nickName = jsonData.getString("nickname");

                                    jsonData=new JSONObject(jsonData.get("extend").toString());
                                    String userName=jsonData.get("ORGANIZ_CODE").toString();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userName", userName);
                                    //bundle.putString("phone", phone);
                                    //bundle.putString("nowNickName", nickName);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                    Message msg = new Message();
                                    msg.what = USER_NAME_OK;
                                    handler.sendMessage(msg);
                                }else {
                                    Message msg = new Message();
                                    msg.what = USER_NAME_WRONG;
                                    handler.sendMessage(msg);
                                }
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("MyLogin", "run:用户名或密码错误" );
                            Message msg = new Message();
                            msg.what = USER_NAME_WRONG;
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("MyLogin", "run:网络故障" );
                            Message msg = new Message();
                            msg.what = NET_ERROR;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();

            }
        });

        checkBox_eye = (CheckBox) findViewById(R.id.eye);
        checkBox_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editText_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }else{
                    editText_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
                //设置光标在最后显示
                editText_password.setSelection(editText_password.getText().toString().trim().length());
            }
        });

        /*checkBox_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isChecked){
                    isChecked = true;
                }else{
                    isChecked = false;
                }
                Log.d("password", "onClick: " + isChecked);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isChecked){
                            //输入一个不可见的密码
                            imageView_eye.setImageResource(R.mipmap.eye0);
                            editText_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        }else{
                            //输入一个可见的密码
                            imageView_eye.setImageResource(R.mipmap.eye1);
                            editText_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        }
                        //设置光标在最后显示
                        editText_password.setSelection(editText_password.getText().toString().trim().length());
                    }
                });
            }
        });*/

        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            editText_username.setText(account);
            editText_password.setText(password);

            //设置光标位置
            editText_username.setSelection(account.trim().length());
        }
    }

}
