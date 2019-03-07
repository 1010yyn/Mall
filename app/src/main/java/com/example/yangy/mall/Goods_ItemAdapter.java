package com.example.yangy.mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Goods_ItemAdapter extends ArrayAdapter {

    public Goods_ItemAdapter(Context context, int resource, List<Goods_Item> objects) {
        super(context, resource, objects);
    }

    public View getview(int position, View convertView, ViewGroup parent) {
        Goods_Item goods_item = (Goods_Item) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_goods, null);
        ImageView head = view.findViewById(R.id.cart_shop_goods_photo);
        TextView name = view.findViewById(R.id.cart_shop_goods_name);
        TextView price = view.findViewById(R.id.cart_shop_goods_price);
        TextView sum = view.findViewById(R.id.cart_shop_goods_sum);
        head.setImageResource(goods_item.getHeadImage());
        name.setText(goods_item.getName());
        price.setText(goods_item.getPrice());
        sum.setText(goods_item.getSum());

        return view;
    }
}
