package com.example.yangy.mall;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Order extends AppCompatActivity {

    private final static String TAG = "MYTAG";

    public final static int REQUEST_CODE = 11;//请求标识
    private int UPDATE_OK = 1;
    private int UPDATE_ERROR = 0;
    private int DELETE_ORDER_OK = 2;

    private Intent intent;
    private Bundle bundle;

    private String time;//获取时间戳
    private int id;

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();
    ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> list = new ArrayList<>();

    private CreateData createData = new CreateData();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_OK) {
                deleteCart();//删除购物车商品
                finish();
            } else if (msg.what == UPDATE_ERROR) {
                deleleOrder();//删除错误订单
            } else if (msg.what == DELETE_ORDER_OK)
                finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order);
        Log.i(TAG, "跳转订单界面");
        intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getInt("id");
        list = (ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean>) bundle.get("list");
        Data_Cart_Bean.Data_Shop_Bean shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//临时店铺店
        List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表
        shop_bean.setGoodsData(list);//向商店中加入商品列表
        shop_bean.setName("订单信息");
        data_shop_beans.add(shop_bean);//将该商店加入店铺列表
        data_cart_bean.setShopData(data_shop_beans);//店铺列表加入到购物车中

        Calendar cld = Calendar.getInstance();
        cld.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));//设置时区
        time = String.valueOf(cld.get(Calendar.YEAR)) + String.valueOf(cld.get(Calendar.MONTH) + 1) + String.valueOf(cld.get(Calendar.DATE)) + String.valueOf(cld.get(Calendar.HOUR) + 12) + String.valueOf(cld.get(Calendar.MINUTE)) + String.valueOf(cld.get(Calendar.SECOND)) + String.valueOf(cld.get(Calendar.MILLISECOND));

        setData();//填充订单信息
        Button pay = findViewById(R.id.order_pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();//确认支付时，添加订单
            }
        });

    }

    void setData() {
        RecyclerView list_order = findViewById(R.id.order_list);//获取recycleview
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
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品");
                intent = new Intent(Order.this, Goods.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
                bundle.putCharSequence("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //设定用户地址
        String address = bundle.getString("address");
        TextView Address = findViewById(R.id.order_address);
        Address.setText("地址：" + address);

        //显示订单编号
        TextView number = findViewById(R.id.order_number);
        number.setText("订单编号：" + time);
    }

    //订单信息记录到数据库
    void updateData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean flag = true;
                    JSONObject req;
                    int i = 0;
                    while (i < list.size()) {
                        req = new JSONObject();
                        req.put("type", "OA");
                        req.put("id", id);
                        req.put("order_number", time);
                        req.put("goods_id", list.get(i).getGoodsid());
                        req.put("shop_id", list.get(i).getShopid());
                        req.put("sum", list.get(i).getSum());
                        String rst = createData.post_m(req).get(0);
                        if (rst.equals("false")) {
                            flag = false;
                            break;//跳出循环
                        }
                        i++;
                    }
                    if (flag)
                        handler.sendEmptyMessage(UPDATE_OK);
                    else
                        handler.sendEmptyMessage(UPDATE_ERROR);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(UPDATE_ERROR);
                }
            }
        }).start();
    }

    void deleleOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject req = new JSONObject();
                    req.put("type", "OD");
                    req.put("order_number", time);
                    String rst = createData.post_m(req).get(0);
                    if (rst.equals("true"))
                        handler.sendEmptyMessage(DELETE_ORDER_OK);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //删除购物车内商品
    void deleteCart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject req;
                try {
                    req = new JSONObject();
                    int i = 0;
                    while (i < list.size()) {
                        req.put("type", "CD");
                        req.put("id", id);
                        req.put("goods_id", list.get(i).getGoodsid());
                        req.put("shop_id", list.get(i).getShopid());
                        String rst = createData.post_m(req).get(0);
                        i++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}