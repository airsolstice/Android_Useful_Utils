package com.hs.cla;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.hs.cla.entity.ItemBean;
import com.hs.cla.utils.CommonAdapter;
import com.hs.cla.utils.ViewHolder;

import java.util.List;


/**
 * Created by Holy-Spirit on 2015/12/29.
 * <p/>
 * 可以直接写一个匿名类，在ListView绑定Adapter的时候
 */
public class SubCommonAdapter extends CommonAdapter<ItemBean> {


    public SubCommonAdapter(Context context, List<ItemBean> datas) {
        super(context, datas);
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item;
    }

    /**
     * 重写抽象方法，将父类中的T泛型具体化，直接编程
     *
     * @param holder
     * @param item
     */
    @Override
    public void convert(ViewHolder holder, final ItemBean item) {


        holder.setImageURL(R.id.item_img, item.getUrl())
                .setText(R.id.item_title, item.getmTitle())
                .setText(R.id.item_content, item.getmContent())
                .setText(R.id.item_time, item.getmTime());

        final CheckBox mBox = holder.getView(R.id.item_cbox);
        mBox.setChecked(item.isSelected());
        mBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在复用控件的过程中，同控件在不同位置有不同的状态， 如果复用的话，可能导致内容错乱
                //这时需要一个存储机制来存储不同位置上同一个控件的值
                //除了写出来的这种方法，可以自己实例化一个List来存储每个位置的状态，每次获取相应位置
                item.setSelected(mBox.isChecked());
            }
        });

    }


}
