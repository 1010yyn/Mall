package com.example.yangy.mall;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class CreateData extends AppCompatActivity {

    //TODO——获取商店数据
    //TODO——获取用户个人信息
    //TODO——获取收藏夹信息
    //TODO——获取黑名单信息

    //获取商店数据
    public Data_Cart_Bean getdata(Context context) {
        String Head_Name = "head5";
        Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的OpenRecordBean对象

        //临时存储信息
        Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
        Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
        List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表
        List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<Data_Cart_Bean.Data_Shop_Bean>();//临时店铺列表

        data_goods_beans = new ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean>();
        //设置商品1信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setName("123");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品1");
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品2信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("一家店");
        goods_bean.setName("541");
        goods_bean.setPrice(432);
        goods_bean.setSum(3);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品2");
        //添加商品至临时商品列表1
        data_goods_beans.add(goods_bean);

        //设置临时商铺信息1
        shop_bean = new Data_Cart_Bean.Data_Shop_Bean();
        shop_bean.setName("一家店");
        //将商品列表加入临时店铺1
        shop_bean.setGoodsData(data_goods_beans);

        //将店铺加入临时店铺列表
        data_shop_beans.add(shop_bean);


        data_goods_beans = new ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean>();
        //设置商品3信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("另一家店");
        goods_bean.setName("324");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品3");
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品4信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("另一家店");
        goods_bean.setName("793");
        goods_bean.setPrice(432);
        goods_bean.setSum(3);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品4");
        //添加商品至临时商品列表2
        data_goods_beans.add(goods_bean);

        //设置临时商铺信息2
        shop_bean = new Data_Cart_Bean.Data_Shop_Bean();
        shop_bean.setName("另一家店");
        //将商品列表加入临时店铺2
        shop_bean.setGoodsData(data_goods_beans);

        //将店铺加入临时店铺列表
        data_shop_beans.add(shop_bean);

        //将店铺列表加入购物车
        data_cart_bean.setShopData(data_shop_beans);
        return data_cart_bean;
    }
}
