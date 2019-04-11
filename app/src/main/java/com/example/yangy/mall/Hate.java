package com.example.yangy.mall;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Hate extends TabActivity {


    private String TAG = "MYTAG";
    public final static int REQUEST_CODE = 9;//请求标识
    private int GET_GOODS_OK = 1;
    private int GET_SHOP_OK = 2;
    private int HATE_EMPTY_G = 3;
    private int HATE_EMPTY_S = 4;
    private int DLT_SHOP_OK = 5;
    private int DLT_GOODS_OK = 6;
    private int GET_ERROR = 0;

    private Intent intent1;
    private Bundle bundle = new Bundle();

    private AlertDialog.Builder delete;

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//黑名单商品列表
    private Data_Cart_Bean data_cart_bean1 = new Data_Cart_Bean();//黑名单商店列表

    private CreateData createData = new CreateData();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_GOODS_OK) {
                data_cart_bean = (Data_Cart_Bean) msg.obj;//收藏夹商品数据
            } else if (msg.what == GET_SHOP_OK) {
                data_cart_bean1 = (Data_Cart_Bean) msg.obj;//收藏夹店铺数据
                setData();//设定数据
            } else if (msg.what == HATE_EMPTY_G)
                data_cart_bean = null;
            else if (msg.what == HATE_EMPTY_S) {
                data_cart_bean1 = null;
                setData();//设定数据
            } else if (msg.what == DLT_SHOP_OK)
                Toast.makeText(Hate.this, "删除黑名单店铺成功！", Toast.LENGTH_SHORT).show();
            else if (msg.what == DLT_GOODS_OK)
                Toast.makeText(Hate.this, "删除黑名单商品成功！", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Hate.this, "操作失败！", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        bundle = intent.getExtras();

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject req = new JSONObject();
                List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表
                try {
                    req.put("type", "LG_G");
                    req.put("id", MainActivity.idOfuser + "");
                    req.put("star", "0");
                    String rst = createData.post(req);//获取收藏夹商品信息

                    JSONObject star = new JSONObject(rst);//获取商品简略列表（shop_id,goods_id）
                    JSONObject goods = new JSONObject();//存储查询商品简略信息
                    JSONObject req_g;//新查询商品请求

                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean = null;//临时店铺信息
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans = null;//临时商品列表

                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    data_goods_beans = new ArrayList<>();//商店商品列表初始化
                    shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化

                    int i = 0;//查询商品序号
                    while (i < star.getInt("count")) {
                        req_g = new JSONObject();
                        req_g.put("type", "GG");
                        req_g.put("goods_id", star.getString("goods_id" + i));
                        req_g.put("shop_id", star.getString("shop_id" + i));
                        Log.i(TAG, "查询goods " + star.getString("goods_id" + i) + " " + star.getString("shop_id" + i));
                        rst = createData.post(req_g);//获取商品信息
                        goods = new JSONObject(rst);//商品信息string转json
                        //添加商品信息，加入商店商品列表
                        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean.setShopname(goods.getString("shop_name"));//设定商店名称
                        goods_bean.setShopid(goods.getString("shop_id"));//设定商店id
                        goods_bean.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean.setPhoto(getResources().getIdentifier(goods.getString("photo"), "drawable", getPackageName()));
                        goods_bean.setDescription(goods.getString("description"));//设定商品描述
                        //添加商品至临时商品列表
                        data_goods_beans.add(goods_bean);
                        i++;
                    }
                    //如果收藏夹非空
                    if (star.getInt("count") > 0) {
                        shop_bean.setName(goods.getString("shop_name"));//设定商店名
                        shop_bean.setId(goods.getString("shop_id"));//设定商店id
                        shop_bean.setGoodsData(data_goods_beans);//将商品列表加入店铺信息
                        data_shop_beans.add(shop_bean);//将商店加入购物车列表
                        //将所有的店铺列表加入购物车
                        data_cart_bean.setShopData(data_shop_beans);
                        Message msg = Message.obtain();
                        msg.what = GET_GOODS_OK;
                        msg.obj = data_cart_bean;
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(HATE_EMPTY_G);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                data_shop_beans = new ArrayList<>();//临时店铺列表
                //获取收藏夹商店信息
                try {
                    req = new JSONObject();
                    req.put("type", "LG_S");
                    req.put("id", MainActivity.idOfuser + "");
                    req.put("star", "0");

                    String rst = createData.post(req);
                    JSONObject star = new JSONObject(rst);//获取商品简略列表（shop_id）

                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean = null;//临时店铺信息
                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    int i = 0;
                    while (i < star.getInt("count")) {
                        shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化
                        shop_bean.setName(star.getString("shop_name" + i));//设定商店名
                        shop_bean.setId(star.getString("shop_id" + i));//设定商店id
                        shop_bean.setGoodsData(null);//将商品列表加入店铺信息
                        data_shop_beans.add(shop_bean);//将商店加入购物车列表
                        i++;
                    }
                    if (star.getInt("count") > 0) {
                        data_cart_bean.setShopData(data_shop_beans);
                        Message msg = Message.obtain();
                        msg.what = GET_SHOP_OK;
                        msg.obj = data_cart_bean;
                        handler.sendMessage(msg);
                    } else handler.sendEmptyMessage(HATE_EMPTY_S);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    void setData() {
        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.layout_hate, tabHost.getTabContentView(), true);
        tabHost.addTab(tabHost.newTabSpec("layout_hate_goods").setIndicator("商品").setContent(R.id.hate_tab__goods));
        tabHost.addTab(tabHost.newTabSpec("layout_hate_shop").setIndicator("商店").setContent(R.id.hate_tab__shop));

        //获取数据
        add_goods();//商品列表
        add_shop();//商店列表
    }

    private void add_goods() {
        //获取recycleview
        RecyclerView list_goods = findViewById(R.id.hate__goods_list);
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
                intent1 = new Intent(Hate.this, Goods.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
                bundle.putCharSequence("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                Log.i(TAG, "长按购物车商品" + position);
                delete = new AlertDialog.Builder(Hate.this);//确认删除对话框
                delete.setTitle("系统提示：");
                delete.setMessage("确定要删除吗？");
                delete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "取消删除");//不做操作
                        dialog.dismiss();//关闭对话框
                    }
                });
                delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "确认删除");
                        final JSONObject req = new JSONObject();
                        try {
                            req.put("type", "LD_G");
                            req.put("id", MainActivity.idOfuser + "");
                            req.put("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
                            req.put("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
                            req.put("star", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String rst = createData.post(req);
                                if (rst.equals("true")) {
                                    handler.sendEmptyMessage(DLT_GOODS_OK);
                                } else
                                    handler.sendEmptyMessage(GET_ERROR);
                            }
                        }).start();
                        adapter.remove(position);//删除商品item
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                    }
                });
                delete.create().show();
                return true;
            }
        });
    }

    private void add_shop() {
        //获取recycleview
        RecyclerView list_shop = findViewById(R.id.hate__shop_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean1);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_shop.setLayoutManager(manager);
        final Shop_Item_adapter adapter = new Shop_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_shop.addItemDecoration(divider);
        list_shop.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品");
                intent1 = new Intent(Hate.this, Shop.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopname());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                Log.i(TAG, "长按购物车商品" + position);
                delete = new AlertDialog.Builder(Hate.this);//确认删除对话框
                delete.setTitle("系统提示：");
                delete.setMessage("确定要删除吗？");
                delete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "取消删除");//不做操作
                        dialog.dismiss();//关闭对话框
                    }
                });
                delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "确认删除");
                        //删除数据库中数据
                        final JSONObject req = new JSONObject();
                        try {
                            req.put("type", "LD_S");
                            req.put("id", MainActivity.idOfuser + "");
                            req.put("shop_name", adapter.getItem(position));
                            req.put("star", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String rst = createData.post(req);
                                if (rst.equals("true")) {
                                    handler.sendEmptyMessage(DLT_SHOP_OK);
                                } else
                                    handler.sendEmptyMessage(GET_ERROR);
                            }
                        }).start();
                        adapter.remove(position);//删除商品item
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                    }
                });
                delete.create().show();
                return true;
            }
        });
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }
}
