package com.example.yangy.mall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class Goods extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private final static int REQUEST_CODE = 5;//请求标识
    private Intent intent, intent1;
    private Bundle bundle = new Bundle();

    private ImageView photo;
    private TextView name, price, description;
    private Button star, hate, cart, shop;

    private String Name, Price, Description, Shop;
    private int Photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_goods);
        intent = getIntent();
        bundle = intent.getExtras();
        Name = bundle.getString("name");
        Price = bundle.getString("price");
        Description = bundle.getString("description");
        Shop = bundle.getString("shop");
        Photo = bundle.getInt("photo");

        Log.i(TAG, "成功跳转到商品页面");
        photo = findViewById(R.id.goods_photo);
        name = findViewById(R.id.goods_name);
        price = findViewById(R.id.goods_price);
        description = findViewById(R.id.goods_description);

        photo.setImageResource(Photo);
        name.setText(Name);
        price.setText(Price);
        description.setText(Description);

        star = findViewById(R.id.goods_star);
        hate = findViewById(R.id.goods_hate);
        cart = findViewById(R.id.goods_cart);
        shop = findViewById(R.id.goods_shop);

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
