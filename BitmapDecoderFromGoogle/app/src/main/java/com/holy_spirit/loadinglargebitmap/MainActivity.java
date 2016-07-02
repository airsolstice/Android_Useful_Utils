package com.holy_spirit.loadinglargebitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;


public class MainActivity extends Activity {
    private ImageView mImage = null;
    private ImageView mImage1 = null;

    private ImageView mImage2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImage = (ImageView) this.findViewById(R.id.image_view);
        mImage.setImageBitmap(BitmapDecoder.
                decodeSampledBitmapFromResource(getResources(), R.drawable.pic, 100, 100));

        mImage1 = (ImageView) this.findViewById(R.id.image_view1);
        // loadBitmap(R.drawable.pic2, mImage1);
        ImageLoader loader = new ImageLoader(this);
        Bitmap bitmap = loader.downloadBitmap(mImage1,
                "http://h.hiphotos.baidu.com/image/h%3D360/sign=33e0f03492dda144c5096ab4" +
                        "82b7d009/dc54564e9258d1098ce9fd95d458ccbf6c814d8a.jpg");

        mImage1.setImageBitmap(bitmap);

        mImage2 = (ImageView) findViewById(R.id.image_view2);

        Bitmap bitmap1 = BitmapDecoder
                .decodeSampledBitmapFromResource(this.getResources(), R.drawable.pic3, 100, 100);

        mImage2.setImageBitmap(bitmap1);
    }


    public void loadBitmap(int resId, ImageView imageView) {
        //判断在引用队列中，是否存在所要执行的任务
        //如果没用就实例化一个新的任务对象
        // 并且在下载完成前，ImagView先绑定一个默认的资源
        if (BitmapWorkerTask.cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(MainActivity.this, imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(getResources(), BitmapFactory.decodeResource(getResources()
                            , R.drawable.pic2), task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

}
