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
 * ����ֱ��дһ�������࣬��ListView��Adapter��ʱ��
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
     * ��д���󷽷����������е�T���;��廯��ֱ�ӱ��
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
                //�ڸ��ÿؼ��Ĺ����У�ͬ�ؼ��ڲ�ͬλ���в�ͬ��״̬�� ������õĻ������ܵ������ݴ���
                //��ʱ��Ҫһ���洢�������洢��ͬλ����ͬһ���ؼ���ֵ
                //����д���������ַ����������Լ�ʵ����һ��List���洢ÿ��λ�õ�״̬��ÿ�λ�ȡ��Ӧλ��
                item.setSelected(mBox.isChecked());
            }
        });

    }


}
