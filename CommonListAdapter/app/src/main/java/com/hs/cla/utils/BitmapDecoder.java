package com.hs.cla.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Holy-Spirit on 2015/11/28.
 * BaseClass for compres pictures(Bitmap)
 */
public class BitmapDecoder {


    /*计算降低分辨率的比例*/
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight &&
                    (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    /* 拉伸位图的宽高，使能够自适应View的大小*/
    public static Bitmap zoomBitmap(Bitmap srcBitmap, int tagWidth, int tagHeight) {


        final int width = srcBitmap.getWidth();
        final int height = srcBitmap.getHeight();
        float scaleWidth = ((float) tagWidth) / width;
        float scaleHeight = ((float) tagHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
    }


    public static Bitmap decodeBitmapByResource(
            Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static Bitmap decodeBitmapByFileStream(File file, int reqWidth, int reqHeight) {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(new FileInputStream(file), null, options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap decodeBitmapByByteArray(byte[] datas, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
    }


    public static Bitmap decodeBitmapByStream(InputStream inStream1, InputStream inStream2
            , int reqWidth, int reqHeight) {
        //每个stream只能读一次，如果需要计算宽高和加载Bitmap，
        //则需要两次，则第二次从Stream中读取的数据为空
        //因此想使用decodeStream方法，则需要访问一个URL两次才可

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inStream1, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(inStream2, null, options);
    }


}
