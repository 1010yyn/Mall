package com.example.yangy.mall;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TabHost;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Show_result extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int REQUEST_CODE = 7;//请求标识
    private int GET_GOODS_OK = 1;
    private int GET_SHOP_OK = 2;
    private int GOODS_EMPTY = 3;
    private int SHOP_EMPTY = 4;
    private int GET_ERROR = 0;

    private Intent intent1;
    private Bundle bundle = new Bundle();

    private int id;

    private CreateData createData = new CreateData();

    private Data_Cart_Bean data_cart_bean;//商品
    private Data_Cart_Bean data_cart_bean1;//商店

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_GOODS_OK)
                data_cart_bean = (Data_Cart_Bean) msg.obj;
            else if (msg.what == GET_SHOP_OK) {
                data_cart_bean1 = (Data_Cart_Bean) msg.obj;
                setData();
            } else if (msg.what == GOODS_EMPTY)
                data_cart_bean = null;
            else if (msg.what == SHOP_EMPTY) {
                data_cart_bean1 = null;
                setData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_result);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getInt("id");
        String key = bundle.getString("key");
        Log.i(TAG, "成功跳转到搜索结果页面，本次搜索关键词为：" + key);
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.layout_show_result__goods, tabHost.getTabContentView());//设置商品选项卡
        inflater.inflate(R.layout.layout_show_result__shop, tabHost.getTabContentView());//设置商店选项卡
        tabHost.addTab(tabHost.newTabSpec("layout_show_result__goods").setIndicator("商品").setContent(R.id.result_left));
        tabHost.addTab(tabHost.newTabSpec("layout_show_result__shop").setIndicator("商店").setContent(R.id.result_right));
        Query(key);
    }

    void setData() {

        setGoods();
        setShop();
        setSearch();
    }

    void setGoods() {
        RecyclerView list_goods = findViewById(R.id.result_goods);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_goods.setLayoutManager(manager);
        Goods_Item_adapter adapter_goods = new Goods_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_goods.addItemDecoration(divider);

        list_goods.setAdapter(adapter_goods);

        adapter_goods.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品");
                intent1 = new Intent(Show_result.this, Goods.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
                bundle.putCharSequence("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
                bundle.putInt("id", id);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }

    void setShop() {
        //获取recycleview
        RecyclerView list_shop = findViewById(R.id.result_shop);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean1);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_shop.setLayoutManager(manager);
        Shop_Item_adapter adapter_shop = new Shop_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_shop.addItemDecoration(divider);
        list_shop.setAdapter(adapter_shop);

        adapter_shop.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商店");
                intent1 = new Intent(Show_result.this, Shop.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop_name", (String) adapter.getItem(position));
                bundle.putInt("id", id);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }

    protected void setSearch() {
        final SearchView searchView = findViewById(R.id.result_search_bar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus(); // 不获取焦点
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                }
                Log.i(TAG, "进行搜索，关键词为：" + query);
                Query(query);//进行查询
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String selection = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " LIKE '%" + newText + "%' " + " OR "
                        + ContactsContract.RawContacts.SORT_KEY_PRIMARY + " LIKE '%" + newText + "%' ";
                return true;
            }
        });
    }

    //进行查询
    void Query(final String key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表
                try {
                    JSONObject req = new JSONObject();
                    req.put("type", "GS");
                    req.put("key", key);
                    req.put("id", id);
                    ArrayList<String> rst = createData.post_m(req);//获取收藏夹商品信息

                    JSONObject goods = null;//存储查询商品简略信息

                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表

                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    data_goods_beans = new ArrayList<>();//商店商品列表初始化
                    shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化

                    int i = 0;
                    while (i < rst.size()) {
                        goods = new JSONObject(rst.get(i));//商品信息string转json
                        //添加商品信息，加入商店商品列表
                        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean.setShopname(goods.getString("shop_name"));//设定商店名称
                        goods_bean.setShopid(goods.getString("shop_id"));//设定商店id
                        goods_bean.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean.setSum("");//设定数量（此处为空，收藏夹不需要显示数目
                        goods_bean.setPhoto(goods.getString("photo"));
                        goods_bean.setDescription(goods.getString("description"));//设定商品描述
                        //添加商品至临时商品列表
                        data_goods_beans.add(goods_bean);
                        i++;
                    }
                    //如果收藏夹非空
                    if (rst.size() > 0) {
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
                    } else handler.sendEmptyMessage(GOODS_EMPTY);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(GET_ERROR);
                }

                data_shop_beans = new ArrayList<>();//临时店铺列表
                //搜索商店
                try {
                    JSONObject req = new JSONObject();
                    req.put("type", "SS");
                    req.put("id", id);
                    req.put("key", key);

                    ArrayList<String> rst = createData.post_m(req);//获取搜素商店信息

                    JSONObject shop;//存储查询商店简略信息

                    Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    int i = 0;
                    while (i < rst.size()) {
                        shop = new JSONObject(rst.get(i));//商店信息转json
                        shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化
                        shop_bean.setName(shop.getString("shop_name"));//设定商店名
                        shop_bean.setId(shop.getString("shop_id"));//设定商店id
                        shop_bean.setGoodsData(null);//将商品列表加入店铺信息
                        data_shop_beans.add(shop_bean);//将商店加入购物车列表
                        i++;
                    }
                    //如果查询结果非空
                    if (rst.size() > 0) {
                        data_cart_bean.setShopData(data_shop_beans);
                        Message msg = Message.obtain();
                        msg.what = GET_SHOP_OK;
                        msg.obj = data_cart_bean;
                        handler.sendMessage(msg);
                    } else handler.sendEmptyMessage(SHOP_EMPTY);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }
}
