package com.holy_spirit.loadinglargebitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

/**
 * Created by Holy-Spirit on 2015/11/28.
 * AsyncTask for Bitmap
 */
public class BitmapWorkerTask extends AsyncTask<Integer,Void ,Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    public int data = 0;
    private Context mContext = null;
    public BitmapWorkerTask(Context context,ImageView imageView) {
        this.mContext = context;
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        return BitmapDecoder.decodeSampledBitmapFromResource
                (mContext.getResources(), data, 100, 100);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask =
                    getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        //获取任务对象
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        //任务对象不为空，则判断所要加载的图像的资源标志是否一致
        //如果不一致，则丢弃以前的任务，重新创建
        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;

            if (bitmapData == 0 || bitmapData != data) {

                bitmapWorkerTask.cancel(true);
            } else {

                return false;
            }
        }

        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        //判断ImageView是否为空，若不为空
        // 则判断ImageView所绑定图片资源类型是否和预定的一致
        //一致的话就获取AsyncDrawable对象中的任务
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

}
