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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Order_Show extends AppCompatActivity {

    private String TAG = "MYTAG";
    private int GET_ORDER_OK = 1;
    private int GET_USER_OK = 2;

    private Intent intent;
    private Bundle bundle;

    private int id;
    private String order_number, address, username, phone;

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();
    ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> list = new ArrayList<>();

    private CreateData createData = new CreateData();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_ORDER_OK) {
                data_cart_bean = (Data_Cart_Bean) msg.obj;
                setData();
            } else if (msg.what == GET_USER_OK) {
                String rst = msg.obj.toString();
                try {
                    JSONObject js = new JSONObject(rst);
                    address = js.getString("address");
                    username = js.getString("nick");
                    phone = js.getString("phone");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order__show);
        intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getInt("id");
        order_number = bundle.getString("order_number");

    }

    void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表
                try {
                    //获取订单客户信息
                    JSONObject req = new JSONObject();
                    Log.i(TAG, "run: 获取客户信息");
                    req.put("type", "UG_O");
                    req.put("order_number", order_number);
                    String rst1 = createData.post_m(req).get(0);
                    Message msg = Message.obtain();
                    msg.what = GET_USER_OK;
                    msg.obj = rst1;
                    handler.sendMessage(msg);

                    //获取订单信息
                    req = new JSONObject();
                    Log.i(TAG, "run: 获取订单信息");
                    if (bundle.getString("type").equals("user")) {
                        req.put("type", "OG_S_I");//获取订单
                        req.put("shop_id", id);//商店id
                    } else {
                        req.put("type", "OG_U_I");//获取订单
                        req.put("id", id);//用户id
                    }
                    req.put("order_number", order_number);//订单号
                    ArrayList<String> rst = createData.post_m(req);

                    JSONObject goods = null;//存储查询商品简略信息

                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表

                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    data_goods_beans = new ArrayList<>();//商店商品列表初始化
                    shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化

                    int i = 0;//查询商品序号
                    while (i < rst.size()) {
                        goods = new JSONObject(rst.get(i));//商品信息string转json
                        //添加商品信息，加入商店商品列表
                        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean.setShopname(goods.getString("shop_name"));//设定商店名称
                        goods_bean.setShopid(goods.getString("shop_id"));//设定商店id
                        goods_bean.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean.setPhoto(goods.getString("photo"));//获取图片资源
                        goods_bean.setDescription(goods.getString("description"));//设定商品描述
                        if (goods.getInt("tag") == 1)
                            goods_bean.setSum(goods.getString("sum") + "  已发货");//设定数量
                        else
                            goods_bean.setSum(goods.getString("sum") + "  未发货");//设定数量

                        //添加商品至临时商品列表
                        data_goods_beans.add(goods_bean);
                        i++;
                    }
                    shop_bean.setName(goods.getString("shop_name"));//设定商店名
                    shop_bean.setId(goods.getString("shop_id"));//设定商店id
                    shop_bean.setGoodsData(data_goods_beans);//将商品列表加入店铺信息
                    data_shop_beans.add(shop_bean);//将商店加入购物车列表
                    //将所有的店铺列表加入购物车
                    data_cart_bean.setShopData(data_shop_beans);
                    msg = Message.obtain();
                    msg.what = GET_ORDER_OK;
                    msg.obj = data_cart_bean;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    void setData() {
        RecyclerView list_order = findViewById(R.id.shop_order_list);//获取recycleview
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_order.setLayoutManager(manager);
        Goods_Item_adapter adapter = new Goods_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_order.addItemDecoration(divider);
        list_order.setFocusable(false);//解决数据加载完成后, 没有停留在顶部的问题

        list_order.setAdapter(adapter);

        //设定用户地址
        TextView Address = findViewById(R.id.shop_order_address);
        Address.setText("地址：" + address);

        //设定收货人姓名
        TextView Nick = findViewById(R.id.shop_order_name);
        Nick.setText("收货人：" + username);

        //设定收货人手机号码
        TextView Phone = findViewById(R.id.shop_order_phone);
        Phone.setText("手机号码：" + phone);

        //显示订单编号
        TextView number = findViewById(R.id.shop_order_number);
        number.setText("订单编号：" + order_number);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
