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
 *���߳������ļ�
 *  Ĭ���߳���Ϊ3���߳�
 *      ��������ֱ�����ã�ֻҪ�ṩһ��Handler����UI�̺߳������߳�֮���ͨ�ż���
 *
 */
public class MultiDownLoad
{

    /* �̳߳ض���*/
    private Executor mThreaPool = Executors.newFixedThreadPool(3);
    /*UI���»ص��ӿ�*/
    private Handler mHandler;
    /*�������صĵ��߳�����Ĭ��Ϊ3*/
    private int mThreadCount = 3;


    /**
     *
     * @param handler
     *          ��Ҫ��д�ӿڣ����������߳����ʱ��֪ͨ
     *           ������ͷ�̷߳������������ļ���ŵ�λ��
     * @param count
     *            �������ص��߳���
     */
    public MultiDownLoad(Handler handler,int count){
        this.mHandler = handler;
        this.mThreadCount = count;
    }


    /**
     * ��������
     * @param url
     */
    public void downLoadFile(String url){

        try
        {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);

            /*��ȡÿ���߳���Ҫ�����ص������С*/
            int len =  conn.getContentLength();
            int block = len/mThreadCount;

            /*��SD���н����ļ�����ȡ·��*/
            String fileName = getFileName(url);
            File parent = Environment.getExternalStorageDirectory();
            File downLoadFile =  new File(parent,fileName);
            String filePath = downLoadFile.getAbsolutePath();

            System.out.println("path:"+filePath);

            /*�����ļ���·����UI�̣߳�һ������������Ƕ���Ҫ�����������*/
            Message msg = new Message();
            msg.what = 0;
            msg.obj = filePath;
            mHandler.sendMessage(msg);

            for(int i = 0;i<mThreadCount;i++){
                long start = block*i;
                long end = block*(i+1)-1;

                if(i == (mThreadCount-1))
                    end = len;

                /*ִ�������̲߳���������̳߳�*/
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
     * ��ȡurl�е�ĩβ���ļ���
     * @param url
     * @return
     */
    private String getFileName(String url){
        return url.substring(url.lastIndexOf("/")+1);
    }




    /**
     * �����������߳̽ӿ�
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

                /*�����ļ����ص���ʼλ�ã����Ƿ��߳����صĹؼ�*/
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

                /*ÿ�������߳����������񣬾�֪ͨUI*/
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
