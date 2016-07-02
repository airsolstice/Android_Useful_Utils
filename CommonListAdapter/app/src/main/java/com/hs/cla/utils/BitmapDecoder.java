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


    /*���㽵�ͷֱ��ʵı���*/
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


    /* ����λͼ�Ŀ�ߣ�ʹ�ܹ�����ӦView�Ĵ�С*/
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
        //ÿ��streamֻ�ܶ�һ�Σ������Ҫ�����ߺͼ���Bitmap��
        //����Ҫ���Σ���ڶ��δ�Stream�ж�ȡ������Ϊ��
        //�����ʹ��decodeStream����������Ҫ����һ��URL���βſ�

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inStream1, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(inStream2, null, options);
    }


}
