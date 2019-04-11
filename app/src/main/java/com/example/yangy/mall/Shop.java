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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Shop extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private final static int REQUEST_CODE = 6;//请求标识
    private int GET_SHOP_OK = 1;
    private int ADD_STAR_OK = 2;
    private int ADD_HATE_OK = 3;
    private int ADD_ERROR = 0;


    private Bundle bundle = new Bundle();
    private Intent intent1;

    private String shop_id;
    private String shop_name = "一家店";

    private CreateData createData = new CreateData();

    private Data_Cart_Bean data_cart_bean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_SHOP_OK) {
                shop_name = "";
                setData();//加载信息
            } else if (msg.what == ADD_STAR_OK)
                Toast.makeText(Shop.this, "加入收藏夹成功", Toast.LENGTH_SHORT).show();
            else if (msg.what == ADD_HATE_OK)
                Toast.makeText(Shop.this, "加入黑名单成功", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shop);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        shop_id = bundle.getString("shop_id");//获取商店id
        Log.i(TAG, "成功跳转商店界面");
        setData();
        //TODO——根据Name检索店铺信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject req = new JSONObject();
                    req.put("shop_id", shop_id);//查询商品信息
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    void setData() {
        //网络请求成功返回的OpenRecordBean对象
        Data_Cart_Bean data_cart_bean = createData.getdata(this.getBaseContext());

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
                            req.put("id", MainActivity.idOfuser);
                            req.put("shop_id", shop_id);
                            req.put("shop_name", shop_name);
                            req.put("star", "1");
                            String rst = createData.post(req);
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
                            req.put("id", MainActivity.idOfuser);
                            req.put("shop_id", shop_id);
                            req.put("shop_name", shop_name);
                            req.put("star", "0");
                            String rst = createData.post(req);
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

                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }
}
