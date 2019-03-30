package com.example.yangy.mall;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateData extends Activity {

    private String TAG = "MYTAG";
    private boolean isNextPage;
    private List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data = new ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean>();
    //TODO——获取商店数据
    //TODO——获取用户个人信息
    //TODO——获取收藏夹信息
    //TODO——获取黑名单信息

    //获取商店数据
    public Data_Cart_Bean getdata(Context context) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getwebinfo();//把路径选到MainActivity中
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        String Head_Name = "head3";
        Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的OpenRecordBean对象

        //临时存储信息
        Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
        Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
        List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表
        List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表

        data_goods_beans = new ArrayList<>();
        //设置商品1信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("一家店");
        goods_bean.setName("一块西瓜");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品1");
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品2信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("一家店");
        goods_bean.setName("另一块西瓜");
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

        data_goods_beans = new ArrayList<>();
        //设置商品3信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("另一家店");
        goods_bean.setName("还有一块西瓜");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品3");
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品4信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("另一家店");
        goods_bean.setName("最后一块西瓜");
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

    private void getwebinfo() {
        try {
            //1,找水源--创建URL
            //URL url = new URL("http://192.168.43.110:8080/MyServer/server_servlet");//放网站
            URL url = new URL("https://www.baidu.com");
            //2,开水闸--openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //3，建管道--InputStream
            InputStream inputStream = httpURLConnection.getInputStream();
            //4，建蓄水池蓄水-InputStreamReader
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            //5，水桶盛水--BufferedReader
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer buffer = new StringBuffer();
            String temp = null;

            while ((temp = bufferedReader.readLine()) != null) {
                //取水--如果不为空就一直取
                buffer.append(temp);
            }
            bufferedReader.close();//记得关闭
            reader.close();
            inputStream.close();
            Log.e(TAG, buffer.toString());//打印结果

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}