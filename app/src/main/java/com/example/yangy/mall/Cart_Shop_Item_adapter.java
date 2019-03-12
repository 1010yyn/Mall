package com.example.yangy.mall;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

public class Cart_Shop_Item_adapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public static final int ITEM_TITLE = 1;
    public static final int ITEM_CONTENT = 2;

    public Cart_Shop_Item_adapter(List<Object> data) {
        super(data);
        // 第一步：动态判断
        setMultiTypeDelegate(new MultiTypeDelegate<Object>() {
            @Override
            protected int getItemType(Object o) {
                // 当前例子中只有两种类型
                if (o instanceof String) {
                    // 加载布局1
                    return ITEM_TITLE;
                } else if (o instanceof Data_Cart_Bean) {
                    // 加载布局2
                    return ITEM_CONTENT;
                }
                return 0;
            }
        });
    }

    // 第二步：设置type和layout的对应关系
    //getMultiTypeDelegate().registerItemType(ITEM_TITLE,R.layout.open_record_title)
    //       .registerItemType(ITEM_CONTENT,R.layout.open_record_content);
    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        // 第三步：设置不同布局下的组件数据
        switch (helper.getItemViewType()) {
            case ITEM_TITLE:
                // 标题赋值
                helper.setText(R.id.open_record_date_tv, (String) item);
                break;
            case ITEM_CONTENT:
//                helper.setText(R.id.open_record_time_tv, ((OpenRecordBean.com.example.yangy.mall.Data_Shop_Bean.LogDOListBean) item).getUpdateTime())
//                        .setText(R.id.open_record_terminal_tv, ((OpenRecordBean.com.example.yangy.mall.Data_Shop_Bean.LogDOListBean) item).getDeviceName())
//                        .setText(R.id.open_record_name_tv, ((OpenRecordBean.com.example.yangy.mall.Data_Shop_Bean.LogDOListBean) item).getFullName())
//                        .setText(R.id.open_record_door_NO_tv, "" + ((OpenRecordBean.com.example.yangy.mall.Data_Shop_Bean.LogDOListBean) item).getOpenType());
                break;
        }
    }
}