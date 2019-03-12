package com.example.yangy.mall;

import java.util.List;

//DataBean
public class Data_Shop_Bean {
    private String shop_name;
    private List<Data_Goods_Bean> data;

    public String getName() {
        return shop_name;
    }

    public void setName(String name) {
        this.shop_name = name;
    }

    public List<Data_Goods_Bean> getData() {
        return data;
    }

    public void setData(List<Data_Goods_Bean> data) {
        this.data = data;
    }

}
