package com.example.yangy.mall;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Order extends AppCompatActivity {

    private final static String TAG = "MYTAG";

    public final static int REQUEST_CODE = 11;//请求标识

    private Bundle bundle;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order);
        Log.i(TAG, "跳转订单界面");
        Intent intent = getIntent();
        bundle = intent.getExtras();
        ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> list = new ArrayList<>();
        list = (ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean>) bundle.get("list");
        Log.i(TAG, list.get(0).getGoodsname());
        //TODO——完成数据加载
    }
}