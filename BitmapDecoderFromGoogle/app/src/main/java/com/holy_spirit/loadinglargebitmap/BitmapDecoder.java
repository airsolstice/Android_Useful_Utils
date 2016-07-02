package com.holy_spirit.loadinglargebitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by Holy-Spirit on 2015/11/28.
 * BaseClass for compres pictures(Bitmap)
 */
public class BitmapDecoder {

    public static Bitmap decodeSampledBitmapByteArray(byte[] datas, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(datas, 0, datas.length, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
    }





    //    ÿ��streamֻ�ܶ�һ�Σ������Ҫ�����ߺͼ���Bitmap��
    //    ����Ҫ���Σ���ڶ��δ�Stream�ж�ȡ������Ϊ��
    //    �����ʹ��decodeStream����������Ҫ����һ��URL���βſ�

    public static Bitmap decodeSampledBitmapStream(InputStream inStream1, InputStream inStream2
            , int reqWidth, int reqHeight) {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inStream1, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(inStream2, null, options);
    }


    public static Bitmap decodeSampledBitmapFromResource(
            Resources res, int resId, int reqWidth, int reqHeight
    ) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

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
}
