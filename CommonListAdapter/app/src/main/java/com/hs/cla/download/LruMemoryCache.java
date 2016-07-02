package com.hs.cla.download;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

/**
 * Created by Holy-Spirit on 2016/1/9.
 */
public class LruMemoryCache
{

    private LruCache<String,Bitmap> mLruCache;

    public LruMemoryCache(){

        int blockSize = (int) (Runtime.getRuntime().maxMemory()/8/1024);

        mLruCache = new LruCache<String,Bitmap>(blockSize){
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            protected int sizeOf(String key, Bitmap value)
            {
                return value.getByteCount()/1024;
            }
        };

    }



    public void put(String url,Bitmap bitmap){

        if(bitmap == null)
            return;

        mLruCache.put(url,bitmap);
        System.out.println("-->>size:"+mLruCache.size()+"max size:"+mLruCache.maxSize());
    }


    public Bitmap get(String url){
        return mLruCache.get(url);
    }

}
