package com.yunwei.xunjian.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    /**
     * 将图片压缩转为缩略图
     * @param cr
     * @param uri
     * @param size
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Bitmap getThumbnail(ContentResolver cr, Uri uri, int size) throws FileNotFoundException, IOException {
        InputStream input = cr.openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
        double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither=true;//optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = cr.openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    /**
     * 从URI获取本地路径
     *
     * @param activity
     * @param contentUri
     * @return
     */
    public static  String getAbsoluteImagePath(Activity activity, Uri contentUri) {

        //如果是多媒体文件，在android开机的时候回去扫描，然后把路径添加到数据库中。
        //由打印的contentUri可以看到：2种结构。正常的是：content://那么这种就要去数据库读取path。
        //另外一种是Uri是 file:///那么这种是 Uri.fromFile(File file);得到的
        System.out.println(contentUri);

        String[] projection = { MediaStore.Images.Media.DATA };
        String urlpath;
        CursorLoader loader = new CursorLoader(activity,contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        try
        {
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            urlpath =cursor.getString(column_index);
            //如果是正常的查询到数据库。然后返回结构
            return urlpath;
        }
        catch (Exception e)
        {

            e.printStackTrace();
            // TODO: handle exception
        }finally{
            if(cursor != null){
                cursor.close();
            }
        }

        //如果是文件。Uri.fromFile(File file)生成的uri。那么下面这个方法可以得到结果
        urlpath = contentUri.getPath();
        return urlpath;
    }
}
