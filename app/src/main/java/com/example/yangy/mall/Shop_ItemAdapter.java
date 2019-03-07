package com.example.yangy.mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Shop_ItemAdapter extends ArrayAdapter {
    public Shop_ItemAdapter(Context context, int resource, List<Shop_Item> objects) {
        super(context, resource, objects);
    }

    public View getview(int position, View convertView, ViewGroup parent) {
        Shop_Item shop_item = (Shop_Item) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_goods, null);
        TextView name = view.findViewById(R.id.cart_shop_name);
        //TODO——自定义listview
        // shop=view.findViewById(R.id.cart_shop_goods);
        name.setText(shop_item.getName());
        //shop.setAdapter(shop_item.getGoods());
        return view;
    }
}
