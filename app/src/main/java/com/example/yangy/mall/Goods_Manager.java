package com.example.yangy.mall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class Goods_Manager extends AppCompatActivity {

    private final static String TAG = "MYTAG";

    private int GET_ERROR = 0;
    private int GET_OK = 1;//获取商品信息OK
    private int UPDATE_OK = 2;//商品信息修改/添加OK
    private int UPDATE_ERROR = 3;//失败

    private CreateData getdata = new CreateData();

    private String name1, price1, description1, photo1, sum1, tag1;
    private String type, shop_id, goods_id, shop_name;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_OK) {
                try {
                    JSONObject goods = new JSONObject(msg.obj.toString());
                    name1 = goods.getString("goods_name");
                    photo1 = goods.getString("photo");
                    price1 = goods.getString("price");
                    description1 = goods.getString("description");
                    sum1 = goods.getString("sum");
                    setData();
                } catch (JSONException e) {
                    Log.e(TAG, "handleMessage: 解析商品数据失败");
                    e.printStackTrace();
                }
            } else if (msg.what == UPDATE_OK) {
                if (type.equals("edit"))
                    Toast.makeText(Goods_Manager.this, "商品信息更新成功！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Goods_Manager.this, "商品信息添加成功！", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == UPDATE_ERROR) {
                Toast.makeText(Goods_Manager.this, "操作失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_goods__manager);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.i(TAG, "成功跳转到修改商品信息页面");
        type = bundle.getString("type");
        JSONObject req = new JSONObject();
        shop_id = bundle.getString("shop_id");
        shop_name = bundle.getString("shop_name");
        goods_id = bundle.getString("goods_id");
        switch (type) {
            case "new":
                photo1 = "";
                name1 = "";
                sum1 = "";
                price1 = "";
                description1 = "";
                tag1 = "";
                setData();
                break;
            case "edit":
                try {
                    req.put("type", "GG");
                    req.put("shop_id", bundle.getString("shop_id"));
                    req.put("goods_id", bundle.getString("goods_id"));
                    getData(req);//获取商品数据
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
        }

    }

    void getData(final JSONObject req) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String rst = getdata.post_m(req).get(0);//尝试获取商品信息
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

    void setData() {
        byte[] bt = Base64.decode(photo1, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);

        final ImageView photo = findViewById(R.id.goods_manager__photo);
        final EditText name = findViewById(R.id.goods_manager__name);
        final EditText price = findViewById(R.id.goods_manager__price);
        final EditText sum = findViewById(R.id.goods_manager__sum);
        final EditText description = findViewById(R.id.goods_manager__description);
        final EditText tag = findViewById(R.id.goods_manager__tag);

        photo.setImageBitmap(bitmap);
        name.setText(name1);
        price.setText(price1);
        sum.setText(sum1);
        description.setText(description1);
        tag.setText(tag1);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——单击上传相册图片
                Log.i(TAG, "上传图片");
            }
        });

        Button ok = findViewById(R.id.goods_manager__ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) photo.getBackground()).getBitmap();
                try {
                    Log.i(TAG, "确认");
                    JSONObject req = new JSONObject();
                    if (type.equals("new"))
                        req.put("type", "GA");//商品添加
                    else {
                        req.put("type", "GU");//商品信息修改
                        req.put("goods_id", goods_id);
                    }
                    req.put("shop_id", shop_id);
                    req.put("goods_name", name.getText());
                    req.put("shop_name", shop_name);
                    req.put("price", price.getText());
                    req.put("sum", sum.getText());
                    req.put("photo", bitmap);//TODO——图片数据类型转换
                    req.put("description", description.getText());
                    req.put("tag", tag.getText());
                    update(req);//添加/修改商品信息
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void update(final JSONObject req) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String rst = getdata.post_m(req).get(0);
                if (rst.equals("true"))
                    handler.sendEmptyMessage(UPDATE_OK);
                else handler.sendEmptyMessage(UPDATE_ERROR);
            }
        }).start();
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }

}
