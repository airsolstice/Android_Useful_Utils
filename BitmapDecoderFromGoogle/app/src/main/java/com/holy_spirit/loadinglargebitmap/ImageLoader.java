package com.holy_spirit.loadinglargebitmap;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Holy-Spirit on 2016/1/2.
 */
public class ImageLoader {

    private Context mContext;
    private Resources mRes;
    private LruCache<String, Bitmap> mImgLruCache;


    public ImageLoader(Context context) {
        this.mContext = context;
        this.mRes = context.getResources();
        initLruCache();
    }


    private void initLruCache() {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mImgLruCache = new LruCache<String, Bitmap>(cacheSize) {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };


    }

    private void addBitmapToMemoryCache(String key, Bitmap value) {
        if (value != null) {
            mImgLruCache.put(key, value);
        } else {
            System.out.println("-->>cache faild:" + key);
        }

    }

    private Bitmap getBitmapFromMemoryCache(String key) {
        return mImgLruCache.get(key);
    }

    public Bitmap downloadBitmap(ImageView view,String url) {

        Bitmap bitmap = getBitmapFromMemoryCache(url);

        if (bitmap == null) {
            try {

                DownloadAsync load = new DownloadAsync(view);
                bitmap = load.execute(url).get();
                addBitmapToMemoryCache(url, bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("-->>already exist");
        }

        return bitmap;
    }


    private class DownloadAsync extends AsyncTask<String, Integer, Bitmap> {

        private ImageView view;


        public DownloadAsync(ImageView view) {
            this.view = view;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                System.out.println("-->>download success");
            }
            super.onPostExecute(bitmap);
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = null;
            try {
                URL httpUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                InputStream inStream = conn.getInputStream();
                ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer)) != -1) {
                    byteOutStream.write(buffer, 0, len);
                }

                byte[] res = byteOutStream.toByteArray();
                bitmap = BitmapDecoder.decodeSampledBitmapByteArray(res, 100, 100);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }


}
