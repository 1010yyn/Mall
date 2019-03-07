package com.example.yangy.mall;

public class Goods_Item {
    private int HeadImage;
    private String Name;
    private String Price;
    private int Sum;

    public Goods_Item(int headImage, String name, String price, int sum) {
        this.HeadImage = headImage;
        this.Name = name;
        this.Price = price;
        this.Sum = sum;
    }

    public int getHeadImage() {
        return HeadImage;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public int getSum() {
        return Sum;
    }
}
