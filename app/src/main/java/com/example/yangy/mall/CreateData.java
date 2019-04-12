package com.example.yangy.mall;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CreateData extends Activity {

    private String TAG = "MYTAG";

    //获取商店数据
    public Data_Cart_Bean getdata(Context context) {
        String Head_Name = "head3";
        Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

        //临时存储信息
        Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
        Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
        List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表
        List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表

        //商店商品列表初始化
        data_goods_beans = new ArrayList<>();
        //设置商品1信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("一家店");
        goods_bean.setShopid("1");
        goods_bean.setGoodsname("一块西瓜");
        goods_bean.setGoodsid("1");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品1");
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品2信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("一家店");
        goods_bean.setShopid("1");
        goods_bean.setGoodsname("另一块西瓜");
        goods_bean.setGoodsid("2");
        goods_bean.setPrice(432);
        goods_bean.setSum(3);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品2");
        //添加商品至临时商品列表1
        data_goods_beans.add(goods_bean);

        //设置临时商铺信息1
        shop_bean = new Data_Cart_Bean.Data_Shop_Bean();
        shop_bean.setName("一家店");
        shop_bean.setId("1");
        //将商品列表加入临时店铺1
        shop_bean.setGoodsData(data_goods_beans);

        //将店铺加入临时店铺列表
        data_shop_beans.add(shop_bean);

        data_goods_beans = new ArrayList<>();
        //设置商品3信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("另一家店");
        goods_bean.setShopid("2");
        goods_bean.setGoodsname("还有一块西瓜");
        goods_bean.setGoodsid("1");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(context.getResources().getIdentifier(Head_Name, "drawable", context.getPackageName()));
        goods_bean.setDescription("商品3");
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品4信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("另一家店");
        goods_bean.setShopid("2");
        goods_bean.setGoodsname("最后一块西瓜");
        goods_bean.setGoodsid("2");
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

    //get数据
    //public ArrayList<String> post(JSONObject req) {
    public String post(JSONObject req) {
        String strUrl = "http://192.168.43.110:8080/MyServer/server_servlet";//手机
        //String strUrl = "http://172.17.104.159:8080/MyServer/server_servlet";//宿舍
        String result = "";
        ArrayList<String> rst = new ArrayList<>();
        URL url;
        try {
            url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);//设置输入流采用字节流
            urlConn.setDoOutput(true);//设置输出流采用字节流
            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);//设置缓存
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置meta参数
            urlConn.setRequestProperty("Charset", "utf-8");

            Log.i(TAG, "尝试连接");
            urlConn.connect();//连接，往服务端发送消息

            DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
            dop.writeBytes("param=" + URLEncoder.encode(req.toString(), "utf-8"));//发送参数
            dop.flush();//发送，清空缓存
            dop.close();//关闭

            //接收返回数据
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String readLine;

            while ((readLine = bufferReader.readLine()) != null) {
                result += readLine;
                rst.add(readLine);
            }
            bufferReader.close();
            urlConn.disconnect();//关闭连接
            result = URLDecoder.decode(result, "UTF-8");//解码数据
            if (req.getString("type").equals("CG") || req.getString("type").equals("LG_G") || req.getString("type").equals("LG_S"))//获取购物车数据开头结尾加上括号，以便转换成json
                result = "{" + result + "}";
            Log.i(TAG, "result:" + result);
        } catch (Exception e) {
            Log.e(TAG, "连接失败" + e.getMessage());
            rst.add("false");
        }
        return result;
        //return rst;
    }

    public ArrayList<String> post_m(JSONObject req) {
        String strUrl = "http://192.168.43.110:8080/MyServer/server_servlet";//手机
        //String strUrl = "http://172.17.104.159:8080/MyServer/server_servlet";//宿舍
        String result = "";
        ArrayList<String> rst = new ArrayList<>();
        URL url;
        try {
            url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);//设置输入流采用字节流
            urlConn.setDoOutput(true);//设置输出流采用字节流
            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);//设置缓存
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置meta参数
            urlConn.setRequestProperty("Charset", "utf-8");

            Log.i(TAG, "尝试连接");
            urlConn.connect();//连接，往服务端发送消息

            DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
            dop.writeBytes("param=" + URLEncoder.encode(req.toString(), "utf-8"));//发送参数
            dop.flush();//发送，清空缓存
            dop.close();//关闭

            //接收返回数据
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String readLine;

            while ((readLine = bufferReader.readLine()) != null) {
                result += readLine;
                rst.add(URLDecoder.decode(readLine, "UTF-8"));
            }
            bufferReader.close();
            urlConn.disconnect();//关闭连接
            result = URLDecoder.decode(result, "UTF-8");//解码数据
            Log.i(TAG, "result:" + result);
        } catch (Exception e) {
            Log.e(TAG, "连接失败" + e.getMessage());
            rst.add("false");
        }
        return rst;
    }

    //TODO——get图片
}