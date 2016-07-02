package com.hs.cla.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hs.cla.download.ImageLoader;


/**
 * Created by Holy-Spirit on 2015/12/29.
 * <p/>
 * This is a common ViewHolder
 */
public class ViewHolder {

    private SparseArray<View> mViews;
    private int mPosition;
    private LayoutInflater mInflater;
    private View mConvertView;
    private Context mContext;

    /**
     * ViewHolder构造方法
     * 初始化了SparseArray，过滤器..
     *
     * @param context
     * @param layoutId
     * @param parent
     * @param position
     */
    public ViewHolder(Context context, int layoutId, ViewGroup parent, int position) {

        this.mPosition = position;
        this.mContext = context;
        mViews = new SparseArray<View>();
        mInflater = LayoutInflater.from(context);
        mConvertView = mInflater.inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }


    /**
     * 获取ViewHolder对象
     *
     * @param convertView
     * @param context
     * @param layoutId
     * @param parent
     * @param position
     * @return
     */
    public static ViewHolder get(View convertView, Context context,
                                 int layoutId, ViewGroup parent, int position) {


        if (convertView == null)
        //判断convertView，如果没View存储，就实例化一个，
        // 不然就获取View，返回；之所以这么参数，是因为在new的时候有可能用到
        {
            return new ViewHolder(context, layoutId, parent, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            System.out.println("position_" + holder.mPosition);
            return holder;

        }
    }

    /**
     * 获取Item中控件对象
     * 这个方法使用于所有控件
     *
     * @param viewId
     * @param <T>
     * @return
     */

    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 获取convertView对象，用于getView()方法的返回值
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }


    /**
     * 设置TextView的文本内容
     * 之所以返回ViewHolder对象，是为了可以进行链式编程
     *
     * @param viewId
     * @param str
     * @return
     */
    public ViewHolder setText(int viewId, String str) {
        TextView mTxt = getView(viewId);
        mTxt.setText(str);
        return this;
    }

    /**
     * 设置ImageView的图片,通过资源Id
     *
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView mImg = getView(viewId);
        mImg.setImageResource(resId);

        return this;
    }


    public ViewHolder setImageResource(int viewId, int resId, int reqWidth, int reqJHeight) {
        ImageView mImg = getView(viewId);

        mImg.setImageBitmap(BitmapDecoder.decodeBitmapByResource(mContext.getResources(),
                resId, reqWidth, reqJHeight));

        return this;
    }


    /**
     * 设置ImageView图片，通过Bitmap对象
     *
     * @param viewId
     * @param bitmap
     * @return
     */

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView mImg = getView(viewId);
        mImg.setImageBitmap(bitmap);
        return this;
    }


    /**
     * 设置ImageView图片，通过Url在网络中加载图片转为Bitmap对象
     * 这里的加载方式可以自己编写，也可以使用第三方框架，如Volley
     *
     * @param viewId
     * @param url
     * @return
     */

    public ViewHolder setImageURL(int viewId, final String url) {
        final ImageView mImg = getView(viewId);
        //这里可以通过第三方框架加载图片，通过url，转为Bitmap，再在ImageView中设置
        //Bitmap bitmap = ImageLoader.getInstance.loading(url);
        //mImg.setImageBitmap(bitmap);

        //ImageLoaderSample loader = new ImageLoaderSample(mContext);
        //loader.DisplayImage(url,mImg);

        ImageLoader loader = new ImageLoader(mContext);
        loader.display(mImg, url);

        return this;
    }

}
