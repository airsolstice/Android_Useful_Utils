package com.hs.mtli;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Holy-Spirit on 2015/12/31.
 *
 *多线程下载文件
 *  默认线程数为3个线程
 *      这个类可以直接引用，只要提供一个Handler进行UI线程和下载线程之间的通信即可
 *
 */
public class MultiDownLoad
{

    /* 线程池对象*/
    private Executor mThreaPool = Executors.newFixedThreadPool(3);
    /*UI更新回调接口*/
    private Handler mHandler;
    /*任务下载的的线程数，默认为3*/
    private int mThreadCount = 3;


    /**
     *
     * @param handler
     *          需要重写接口，接收任务线程完成时的通知
     *           和任务头线程发过来的下载文件存放的位置
     * @param count
     *            任务下载的线程数
     */
    public MultiDownLoad(Handler handler,int count){
        this.mHandler = handler;
        this.mThreadCount = count;
    }


    /**
     * 任务下载
     * @param url
     */
    public void downLoadFile(String url){

        try
        {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);

            /*获取每个线程所要的下载的任务大小*/
            int len =  conn.getContentLength();
            int block = len/mThreadCount;

            /*在SD卡中建立文件并获取路径*/
            String fileName = getFileName(url);
            File parent = Environment.getExternalStorageDirectory();
            File downLoadFile =  new File(parent,fileName);
            String filePath = downLoadFile.getAbsolutePath();

            System.out.println("path:"+filePath);

            /*发送文件的路径给UI线程，一般下载完后，我们都需要对其进行引用*/
            Message msg = new Message();
            msg.what = 0;
            msg.obj = filePath;
            mHandler.sendMessage(msg);

            for(int i = 0;i<mThreadCount;i++){
                long start = block*i;
                long end = block*(i+1)-1;

                if(i == (mThreadCount-1))
                    end = len;

                /*执行任务线程并将其放入线程池*/
                DownLoadRunnable runnable = new DownLoadRunnable(mHandler,url
                        ,filePath,start,end);
                mThreaPool.execute(runnable);
            }

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 获取url中的末尾的文件名
     * @param url
     * @return
     */
    private String getFileName(String url){
        return url.substring(url.lastIndexOf("/")+1);
    }




    /**
     * 分任务下载线程接口
     */
    static class DownLoadRunnable implements Runnable
    {

        private String url;
        private String fileName;
        private long start;
        private long  end;
        private Handler mHandler;

        public DownLoadRunnable(Handler handler,String url,String fileName,long start,long end){
            this.mHandler = handler;
            this.url = url;
            this.fileName = fileName;
            this.start = start;
            this.end = end;
        }


        @Override
        public void run(){

            try
            {
                URL httpUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);

                /*设置文件下载的起始位置，这是分线程下载的关键*/
                conn.setRequestProperty("Range", "byte=" + start + "-" + end);
                RandomAccessFile accessFile = new RandomAccessFile(new File(fileName),"rwd");
                accessFile.seek(start);
                InputStream inutStream = conn.getInputStream();

                byte[] buffer = new byte[1024*4];
                int len;

                while((len = inutStream.read(buffer)) != -1){
                    accessFile.write(buffer,0,len);
                    System.out.println("loading");
                }

                /*每个下载线程下载完任务，就通知UI*/
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);

                if(accessFile != null){
                    accessFile.close();
                }
                if(inutStream != null){
                    inutStream.close();
                }


            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }


}
