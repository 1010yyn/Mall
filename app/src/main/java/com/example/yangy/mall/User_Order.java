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
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User_Order extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private int GET_ORDER_OK = 1;
    private int ORDER_EMPTY = 2;

    private Bundle bundle = new Bundle();
    private Intent intent1;

    private int id;

    private CreateData createData = new CreateData();

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//待处理商品列表

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_ORDER_OK)
                try {
                    Log.i(TAG, "handleMessage: 获取订单列表成功");
                    data_cart_bean = (Data_Cart_Bean) msg.obj;
                    setData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user__order);
        Log.i(TAG, "跳转订单界面");
        Intent intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getInt("id");

    }

    void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表
                try {
                    JSONObject req = new JSONObject();
                    req.put("id", id);
                    req.put("type", "OG_U");//获取订单信息（待处理）
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
                        msg.what = GET_ORDER_OK;
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(ORDER_EMPTY);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    void setData() {
        //获取主页list
        RecyclerView list_goods = findViewById(R.id.order_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_goods.setLayoutManager(manager);
        final Order_Item_adapter adapter = new Order_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_goods.addItemDecoration(divider);
        list_goods.setFocusable(false);//解决数据加载完成后, 没有停留在顶部的问题

        list_goods.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.cart_shop_title__title:
                        Log.i(TAG, "进入订单信息界面" + position);
                        intent1 = new Intent(User_Order.this, Order_Show.class);//为店铺结果界面创建intent
                        intent1.putExtra("order_number", (String) adapter.getItem(position));
                        intent1.putExtra("id", id);
                        intent1.putExtra("type", "user");
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
