package com.example.yangy.mall;

public class Shop_Item {
    private String Name;
    private Goods_Item Goods;

    public Shop_Item(String name, Goods_Item goods) {
        this.Name = name;
        this.Goods = goods;
    }

    public String getName() {
        return Name;
    }

    public Goods_Item getGoods() {
        return Goods;
    }

}
