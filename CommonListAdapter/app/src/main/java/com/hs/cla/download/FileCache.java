package com.hs.cla.download;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Holy-Spirit on 2016/1/8.
 */
public class FileCache {

    private File mCacheDir = null;


    public FileCache(Context context) {


        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            mCacheDir = Environment.getExternalStorageDirectory();
        } else {
            mCacheDir = context.getCacheDir();
        }

        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }

    }


    public File getFile(String url) {
        String fileName = String.valueOf(url.hashCode());
        System.out.println("--->>" + fileName);
        return new File(mCacheDir, fileName);
    }


    public void clear() {

        File[] files = mCacheDir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            file.delete();
        }

    }


}
