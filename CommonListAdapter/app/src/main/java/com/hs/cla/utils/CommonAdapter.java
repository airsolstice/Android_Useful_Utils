package com.hs.cla.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * Created by Holy-Spirit on 2015/12/29.
 */
public abstract  class CommonAdapter<T> extends BaseAdapter
{

    /**
     * 在这个抽象类中，完成了所有BaseAdapter可能做的所有重复的工作
     * 这可以大大节省代码量和时间
     */
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context,List<T> datas){
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * 其实可以直接抽象这个方法的重写，按如下的方法，就有点不针对一般性的编程
     *          因为ViewHolder这个对象的获取需要自己设置参数，如果抽象出来，就不能进行多种Adapter的复用了
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public  View getView(int position, View convertView, ViewGroup parent){

        int layoutId = getLayoutId();

        ViewHolder mHolder = ViewHolder.get(convertView,mContext,layoutId,parent,position);

        convert(mHolder,getItem(position));

        return mHolder.getConvertView();
    }


    /**
     * 抽象此方法，直接完成开发时需要的Item内容设置
     * @param holder
     * @param t
     */
    public abstract void convert(ViewHolder holder,T t);


    /**
     * 抽象方法，获取Item所需要的布局的id
     * @return
     */
    public abstract int getLayoutId();
}
