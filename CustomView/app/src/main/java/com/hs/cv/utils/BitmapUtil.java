package com.hs.cv.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;


/**
 * 图片压缩工具，把图片的分辨率与手机设备相适配
 *
 * @author Holy-Spirit
 */

public class BitmapUtil
{

    public BitmapUtil()
    {

    }

    public static Bitmap decodeSampleBitmapFromResource(Resources res,
                                                        int resId, int reqWidth, int reqHeight)
    {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static void recycleBitmap(View view)
    {
        BitmapDrawable mBitmap = (BitmapDrawable) view.getBackground();
        if (mBitmap != null)
        {
            System.out.println("-->>recycle");
            view.setBackgroundResource(0);
            mBitmap.setCallback(null);
            mBitmap.getBitmap().recycle();
        }

    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
