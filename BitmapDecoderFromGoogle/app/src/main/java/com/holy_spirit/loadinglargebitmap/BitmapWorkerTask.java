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
        //��ȡ�������
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        //�������Ϊ�գ����ж���Ҫ���ص�ͼ�����Դ��־�Ƿ�һ��
        //�����һ�£�������ǰ���������´���
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
        //�ж�ImageView�Ƿ�Ϊ�գ�����Ϊ��
        // ���ж�ImageView����ͼƬ��Դ�����Ƿ��Ԥ����һ��
        //һ�µĻ��ͻ�ȡAsyncDrawable�����е�����
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
