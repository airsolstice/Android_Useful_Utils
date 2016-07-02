package com.hs.cla.entity;

/**
 * Created by Holy-Spirit on 2015/12/29.
 */
public class ItemBean{

    private int resId;
    private String url;
    private String mTitle;
    private String mContent;
    private String mTime;
    private boolean isSelected = false;


    public ItemBean(String title ,String content,String time,int resId,String url){
        this.mTitle = title;
        this.mContent = content;
        this.mTime = time;
        this.resId = resId;
        this.url = url;
    }


    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }


    public int getResId()
    {
            return resId;
    }

    public void setResId(int resId)
        {
            this.resId = resId;
        }



        public String getmTitle()
        {
            return mTitle;
        }

        public void setmTitle(String mTitle)
        {
            this.mTitle = mTitle;
        }

        public String getmContent()
        {
            return mContent;
        }

        public void setmContent(String mContent)
        {
            this.mContent = mContent;
        }

        public String getmTime()
        {
            return mTime;
        }

        public void setmTime(String mTime)
        {
            this.mTime = mTime;
        }
}
