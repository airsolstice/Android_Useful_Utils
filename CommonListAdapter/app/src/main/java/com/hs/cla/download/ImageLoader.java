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
    //Ĭ����ʾ��ͼƬ
    private final int DEF_IMG = R.drawable.ic_launcher;


    public ImageLoader(Context context) {
        //�����ļ�����
        mFileCache = new FileCache(context);
        //�����̳߳�
        mService = Executors.newFixedThreadPool(5);
        //�����ڴ滺�棬�����ַ�ʽ������һ��ֱ��ʹ��LruCache���������
        //Google�ṩ���ڴ���ʱ������δʹ�õ�ѡ��
        //����һ������LinkedHashMap����Ҫ�ֶ��������ƣ����ң�LinkedHashMap���е���ɾ�������ӵ�Ч�ʸ���
        mLruMemCache = new LruMemoryCache();
        //url��View�ļ�ֵ�ԣ�����ͼƬ�������Ĳ����
        mViewCache = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }


    public void display(ImageView view, String url) {
        //��url��view�Ķ�Ӧֵ��������
        mViewCache.put(view, url);

        //����ڴ滺�����Ƿ����url��Ӧ��ͼƬ�����û�оͿ������̣߳�������ͼ
        if (mLruMemCache.get(url) != null) {
            view.setImageBitmap(mLruMemCache.get(url));
        } else {
            view.setImageResource(DEF_IMG);
            load(view, url);
        }

    }


    private void load(ImageView view, String url) {

        //����ͼƬ������̣߳���Ϊ�������ļ�������������������Ǻ�ʱ��
        ImageRunnable imageRunnable = new ImageRunnable(view, url);
        //����̵߳��̳߳���
        mService.submit(imageRunnable);
    }

    /**
     * ��ֹͼƬ��λ
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
     * ��ͼƬ������ɺ󣬸��²�������Ϊ����UI������UI�߳��У����ԣ���Ҫ�����²�������Runnable
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

            //���ͼƬ��Ϊ�գ�������ͼƬ����ӦView�ؼ���Ϊ��������Ĭ��ͼƬ��ʾ
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
            //�����ļ��������Ƿ����Ŀ��ͼƬ
            File file = mFileCache.getFile(url);
            if (reusedImageView(view, url)) {
                return;
            }
            Bitmap bitmap =
                    BitmapDecoder.decodeBitmapByFileStream(file, 100, 100);
            //��������ڣ���ѡ����������
            if (bitmap == null) {
                bitmap = loadByNetWork(url, file, view);
            }
            //��ŵ��ڴ滺����
            mLruMemCache.put(url, bitmap);
            if (reusedImageView(view, url)) {
                return;
            }

            //����UI��newһ��Runnable���󣬲��뵽Ui�߳���ȥ
            UpdateRunnable updateRunnable = new UpdateRunnable(view, url, bitmap);
            Activity activity = (Activity) view.getContext();
            activity.runOnUiThread(updateRunnable);

        }
    }

    /**
     * �������ز���
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
