package com.hs.mtli;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends Activity {

    private TextView mTxt;
    private ImageView mImg;
    private Button mBtn;
    private Handler mHandler;
    private int count = 0;
    private String mFilePath;
    private MultiDownLoad load;
    private String url = "http://abv.cn/music/光辉岁月.mp3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxt = (TextView) findViewById(R.id.text_view);
        mImg = (ImageView) findViewById(R.id.image_view);
        mBtn = (Button) findViewById(R.id.btn);


        /**
         * 子线程下载任务完成接口回调更新UI
         */

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 0) {
                    mFilePath = msg.obj.toString();
                } else if (msg.what == 1) {
                    System.out.println("finish a subtask..");
                    count++;
                }
                if (count == 3) {
                    mTxt.setText("download sucess");
                  /*  Bitmap bitmap = BitmapFactory.decodeFile(mFilePath);
                    mImg.setImageBitmap(bitmap);*/
                    System.out.println("filePath:" + mFilePath);
                }


            }
        };


        /**
         * 按钮点击事件
         * 启动一个线程访问网络获取图片信息，并开启线程池
         */
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread() {
                    @Override
                    public void run() {
                        load = new MultiDownLoad(mHandler, 3);

                        String fileName = url.substring(url.lastIndexOf("/") + 1);

                        try {
                            fileName = URLEncoder.encode(fileName, "UTF-8");
                            url = url.substring(0, url.lastIndexOf("/") + 1) + fileName;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        load.downLoadFile(url);

                    }
                }.start();

                mTxt.setText("start loading");

            }
        });


    }


}
