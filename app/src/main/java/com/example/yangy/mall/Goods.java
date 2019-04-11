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

    private int GET_ERROR = 0;
    private int GET_OK = 1;//获取商品信息OK
    private int ADD_CART_OK = 2;//添加购物车OK
    private int ADD_STAR_OK = 3;//添加收藏夹OK
    private int ADD_HATE_OK = 4;//添加黑名单OK

    private Intent intent1;

    private String name1, price1, description1, photo1;

    private String shop_id, goods_id;

    private CreateData getdata = new CreateData();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_ERROR)
                Toast.makeText(Goods.this, "操作失败！", Toast.LENGTH_SHORT);
            else if (msg.what == GET_OK) {
                try {
                    JSONObject goods = new JSONObject(msg.obj.toString());
                    name1 = goods.getString("goods_name");
                    //TODO——图片传输;
                    photo1 = goods.getString("photo");
                    price1 = goods.getString("price");
                    description1 = goods.getString("description");
                    set();
                } catch (JSONException e) {
                    Log.e(TAG, "handleMessage: 解析商品数据失败");
                    e.printStackTrace();
                }
            } else if (msg.what == ADD_CART_OK)//添加购物车成功
                Toast.makeText(Goods.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
            else if (msg.what == ADD_HATE_OK)//添加黑名单成功
                Toast.makeText(Goods.this, "加入黑名单成功", Toast.LENGTH_SHORT).show();
            else if (msg.what == ADD_STAR_OK)
                Toast.makeText(Goods.this, "加入收藏夹成功", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_goods);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.i(TAG, "成功跳转到商品页面");
        shop_id = bundle.getString("shop_id");
        goods_id = bundle.getString("goods_id");

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject req = new JSONObject();
                        try {
                            req.put("type", "LA_G");
                            req.put("id", MainActivity.idOfuser);
                            req.put("shop_id", shop_id);
                            req.put("goods_id", goods_id);
                            req.put("star", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String rst = getdata.post(req);//试图加入收藏夹
                        if (rst.equals("true")) {
                            Log.i(TAG, "run: 加入收藏夹成功");//无需立即刷新列表
                            handler.sendEmptyMessage(ADD_STAR_OK);
                        } else if (rst.equals("false")) {
                            Log.i(TAG, "run: 加入收藏夹失败");
                        }
                    }
                }).start();
            }
        });

        hate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "加入黑名单");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject req = new JSONObject();
                        try {
                            req.put("type", "LA_G");
                            req.put("id", MainActivity.idOfuser);
                            req.put("shop_id", shop_id);
                            req.put("goods_id", goods_id);
                            req.put("star", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String rst = getdata.post(req);//试图加入黑名单
                        if (rst.equals("true")) {
                            Log.i(TAG, "run: 加入黑名单成功");//无需立即刷新列表
                            handler.sendEmptyMessage(ADD_HATE_OK);
                        } else if (rst.equals("false")) {
                            Log.i(TAG, "run: 加入黑名单失败");
                            handler.sendEmptyMessage(GET_ERROR);
                        }
                    }
                }).start();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "加入购物车");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject req = new JSONObject();
                        try {
                            req.put("type", "CA");
                            req.put("id", MainActivity.idOfuser);
                            req.put("shop_id", shop_id);
                            req.put("goods_id", goods_id);
                            req.put("sum", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String rst = getdata.post(req);//试图加入购物车
                        if (rst.equals("true")) {
                            Log.i(TAG, "run: 加入购物车成功");
                            handler.sendEmptyMessage(ADD_CART_OK);
                        } else if (rst.equals("false")) {
                            Log.i(TAG, "run: 加入购物车失败");
                            handler.sendEmptyMessage(GET_ERROR);
                        }
                    }
                }).start();
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(Goods.this, Shop.class);
                intent1.putExtra("shop_id", shop_id);
                startActivity(intent1);
            }
        });
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }

}
