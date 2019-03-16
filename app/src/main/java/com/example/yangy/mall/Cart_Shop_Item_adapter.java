package com.example.yangy.mall;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

public class Cart_Shop_Item_adapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public static final int ITEM_TITLE = 1;
    public static final int ITEM_CONTENT = 2;
    private CheckBox checkBox;

    public Cart_Shop_Item_adapter(@Nullable List<Object> data) {
        super(data);
        // 第一步：动态判断
        setMultiTypeDelegate(new MultiTypeDelegate<Object>() {
            @Override
            protected int getItemType(Object o) {
                // 当前例子中只有两种类型
                if (o instanceof String) {
                    // 加载布局1
                    return ITEM_TITLE;
                } else if (o instanceof Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) {
                    // 加载布局2
                    return ITEM_CONTENT;
                }
                return 0;
            }
        });

        // 第二步：设置type和layout的对应关系
        getMultiTypeDelegate().registerItemType(ITEM_TITLE, R.layout.layout_main_cart_shop_title)
                .registerItemType(ITEM_CONTENT, R.layout.layout_main_cart_shop_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Object item) {
        // 第三步：设置不同布局下的组件数据
        switch (helper.getItemViewType()) {
            case ITEM_TITLE:
                // 标题赋值
                helper.setText(R.id.cart_shop_title__title, (String) item)
                        .addOnClickListener(R.id.cart_shop_title__title);   //给店铺名添加点击事件
                break;
            case ITEM_CONTENT:
                //填充商品内容
                helper.setText(R.id.cart_shop_goods__name, ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) item).getName())
                        .setText(R.id.cart_shop_goods__price, ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) item).getPrice())
                        .setText(R.id.cart_shop_goods__sum, ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) item).getSum())
                        .setImageResource(R.id.cart_shop_goods__photo, ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) item).getPhoto())
                        .setChecked(R.id.cart_shop_goods__selection, ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) item).getStatus())//初始状态设置为未选中
                        //给商品图片和商品名称添加子项单击事件
                        .addOnClickListener(R.id.cart_shop_goods__name)
                        .addOnClickListener(R.id.cart_shop_goods__photo);
                checkBox = helper.getView(R.id.cart_shop_goods__selection);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) item).setStatus(isChecked);
                    }
                });
                break;
        }
    }
}