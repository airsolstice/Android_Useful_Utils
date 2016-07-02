package com.hs.cla;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.hs.cla.entity.ItemBean;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private ListView mListView;
    private List<ItemBean> mDatas;

    private String[] urls = new String[]{
            "http://img.taopic.com/uploads/allimg/130724/240450-130H422422687.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/aa64034f78f0f73610034de60e55b319eac41353" +
                    ".jpg",
            "http://www.sj88.com/attachments/bd-aldimg/1204/123/1.jpg",
            "http://www.sj88.com/attachments/bd-aldimg/1204/123/5.jpg",
            "http://www.9yaocn.com/attachments/201412/04/13/117pan97e.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/472309f790529822dd828685d2ca7bcb0b46d4c7" +
                    ".jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/962bd40735fae6cd1bc81e210cb30f2442a70f75" +
                    ".jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/b3fb43166d224f4a19a4bd350cf790529822d1d3" +
                    ".jpg",
            "http://h.hiphotos.baidu.com/image/h%3D360/sign=33e0f03492dda144c5096ab482b7d009" +
                    "/dc54564e9258d1098ce9fd95d458ccbf6c814d8a.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();


    }

    private void initView() {
        mListView = (ListView) this.findViewById(R.id.main_list);
        SubCommonAdapter mAdapter = new SubCommonAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);

        //可以直接写匿名类
        //        mListView.setAdapter(new CommonAdapter<ItemBean>(this, mDatas) {
        //            @Override
        //            public void convert(ViewHolder holder, ItemBean item) {
        //
        //                holder.setImageURL(R.id.item_img, item.getUrl())
        //                        .setText(R.id.item_title, item.getmTitle())
        //                        .setText(R.id.item_content, item.getmContent())
        //                        .setText(R.id.item_time, item.getmTime());
        //            }
        //
        //            @Override
        //            public int getLayoutId() {
        //                return R.layout.list_item;
        //            }
        //        });

    }

    private void initData() {
        mDatas = new ArrayList<>();

        ItemBean item1 = new ItemBean("标题1", "这是通用适配器1",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[0]);
        mDatas.add(item1);

        ItemBean item2 = new ItemBean("标题2", "这是通用适配器2",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[1]);
        mDatas.add(item2);

        ItemBean item3 = new ItemBean("标题3", "这是通用适配器3",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[2]);
        mDatas.add(item3);

        ItemBean item4 = new ItemBean("标题4", "这是通用适配器4",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[3]);
        mDatas.add(item4);

        ItemBean item5 = new ItemBean("标题5", "这是通用适配器5",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[4]);
        mDatas.add(item5);

        ItemBean item6 = new ItemBean("标题6", "这是通用适配器6",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[5]);
        mDatas.add(item6);

        ItemBean item7 = new ItemBean("标题7", "这是通用适配器7",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[6]);
        mDatas.add(item7);

        ItemBean item8 = new ItemBean("标题8", "这是通用适配器8",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[7]);
        mDatas.add(item8);

        ItemBean item9 = new ItemBean("标题9", "这是通用适配器9",
                "2015-12-29 16:22", R.drawable.icon_mobile, urls[8]);
        mDatas.add(item9);

    }


}
