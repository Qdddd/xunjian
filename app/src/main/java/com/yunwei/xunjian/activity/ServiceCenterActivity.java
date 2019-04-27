package com.yunwei.xunjian.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yunwei.xunjian.R;
import com.yunwei.xunjian.listener.ProgressListener;
import com.yunwei.xunjian.util.FileUtil;
import com.yunwei.xunjian.util.HttpUtil;
import com.yunwei.xunjian.util.StatusBarUtil;
import com.yunwei.xunjian.util.ToastUtil;
import com.yunwei.xunjian.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yunwei.xunjian.util.Constants.ADD_FEEDBACK;
import static com.yunwei.xunjian.util.Constants.ADD_FEEDBACK_WITH_PICTURE;
import static com.yunwei.xunjian.util.Constants.ALBUM_REQUEST_CODE;
import static com.yunwei.xunjian.util.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.yunwei.xunjian.util.Constants.CAMERA_REQUEST_CODE;
import static com.yunwei.xunjian.util.Constants.CROP_REQUEST_CODE;
import static com.yunwei.xunjian.util.Constants.WRITE_PERMISSION_REQUEST_CODE;

public class ServiceCenterActivity extends BaseActivity {

    private TextView textView_title, textView_telephone, textView_history_feedBack;
    private ImageView imageView_back, imageView_photo, imageView_camera;
    private EditText editText_feedBack;
    private Button btn_submit;

    private String userName, nickName;

    private File outputImage;
    private Uri imageUri = null;
    private Uri outputUri = null;

    private int isCropedFlag = 0; // 0:无图片， 1：无裁剪， 2：有裁剪

    private ConstraintLayout layout_progress;
    private TextView textView_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.lan);

        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        nickName = bundle.getString("nickName");

        textView_title = (TextView)findViewById(R.id.title);
        textView_title.setText("意见反馈");

        imageView_back = (ImageView)findViewById(R.id.back);
        imageView_back.setImageResource(R.mipmap.back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        textView_telephone = (TextView)findViewById(R.id.service_phone);
        textView_telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.callPhone(ServiceCenterActivity.this, textView_telephone.getText().toString());
            }
        });

        editText_feedBack = (EditText)findViewById(R.id.feedback);

        textView_history_feedBack = (TextView)findViewById(R.id.history_feedback);
        textView_history_feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceCenterActivity.this, HistoryFeedbackActivity.class);
                startActivity(intent);
            }
        });

        imageView_camera = (ImageView)findViewById(R.id.camera);
        imageView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        imageView_photo = (ImageView)findViewById(R.id.photo);
        imageView_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceCenterActivity.this, ImageActivity.class);
                if(isCropedFlag == 2){
                    intent.putExtra("imageUri", outputUri);
                }else if(isCropedFlag == 1){
                    intent.putExtra("imageUri", imageUri);
                }else{
                    return;
                }

                startActivity(intent);
            }
        });

        btn_submit = (Button)findViewById(R.id.submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText_feedBack.getText().toString().equals("")){
                    Toast.makeText(ServiceCenterActivity.this, "提交失败！反馈意见不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                File file = null;
                if(isCropedFlag == 2){
                    Log.d("outputUri", "onClick: " + FileUtil.getAbsoluteImagePath(ServiceCenterActivity.this, outputUri));
                    file = new File(FileUtil.getAbsoluteImagePath(ServiceCenterActivity.this, outputUri));
                    Log.d("outputImage", "onClick: " + outputImage.getAbsolutePath());
                }else if(isCropedFlag == 1){
                    Log.d("outputUri", "onClick: " + FileUtil.getAbsoluteImagePath(ServiceCenterActivity.this, imageUri));
                    file = new File(FileUtil.getAbsoluteImagePath(ServiceCenterActivity.this, imageUri));
                    Log.d("outputImage", "onClick: " + outputImage.getAbsolutePath());
                }
                    submitFeedBackWithPicture(editText_feedBack.getText().toString(), file);
            }
        });

        layout_progress = (ConstraintLayout)findViewById(R.id.progressLayout);
        textView_progress = (TextView)findViewById(R.id.progressText);

        layout_progress.setVisibility(View.GONE);
    }

//    private void submitFeedback(String feedbackContent){
//        String URL = ADD_FEEDBACK + "?feedbackPerson=" + userName + "&feedbackContent=" + feedbackContent;
//        HttpUtil.sendOkHttpRequest(URL, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(ServiceCenterActivity.this, "问题反馈失败！网络或服务器故障！", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String responseData = response.body().string();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (responseData.equals("1")) {
//                            editText_feedBack.setText("");
//                            Toast.makeText(ServiceCenterActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ServiceCenterActivity.this, "提交失败！请重新提交问题！", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//
//    }

    private void submitFeedBackWithPicture(String feedbackContent, File imgFile) {
        Map<String, String> myMap = new HashMap<>();
        myMap.put("feedbackPerson", userName);
        myMap.put("feedbackContent", feedbackContent);

        File[] files = {imgFile, null};

        layout_progress.setVisibility(View.VISIBLE);

        String URL = ADD_FEEDBACK_WITH_PICTURE;
        HttpUtil.postFile3(URL, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                Log.d("Transformer", "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
                int progress = (int) (currentBytes * 100 / contentLength);
                textView_progress.setText(progress + "%");
            }
        }, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( ServiceCenterActivity.this,  "提交失败，请重新提交！", Toast.LENGTH_SHORT).show();
                        layout_progress.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    result = jsonObject.get("code").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("AcceptActivity", "result===" + result);
                final String finalResult = result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout_progress.setVisibility(View.GONE);
                        if (finalResult.equals("100")) {
                            editText_feedBack.setText("");
                            imageView_photo.setImageResource(R.mipmap.load);
                            Toast.makeText( ServiceCenterActivity.this,  "提交成功！", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText( ServiceCenterActivity.this,  "提交失败，请重新提交！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, files, myMap);
    }

    private void showDialog(){
        View view = getLayoutInflater().inflate( R.layout.dialog_camera, null);
        final TextView textView_camera = (TextView)view.findViewById(R.id.camera);
        final TextView textView_album = (TextView)view.findViewById(R.id.album);
        final TextView textView_cancel = (TextView)view.findViewById(R.id.cancel);

        textView_camera.setText("拍照");
        textView_album.setText("相册");
        textView_cancel.setText("取消");

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        //dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(null);

        textView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                //申请相机权限和读写存储权限
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(ServiceCenterActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
                }else {
                    //调用相机
                    initFile();
                    openCamera();
                }
            }
        });

        textView_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(ServiceCenterActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST_CODE);
                }else {
                    initFile();
                    openAlbum();
                }
            }
        });

        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case CAMERA_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            ToastUtil.showToast(getApplicationContext(), R.string.agree_with_permissions, Toast.LENGTH_SHORT);
                            return;
                        }
                    }
                    //调用相机
                    initFile();
                    openCamera();
                }else{
                    ToastUtil.showToast(getApplicationContext(), R.string.unknown_error, Toast.LENGTH_SHORT);
                    return;
                }
                break;
            case WRITE_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            ToastUtil.showToast(getApplicationContext(), R.string.agree_with_permissions, Toast.LENGTH_SHORT);
                            return;
                        }
                    }
                    //调用相册
                    initFile();
                    openAlbum();
                }else{
                    ToastUtil.showToast(getApplicationContext(), R.string.unknown_error, Toast.LENGTH_SHORT);
                    return;
                }
                break;
            default:
                break;
        }
    }

    private void initFile(){
        //如果状态不是mounted，无法读写
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtil.showToast(getApplicationContext(), "请插入内存卡", Toast.LENGTH_SHORT);
            return;
        }

        // 创建File对象，用于存储拍照后的图片,保存到系统相册
        outputImage = new File(getExternalFilesDir(null).getPath(), System.currentTimeMillis() + ".jpg");
        Log.d("openCamera", outputImage.getAbsolutePath());

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            outputUri = Uri.fromFile(outputImage);
        } else {
            outputUri = FileProvider.getUriForFile(ServiceCenterActivity.this, "com.zzdy.xiansun.fileprovider", outputImage);
        }
    }

    private void openCamera() {

        // 启动相机程序
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void openAlbum(){
        Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        //Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        albumIntent.setType("image/*");
        startActivityForResult(albumIntent, ALBUM_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri imageUri, Uri outputUri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 回调接口
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            // 调用相机后返回
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    /*try {
                        Bitmap myBitmap = getThumbnail(ServiceCenterActivity.this.getContentResolver(), outputUri, 1024);
                        imageView_photo.setImageBitmap(myBitmap);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //发送广播通知更新数据库, 把照片加入到系统图库
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outputUri));
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    imageUri = outputUri;
                    cropPhoto(imageUri, outputUri, 3120, 4160);
                }
                break;
            //调用相册后返回
            case ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    imageUri = intent.getData();

                    /*try {
                        Bitmap myBitmap = getThumbnail(ServiceCenterActivity.this.getContentResolver(), imageUri, 1024);
                        imageView_photo.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    cropPhoto(imageUri, outputUri, 3120, 4160);
                }
                break;
            //调用剪裁后返回
            case CROP_REQUEST_CODE:
                try {
                    Bitmap myBitmap = null;
                    if(resultCode == RESULT_OK){
                        //图片有裁剪
                        isCropedFlag = 2;
                        myBitmap = FileUtil.getThumbnail(ServiceCenterActivity.this.getContentResolver(), outputUri, 1024);
                    }else{
                        //图片未裁剪
                        isCropedFlag = 1;
                        myBitmap = FileUtil.getThumbnail(ServiceCenterActivity.this.getContentResolver(), imageUri, 1024);
                        if(outputImage.exists() && outputImage.length() == 0) {
                            outputImage.delete();
                        }
                    }
                    imageView_photo.setImageBitmap(myBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //发送广播通知更新数据库, 把照片加入到系统图库
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
                    }
                }).start();
                break;
        }
    }

    /**
     * 保存图片到本地缓存
     *
     * @param uri
     * @param bmp
     * @return
     */
    public String saveImage(Uri uri, Bitmap bmp) {
        try {
            File file = new File(String.valueOf(uri));
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
