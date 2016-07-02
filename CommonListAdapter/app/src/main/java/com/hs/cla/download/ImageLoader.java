package com.hs.cla.download;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hs.cla.R;
import com.hs.cla.utils.BitmapDecoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Holy-Spirit on 2016/1/2.
 */
public class ImageLoader {

    private FileCache mFileCache;
    private ExecutorService mService;
    private LruMemoryCache mLruMemCache;
    private Map<ImageView, String> mViewCache;
    //默认显示的图片
    private final int DEF_IMG = R.drawable.ic_launcher;


    public ImageLoader(Context context) {
        //创建文件缓存
        mFileCache = new FileCache(context);
        //创建线程池
        mService = Executors.newFixedThreadPool(5);
        //创建内存缓存，有两种方式创建，一种直接使用LruCache缓存这个类
        //Google提供了内存上时清楚最久未使用的选项
        //而另一种是用LinkedHashMap，需要手动增加限制，并且，LinkedHashMap的有点是删除和增加的效率更高
        mLruMemCache = new LruMemoryCache();
        //url和View的键值对，用来图片和容器的差错检测
        mViewCache = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }


    public void display(ImageView view, String url) {
        //将url和view的对应值放入容器
        mViewCache.put(view, url);

        //检测内存缓存中是否存在url对应的图片，如果没有就开启新线程，有则上图
        if (mLruMemCache.get(url) != null) {
            view.setImageBitmap(mLruMemCache.get(url));
        } else {
            view.setImageResource(DEF_IMG);
            load(view, url);
        }

    }


    private void load(ImageView view, String url) {

        //创建图片处理的线程，因为无论是文件操作还是网络操作都是耗时的
        ImageRunnable imageRunnable = new ImageRunnable(view, url);
        //添加线程到线程池中
        mService.submit(imageRunnable);
    }

    /**
     * 防止图片错位
     *
     * @param view
     * @param url
     * @return
     */

    private boolean reusedImageView(ImageView view, String url) {

        String tag = mViewCache.get(view);
        if (tag == null && !tag.equals(url)) {
            return true;
        }
        return false;
    }


    /**
     * 当图片下载完成后，更新操作，因为更新UI必须在UI线程中，所以，需要将更新操作另起Runnable
     */
    class UpdateRunnable implements Runnable {


        private ImageView view;
        private String url;
        private Bitmap bitmap;


        public UpdateRunnable(ImageView view, String url, Bitmap bitmap) {
            this.view = view;
            this.url = url;
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            if (reusedImageView(view, url)) {
                return;
            }

            //如果图片不为空，则拉伸图片，适应View控件，为空则设置默认图片显示
            if (bitmap != null) {
                bitmap = BitmapDecoder.zoomBitmap(bitmap, view.getWidth(), view.getHeight());
                view.setImageBitmap(bitmap);

            } else {
                view.setImageResource(DEF_IMG);
            }

            view.setAdjustViewBounds(true);
        }
    }


    class ImageRunnable implements Runnable {

        private String url;
        private ImageView view;

        public ImageRunnable(ImageView view, String url) {
            this.url = url;
            this.view = view;
        }

        @Override
        public void run() {
            //查找文件缓存中是否存在目的图片
            File file = mFileCache.getFile(url);
            if (reusedImageView(view, url)) {
                return;
            }
            Bitmap bitmap =
                    BitmapDecoder.decodeBitmapByFileStream(file, 100, 100);
            //如果不存在，则选择网络下载
            if (bitmap == null) {
                bitmap = loadByNetWork(url, file, view);
            }
            //存放到内存缓存中
            mLruMemCache.put(url, bitmap);
            if (reusedImageView(view, url)) {
                return;
            }

            //更新UI，new一个Runnable对象，插入到Ui线程中去
            UpdateRunnable updateRunnable = new UpdateRunnable(view, url, bitmap);
            Activity activity = (Activity) view.getContext();
            activity.runOnUiThread(updateRunnable);

        }
    }

    /**
     * 网络下载操作
     *
     * @param url
     * @param file
     * @param veiw
     * @return
     */

    private Bitmap  loadByNetWork(String url, File file, ImageView veiw) {

        try {
            URL imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            InputStream inStream = conn.getInputStream();
            FileOutputStream fileOutStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;

            while ((len = inStream.read(buffer)) != -1) {
                fileOutStream.write(buffer, 0, len);
            }
            fileOutStream.close();
            inStream.close();

            return BitmapDecoder.decodeBitmapByFileStream(file, 100, 100);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


}
