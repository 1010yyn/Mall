package com.example.yangy.mall;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Shop_Order extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private int GET_NEW_OK = 1;//获取待处理订单OK
    private int GET_OLD_OK = 2;//获取已处理订单OK
    private int ORDER_NEW_EMPTY = 3;
    private int ORDER_OLD_EMPTY = 4;


    private Bundle bundle = new Bundle();
    private Intent intent1;

    private int id;

    private CreateData createData = new CreateData();

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//待处理商品列表
    private Data_Cart_Bean data_cart_bean1 = new Data_Cart_Bean();//已处理商店列表


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_NEW_OK) {
                try {
                    Log.i(TAG, "handleMessage: 获取待处理列表成功");
                    data_cart_bean = (Data_Cart_Bean) msg.obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == GET_OLD_OK) {
                try {
                    Log.i(TAG, "handleMessage: 获取已处理列表成功");
                    data_cart_bean1 = (Data_Cart_Bean) msg.obj;
                    setData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == ORDER_NEW_EMPTY)
                data_cart_bean = null;
            else if (msg.what == ORDER_OLD_EMPTY) {
                data_cart_bean1 = null;
                setData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shop__order);
        Log.i(TAG, "跳转订单界面");
        Intent intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getInt("id");//shop_id
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.layout_shop__order_new, tabHost.getTabContentView());//设置待处理订单选项卡
        inflater.inflate(R.layout.layout_shop__order_completed, tabHost.getTabContentView());//设置已处理订单选项卡
        tabHost.addTab(tabHost.newTabSpec("layout_shop__order_new").setIndicator("待处理").setContent(R.id.shop_order_left));
        tabHost.addTab(tabHost.newTabSpec("layout_shop__order_completed").setIndicator("已处理").setContent(R.id.shop_order_right));
    }

    void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表
                try {
                    JSONObject req = new JSONObject();
                    req.put("shop_id", id);
                    req.put("type", "OG_S");//获取订单信息（待处理）
                    req.put("operated", 0);//处理状态
                    ArrayList<String> rst = createData.post_m(req);//获取订单列表

                    JSONObject goods = null;//存储查询商品简略信息
                    String shop = "-1";//记录订单id，便于合并商品
                    //用shop_id记录订单编号

                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时订单信息
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表

                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    data_goods_beans = new ArrayList<>();//订单商品列表初始化
                    shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//订单信息初始化

                    int i = 0;
                    while (i < rst.size()) {
                        goods = new JSONObject(rst.get(i));//转换数据
                        //判定是否同一订单商品
                        //不是同一订单，则先将订单加入订单列表然后新建订单，否则不做处理
                        if (!shop.equals(goods.getString("shop_id"))) {
                            //将订单加入临时订单列表
                            if (i != 0)//若是第一件商品，则需要创建新订单item，否则，将先当前订单加入订单列表
                            {//将订单加入订单列表
                                shop_bean.setGoodsData(data_goods_beans);//将订单商品列表加入订单信息
                                data_shop_beans.add(shop_bean);//将订单加入订单列表
                            }
                            //创建新订单和订单商品列表并初始化
                            data_goods_beans = new ArrayList<>();//订单商品列表初始化
                            shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//订单信息初始化
                            shop_bean.setName(goods.getString("shop_id"));//设定订单号
                            shop_bean.setId(id + "");//设定商店id
                            //更新当前订单id
                            shop = goods.getString("shop_id");
                        }
                        //添加商品信息，加入订单商品列表
                        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean.setShopname(goods.getString("shop_id"));//设定订单号
                        goods_bean.setShopid(id + "");//设定商店id
                        goods_bean.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean.setSum(goods.getString("sum"));//设定数量
                        goods_bean.setPhoto(goods.getString("photo"));//获取图片资源
                        goods_bean.setDescription(goods.getString("description"));//设定商品描述
                        //添加商品至临时商品列表
                        data_goods_beans.add(goods_bean);
                        i++;
                    }
                    //如果订单列表非空
                    if (rst.size() > 0) {
                        //当前订单还未进行加入订单列表操作，跳出循环后继续
                        shop_bean.setGoodsData(data_goods_beans);//将商品列表加入订单信息
                        data_shop_beans.add(shop_bean);//将订单加入订单列表
                        //将所有的【订单列表】加入【商店订单列表】
                        data_cart_bean.setShopData(data_shop_beans);
                        Message msg = Message.obtain();
                        msg.obj = data_cart_bean;
                        msg.what = GET_NEW_OK;
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(ORDER_NEW_EMPTY);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans1 = new ArrayList<>();//临时店铺列表
                //获取已处理商品列表
                try {
                    JSONObject req = new JSONObject();
                    req.put("shop_id", id);
                    req.put("type", "OG_S");//获取订单信息（已处理）
                    req.put("operated", 1);//处理状态
                    ArrayList<String> rst = createData.post_m(req);//获取订单列表

                    JSONObject goods = null;//存储查询商品简略信息
                    String shop = "-1";//记录订单id，便于合并商品
                    //用shop_id记录订单编号

                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean1;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean1 = null;//临时订单信息
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans1 = null;//临时商品列表

                    Data_Cart_Bean data_cart_bean1 = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    int i = 0;
                    while (i < rst.size()) {
                        goods = new JSONObject(rst.get(i));//转换数据
                        //判定是否同一订单商品
                        //不是同一订单，则先将订单加入订单列表然后新建订单，否则不做处理
                        if (!shop.equals(goods.getString("shop_id"))) {
                            //将订单加入临时订单列表
                            if (i != 0)//若是第一件商品，则需要创建新订单item，否则，将先当前订单加入订单列表
                            {//将订单加入订单列表
                                shop_bean1.setGoodsData(data_goods_beans1);//将订单商品列表加入订单信息
                                data_shop_beans1.add(shop_bean1);//将订单加入订单列表
                            }
                            //创建新订单和订单商品列表并初始化
                            data_goods_beans1 = new ArrayList<>();//订单商品列表初始化
                            shop_bean1 = new Data_Cart_Bean.Data_Shop_Bean();//订单信息初始化
                            shop_bean1.setName(goods.getString("shop_id"));//设定订单号
                            shop_bean1.setId(id + "");//设定商店id
                            //更新当前订单id
                            shop = goods.getString("shop_id");
                        }
                        //添加商品信息，加入订单商品列表
                        goods_bean1 = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean1.setShopname(goods.getString("shop_id"));//设定订单编号
                        goods_bean1.setShopid(id + "");//设定商店id
                        goods_bean1.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean1.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean1.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean1.setSum(goods.getString("sum"));//设定数量
                        goods_bean1.setPhoto(goods.getString("photo"));//获取图片资源
                        goods_bean1.setDescription(goods.getString("description"));//设定商品描述
                        //添加商品至临时商品列表
                        data_goods_beans1.add(goods_bean1);//主页展示列表
                        i++;
                    }
                    //如果订单列表非空
                    if (rst.size() > 0) {
                        //当前订单还未进行加入订单列表操作，跳出循环后继续
                        shop_bean1.setGoodsData(data_goods_beans1);//将商品列表加入订单信息
                        data_shop_beans1.add(shop_bean1);//将订单加入订单列表
                        //将所有的【订单列表】加入【商店订单列表】
                        data_cart_bean1.setShopData(data_shop_beans1);
                        Message msg = Message.obtain();
                        msg.obj = data_cart_bean1;
                        msg.what = GET_OLD_OK;
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(ORDER_OLD_EMPTY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void setData() {
        RecyclerView new_list = findViewById(R.id.shop_order_list_new);//获取recycleview
        RecyclerView completed_list = findViewById(R.id.shop_order_list_completed);//获取recycleview
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list_new = MainActivity.sortData(data_cart_bean);
        List<Object> list_cplt = MainActivity.sortData(data_cart_bean1);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        new_list.setLayoutManager(manager);
        completed_list.setLayoutManager(manager1);
        final Order_Item_adapter adapter = new Order_Item_adapter(list_new);
        final Order_Item_adapter adapter1 = new Order_Item_adapter(list_cplt);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        new_list.addItemDecoration(divider);
        completed_list.addItemDecoration(divider);

        new_list.setAdapter(adapter);
        completed_list.setAdapter(adapter1);

        //设置单击事件
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.cart_shop_title__title:
                        Log.i(TAG, "进入订单信息界面" + position);
                        intent1 = new Intent(Shop_Order.this, Shop_Order_Handle.class);//为店铺结果界面创建intent
                        intent1.putExtra("order_number", (String) adapter.getItem(position));
                        intent1.putExtra("id", id);
                        startActivity(intent1);
                        break;
                }
            }
        });
        adapter1.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.cart_shop_title__title:
                        Log.i(TAG, "进入订单信息界面" + position);
                        intent1 = new Intent(Shop_Order.this, Order_Show.class);//为店铺结果界面创建intent
                        intent1.putExtra("order_number", (String) adapter.getItem(position));
                        intent1.putExtra("id", id);
                        intent1.putExtra("type", "shop");
                        startActivity(intent1);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }
}
