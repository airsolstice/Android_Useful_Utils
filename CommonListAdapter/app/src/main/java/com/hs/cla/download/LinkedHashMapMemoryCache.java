package com.hs.cla.download;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Holy-Spirit on 2016/1/8.
 */
public class LinkedHashMapMemoryCache
{
    private Map<String,Bitmap> mMemCache = Collections.synchronizedMap(new LinkedHashMap<String,
            Bitmap>(10,1.5f,true));

    private long mMaxSize = 1000000;

    private long mMemSize = 0;

    public LinkedHashMapMemoryCache(){
       mMaxSize =  Runtime.getRuntime().maxMemory()/4;
    }



    public Bitmap get(String url){

       Bitmap bitmap =  mMemCache.get(url);
        if(bitmap != null){
            return bitmap;
        }
        return null;
    }


    public void put(String url,Bitmap bitmap){

        if(mMemCache.containsKey(url)){
            mMemSize -= getSizeInByte(mMemCache.get(url));
        }

        mMemCache.put(url,bitmap);
        mMemSize += getSizeInByte(bitmap);
        checkMemLimit();
    }


    public void clear(){
        mMemCache.clear();;
    }

    private void checkMemLimit()
    {

        if(mMemSize > mMaxSize){

            Iterator<Map.Entry<String,Bitmap>> iterator = mMemCache.entrySet().iterator();

            while(iterator.hasNext()){
                Map.Entry<String,Bitmap> entry = iterator.next();
                mMemSize -= getSizeInByte(entry.getValue());
                mMemCache.remove(entry.getKey());

                if(mMemSize <= mMaxSize){
                    break;
                }


            }
        }

    }

    private long getSizeInByte(Bitmap bitmap)
    {
        if(bitmap == null){
            return 0;
        }

        return bitmap.getRowBytes()*bitmap.getHeight();
    }

}
