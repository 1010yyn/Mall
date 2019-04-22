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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Shop extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private final static int REQUEST_CODE = 6;//请求标识
    private int GET_SHOP_OK = 1;
    private int ADD_STAR_OK = 2;
    private int ADD_HATE_OK = 3;
    private int SHOP_EMPTY = 4;
    private int ADD_ERROR = 0;

    private Bundle bundle = new Bundle();
    private Intent intent1;

    private String shop_id, shop_name;
    private int id;

    private CreateData createData = new CreateData();

    private Data_Cart_Bean data_cart_bean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_SHOP_OK) {
                data_cart_bean = (Data_Cart_Bean) msg.obj;
                setData();//加载信息
            } else if (msg.what == ADD_STAR_OK)
                Toast.makeText(Shop.this, "加入收藏夹成功", Toast.LENGTH_SHORT).show();
            else if (msg.what == ADD_HATE_OK)
                Toast.makeText(Shop.this, "加入黑名单成功", Toast.LENGTH_SHORT).show();
            else if (msg.what == SHOP_EMPTY) {
                data_cart_bean = null;
                setData();//加载信息
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shop);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getInt("id");
        shop_id = bundle.getString("shop_id");//获取商店id
        shop_name = bundle.getString("shop_name");
        Log.i(TAG, "成功跳转商店界面");
        setData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject req = new JSONObject();
                    req.put("shop_name", shop_name);
                    req.put("type", "SG");
                    ArrayList<String> rst = createData.post_m(req);//查询商品信息

                    JSONObject goods = null;
                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean = null;//临时店铺信息
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans = null;//临时商品列表

                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    data_goods_beans = new ArrayList<>();//商店商品列表初始化
                    shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化

                    List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表

                    int i = 0;
                    while (i < rst.size()) {
                        goods = new JSONObject(rst.get(i));//转换数据
                        //添加商品信息，加入商店商品列表
                        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean.setShopname(goods.getString("shop_name"));//设定商店名称
                        goods_bean.setShopid(goods.getString("shop_id"));//设定商店id
                        goods_bean.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean.setPhoto(goods.getString("photo"));
                        goods_bean.setDescription(goods.getString("description"));//设定商品描述
                        //添加商品至临时商品列表
                        data_goods_beans.add(goods_bean);
                        i++;
                    }
                    //如果店铺商品非空
                    if (rst.size() > 0) {
                        shop_bean.setName(goods.getString("shop_name"));//设定商店名
                        shop_bean.setId(goods.getString("shop_id"));//设定商店id
                        shop_bean.setGoodsData(data_goods_beans);//将商品列表加入店铺信息
                        data_shop_beans.add(shop_bean);//将商店加入购物车列表
                        //将所有的店铺列表加入购物车
                        data_cart_bean.setShopData(data_shop_beans);
                        Message msg = Message.obtain();
                        msg.obj = data_cart_bean;
                        msg.what = GET_SHOP_OK;
                        handler.sendMessage(msg);
                    } else handler.sendEmptyMessage(SHOP_EMPTY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void setData() {
        Button star = findViewById(R.id.shop_star);
        Button hate = findViewById(R.id.shop_hate);

        //收藏
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject req = new JSONObject();
                            req.put("type", "LA_S");
                            req.put("id", id);
                            req.put("shop_id", shop_id);
                            req.put("shop_name", shop_name);
                            req.put("star", "1");
                            String rst = createData.post_m(req).get(0);
                            if (rst.equals("true"))
                                handler.sendEmptyMessage(ADD_STAR_OK);
                            else if (rst.equals("false"))
                                handler.sendEmptyMessage(ADD_ERROR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        //拉黑
        hate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject req = new JSONObject();
                            req.put("type", "LA_S");
                            req.put("id", id);
                            req.put("shop_id", shop_id);
                            req.put("shop_name", shop_name);
                            req.put("star", "0");
                            String rst = createData.post_m(req).get(0);
                            if (rst.equals("true"))
                                handler.sendEmptyMessage(ADD_HATE_OK);
                            else if (rst.equals("false"))
                                handler.sendEmptyMessage(ADD_ERROR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        //获取recycleview
        RecyclerView list_goods = findViewById(R.id.shop_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_goods.setLayoutManager(manager);
        final Goods_Item_adapter adapter = new Goods_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_goods.addItemDecoration(divider);
        list_goods.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品");
                intent1 = new Intent(Shop.this, Goods.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
                bundle.putCharSequence("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
                bundle.putInt("id", id);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        TextView name = findViewById(R.id.shop_name);
        name.setText(shop_name);
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }
}