package com.example.yangy.mall;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Goods extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private final static int REQUEST_CODE = 5;//请求标识
    private int GET_OK = 1;
    private int GET_ERROR = 0;

    private Intent intent1;

    private String Shop;

    private String name1, price1, description1, photo1;

    private String shop_id, goods_id;

    private CreateData getdata = new CreateData();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_ERROR)
                Toast.makeText(Goods.this, "加载商品信息失败！", Toast.LENGTH_SHORT);
            else {
                try {
                    JSONObject goods = new JSONObject(msg.obj.toString());
                    name1 = goods.getString("goods_name");
                    //TODO——图片传输photo=goods.getString("photo");
                    photo1 = "head5";
                    price1 = goods.getString("price");
                    description1 = goods.getString("description");
                    set();
                } catch (JSONException e) {
                    Log.e(TAG, "handleMessage: 解析商品数据失败");
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_goods);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.i(TAG, "成功跳转到商品页面");
        //TODO——get shop_id&goods_id
        //shop_id=bundle.getString("shop_id");
        //goods_id=bundle.getString("goods_id");

        //test
        shop_id = "2";
        goods_id = "1";

        JSONObject req = new JSONObject();
        try {
            req.put("type", "GG");
            req.put("shop_id", shop_id);
            req.put("goods_id", goods_id);
            get(req);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    private void get(final JSONObject req) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String rst = getdata.post(req);//尝试获取商品信息
                Message msg = Message.obtain();
                if (rst.equals("false"))//获取失败
                    handler.sendEmptyMessage(GET_ERROR);
                else {
                    msg.what = GET_OK;
                    msg.obj = rst;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void set() {
        ImageView photo = findViewById(R.id.goods_photo);
        TextView name = findViewById(R.id.goods_name);
        TextView price = findViewById(R.id.goods_price);
        TextView description = findViewById(R.id.goods_description);

        photo.setImageResource(getResources().getIdentifier(photo1, "drawable", getBaseContext().getPackageName()));
        name.setText(name1);
        price.setText(price1);
        description.setText(description1);

        Button star = findViewById(R.id.goods_star);
        Button hate = findViewById(R.id.goods_hate);
        Button cart = findViewById(R.id.goods_cart);
        Button shop = findViewById(R.id.goods_shop);

        //设置单击事件
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "加入收藏夹");
                //TODO——添加到收藏夹
            }
        });

        hate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "加入黑名单");
                //TODO——添加到黑名单
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "加入购物车");
                //TODO——添加到购物车,若购物车内已有，则商品数量++
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(Goods.this, Shop.class);
                intent1.putExtra("name", Shop);
                startActivity(intent1);
            }
        });
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }

}
