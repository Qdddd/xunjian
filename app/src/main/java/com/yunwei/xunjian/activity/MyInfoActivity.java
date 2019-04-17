package com.yunwei.xunjian.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.HttpUtil;
import com.yunwei.xunjian.util.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yunwei.xunjian.util.Constants.MODIFY_IDC_AND_PHONE;
import static com.yunwei.xunjian.util.Constants.MODIFY_PASSWORD;
import static com.yunwei.xunjian.util.Constants.MY_INFO;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView textView_title;
    private TextView textView_name, textView_password;
    private TextView textView_idc, textView_telephone;
    private ImageView imageView_back;
    private ImageView imageView_edit_name, imageView_edit_idc, imageView_edit_phone, imageView_edit_password;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);

        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");

        textView_title = (TextView)findViewById(R.id.title);
        textView_title.setText("个人信息");

        imageView_back = (ImageView)findViewById(R.id.back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textView_name = (TextView)findViewById(R.id.name);
        textView_idc = (TextView) findViewById(R.id.idc);
        textView_telephone = (TextView) findViewById(R.id.phone);
        textView_password = (TextView)findViewById(R.id.password);

        imageView_edit_idc = (ImageView)findViewById(R.id.idc_edit);
        imageView_edit_idc.setOnClickListener(this);
        imageView_edit_phone = (ImageView)findViewById(R.id.phone_edit);
        imageView_edit_phone.setOnClickListener(this);
        imageView_edit_password = (ImageView)findViewById(R.id.password_edit);
        imageView_edit_password.setOnClickListener(this);

        initView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("", "onResume: ");
    }

    private void initView(){
        String URL = MY_INFO + "?userName=" + userName;
        HttpUtil.sendOkHttpRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyInfoActivity.this, "网络或服务器故障！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("responseData", "onResponse: " + responseData);

                try {
                    JSONObject jsonObject = new JSONObject(responseData);

                    final String name = jsonObject.getString("nickname");
                    final String idcNumber = jsonObject.getString("idcnumber");
                    final String phone = jsonObject.getString("phone");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView_name.setText(name);
                            textView_idc.setText(idcNumber);
                            textView_telephone.setText(phone);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.idc_edit:
                showDialog(MyInfoActivity.this, "身份证号");
                break;
            case R.id.phone_edit:
                showDialog(MyInfoActivity.this, "手机号码");
                break;
            case R.id.password_edit:
                showDialogWithPassword(MyInfoActivity.this, "密码");
                break;
        }
    }

    private void showDialog(Context context, final String type){
        View view = getLayoutInflater().inflate( R.layout.dialog_2, null);
        final TextView textView = (TextView)view.findViewById(R.id.text);
        final EditText editText = (EditText)view.findViewById(R.id.edit);
        final TextView cancel = (TextView)view.findViewById(R.id.cancel);
        final TextView save = (TextView)view.findViewById(R.id.save);

        textView.setText("修改" + type);
        editText.setHint("请输入" + type);

        //此处相当于设置xml中的digits属性
        if(type.equals("身份证号")){
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789X"));
        }else{
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getText().toString().equals("")){
                    Toast.makeText(MyInfoActivity.this,"输入" + type + "为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type.equals("身份证号")) {
                    if(editText.getText().toString().length() != 18){
                        Toast.makeText(MyInfoActivity.this,"输入" + type + "位数不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    if(editText.getText().toString().length() != 11){
                        Toast.makeText(MyInfoActivity.this,"输入" + type + "位数不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                saveData(type, editText.getText().toString());

                dialog.dismiss();
            }
        });
    }

    private void saveData(String type, String value){
        if(type.equals("身份证号")){
            modifyInfo(userName, value, textView_telephone.getText().toString());
        }else{
            modifyInfo(userName, textView_idc.getText().toString(), value);
        }
    }

    private void showDialogWithPassword(Context context, final String type){
        View view = getLayoutInflater().inflate( R.layout.dialog_3, null);
        final TextView textView = (TextView)view.findViewById(R.id.text);
        final EditText editText_old = (EditText)view.findViewById(R.id.old_psd_edit);
        final EditText editText_new = (EditText)view.findViewById(R.id.new_psd_edit);
        final EditText editText_new2 = (EditText)view.findViewById(R.id.new_psd1_edit);
        final TextView cancel = (TextView)view.findViewById(R.id.cancel);
        final TextView save = (TextView)view.findViewById(R.id.save);

        textView.setText("修改" + type);
        editText_old.setHint("请输入原密码");
        editText_new.setHint("请输入新密码");
        editText_new2.setHint("请再次输入新密码");

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPsd = editText_old.getText().toString();
                String newPsd1 = editText_new.getText().toString();
                String newPsd2 = editText_new2.getText().toString();

                if(oldPsd.equals("") || newPsd1.equals("") || newPsd2.equals("")){
                    Toast.makeText(MyInfoActivity.this,"原密码或新密码为空，请重新输入！", Toast.LENGTH_SHORT).show();
                    editText_old.setText("");
                    editText_new.setText("");
                    editText_new2.setText("");
                    return;
                }
                if(oldPsd.length() < 6 || newPsd1.length() < 6 || newPsd2.length() < 6){
                    Toast.makeText(MyInfoActivity.this,"密码位数不少于6位，请重新输入！", Toast.LENGTH_SHORT).show();
                    editText_old.setText("");
                    editText_new.setText("");
                    editText_new2.setText("");
                    return;
                }
                if(!newPsd1.equals(newPsd2)){
                    Toast.makeText(MyInfoActivity.this,"两次输入的新密码不同，请重新输入！", Toast.LENGTH_SHORT).show();
                    editText_old.setText("");
                    editText_new.setText("");
                    editText_new2.setText("");
                    return;
                }

                saveDataWithPassword(oldPsd, newPsd1);

                dialog.dismiss();
            }
        });
    }

    private void saveDataWithPassword(final String oldPsd, final String newPsd){
        String URL = MODIFY_PASSWORD + "?operator=" + userName + "&oldPassword=" + oldPsd + "&newPassword=" + newPsd;

        HttpUtil.sendOkHttpRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyInfoActivity.this,"密码修改失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("response", "onResponse: " + response);
                Log.d("responseData", "onResponse: " + responseData);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("0")) {
                            Toast.makeText(MyInfoActivity.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyInfoActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void modifyInfo(String userName, final String idcNumber, final String telephone){
        String URL = MODIFY_IDC_AND_PHONE + "?realUserName=" +userName + "&newIdcNo=" + idcNumber + "&newPhone=" + telephone;
        HttpUtil.sendOkHttpRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyInfoActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseData.equals("0")){
                            Toast.makeText(MyInfoActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                        }else{
                            textView_idc.setText(idcNumber);
                            textView_telephone.setText(telephone);

                            Toast.makeText(MyInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
