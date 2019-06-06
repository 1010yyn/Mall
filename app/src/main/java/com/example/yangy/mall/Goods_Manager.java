package com.example.yangy.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class Goods_Manager extends AppCompatActivity {

    private final static String TAG = "MYTAG";

    private int GET_ERROR = 0;
    private int GET_OK = 1;//获取商品信息OK
    private int UPDATE_OK = 2;//商品信息修改/添加OK
    private int UPDATE_ERROR = 3;//失败
    private int ADD = 4;//转换图片成功

    private static final int CROP_PHOTO = 5;//裁剪图片
    private static final int LOCAL_CROP = 6;//本地图库

    private Intent intent1;

    private CreateData getdata = new CreateData();

    private ImageView photo;
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
                    tag1 = goods.getString("tag");
                    setData(1);//加载商品数据——1
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
            } else if (msg.what == ADD) {
                photo1 = (String) msg.obj;//设置photo
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
                name1 = "";
                sum1 = "";
                price1 = "";
                description1 = "";
                tag1 = "";
                setData(0);//新建——0
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

    void setData(int n) {
        photo = findViewById(R.id.goods_manager__photo);
        final EditText name = findViewById(R.id.goods_manager__name);
        final EditText price = findViewById(R.id.goods_manager__price);
        final EditText sum = findViewById(R.id.goods_manager__sum);
        final EditText description = findViewById(R.id.goods_manager__description);
        final EditText tag = findViewById(R.id.goods_manager__tag);

        if (n == 1) {
            byte[] bt = Base64.decode(photo1, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
            photo.setImageBitmap(bitmap);
        } else photo.setImageResource(R.drawable.photo_sample);

        name.setText(name1);
        price.setText(price1);
        sum.setText(sum1);
        description.setText(description1);
        tag.setText(tag1);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "上传图片");
                photo();
            }
        });

        Button ok = findViewById(R.id.goods_manager__ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(TAG, "确认");
                    //处理图片
                    photo.setDrawingCacheEnabled(true);
                    Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();//获取头像图片
                    photo.setDrawingCacheEnabled(false);
                    //bitmap转file
                    Calendar cld = Calendar.getInstance();
                    cld.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));//设置时区
                    String time = cld.get(Calendar.YEAR) + String.valueOf(cld.get(Calendar.MONTH) + 1) + cld.get(Calendar.DATE) + String.valueOf(cld.get(Calendar.HOUR) + 12) + cld.get(Calendar.MINUTE) + cld.get(Calendar.SECOND) + cld.get(Calendar.MILLISECOND);
                    //生成文件名
                    String path = "/storage/emulated/0/" + time + ".jpg";
                    File file = new File(path);//将要保存图片的路径
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();

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
                    req.put("photo", "D:/Courses/Pics/" + time + ".jpg");
                    req.put("description", description.getText());
                    req.put("tag", tag.getText());

                    update(req, file, path);//添加/修改商品信息
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void update(final JSONObject req, final File file, final String imagePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getdata.post_p(imagePath);
                String rst = getdata.post_m(req).get(0);
                if (rst.equals("true")) {
                        handler.sendEmptyMessage(UPDATE_OK);
                } else handler.sendEmptyMessage(UPDATE_ERROR);
            }
        }).start();
    }

    void photo() {
        CharSequence[] items = {"图库"};//裁剪items选项

        new AlertDialog.Builder(Goods_Manager.this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://选择图库中文件
                        intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent1, LOCAL_CROP);
                        break;
                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCAL_CROP:
                if (resultCode == RESULT_OK) {
                    // 创建intent用于裁剪图片
                    Intent intent1 = new Intent("com.android.camera.action.CROP");
                    // 获取图库所选图片的uri
                    Uri uri = data.getData();
                    intent1.setDataAndType(uri, "image/*");
                    //  设置裁剪图片的宽高
                    intent1.putExtra("outputX", 300);
                    intent1.putExtra("outputY", 300);
                    // 裁剪后返回数据
                    intent1.putExtra("return-data", true);
                    // 启动intent，开始裁剪
                    startActivityForResult(intent1, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 展示图库中选择裁剪后的图片
                        if (data != null) {
                            // 根据返回的data，获取Bitmap对象
                            Bitmap bitmap = data.getExtras().getParcelable("data");
                            // 展示图片
                            photo.setImageBitmap(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }

}
