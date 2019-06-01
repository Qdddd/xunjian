package com.yunwei.xunjian.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.ortiz.touchview.TouchImageView;
import com.yunwei.xunjian.R;
import com.yunwei.xunjian.util.FileUtil;
import com.yunwei.xunjian.util.StatusBarUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.yunwei.xunjian.util.Constants.GET_PIC_FAIL;
import static com.yunwei.xunjian.util.Constants.GET_PIC_SUCCESS;

public class ImageActivity extends BaseActivity {

    private TextView textView_exit;
    private TouchImageView imageView;

    private Uri imageUri;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case GET_PIC_FAIL:
                    //imageView.setImageResource(R.mipmap.load);
                   // ToastUtil.showToast(ImageActivity.this, String.valueOf(msg.obj), Toast.LENGTH_SHORT);
                    break;
                case GET_PIC_SUCCESS:
                    //通过message，拿到字节数组
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    if (Picture.length > 1000) {
                        //通过imageview，设置图片
                        imageView.setImageBitmap(bitmap);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //设置状态栏背景色和字体颜色
        StatusBarUtil.setStatusBarMode(this, false, R.color.black);

        imageUri = getIntent().getParcelableExtra("imageUri");

        textView_exit = (TextView)findViewById(R.id.exit);
        textView_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = (TouchImageView)findViewById(R.id.image);

        getLocalPicOrDownloadPic(imageUri);

    }

    private  void getLocalPicOrDownloadPic(Uri imageUri) {

        File localPicFile = new File(FileUtil.getAbsoluteImagePath(this, imageUri));
        if (localPicFile.exists()) {
            try {
                Bitmap myBitmap = FileUtil.getThumbnail(ImageActivity.this.getContentResolver(), imageUri, 1024);
                imageView.setImageBitmap(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final String picturePath = imageUri.toString();
            //1.创建一个okhttpclient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
            Request request = new Request.Builder()
                    .url(picturePath)
                    .build();
            //3.创建一个Call对象，参数是request对象，发送请求
            Call call = okHttpClient.newCall(request);
            //4.异步请求，请求加入调度
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = handler.obtainMessage();
                    message.obj = "取原上传图片失败";
                    message.what = GET_PIC_FAIL;
                    handler.sendMessage(message);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //得到从网上获取资源，转换成我们想要的类型
                    byte[] imageBytes = response.body().bytes();

                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    if(bitmap != null) {
                        //通过handler更新UI
                        Message message = handler.obtainMessage();
                        message.obj = imageBytes;
                        message.what = GET_PIC_SUCCESS;
                        handler.sendMessage(message);
                    }else{
                        Message message = handler.obtainMessage();
                        message.obj = "取原上传图片失败";
                        message.what = GET_PIC_FAIL;
                        handler.sendMessage(message);
                    }
                }
            });
        }
    }
}
