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

public class Info_User extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int REQUEST_CODE = 2;//请求标识
    private int GET_OK = 1;
    private int GET_ERROR = 0;

    private Intent intent2;
    private Bundle bundle = new Bundle();
    private TextView id, nickname, phone, address;
    private ImageView head;

    private String Head, Id, Nickname, Phone, Address;
    private CreateData getdata = new CreateData();

    private static String rst;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_OK) {
                rst = msg.obj.toString();
                try {
                    JSONObject js = new JSONObject(rst);
                    Head = js.getString("head");
                    Id = js.getString("name");
                    Nickname = js.getString("nick");
                    Phone = js.getString("phone");
                    Address = js.getString("address");
                    set();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(Info_User.this, "加载个人信息失败", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info__user);
        Log.i(TAG, "成功跳转到个人信息页面");
        //TODO——获取用户信息
        final JSONObject req = new JSONObject();
        try {
            req.put("type", "UG");
            req.put("id", MainActivity.idOfuser + "");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                rst = getdata.post(req);//连接服务器获取信息
                Log.i(TAG, "返回信息：" + rst);//查询数据库获取用户个人信息
                if (rst.equals("false"))//获取失败
                    handler.sendEmptyMessage(GET_ERROR);
                else {
                    Message msg = Message.obtain();
                    msg.obj = rst;
                    msg.what = GET_OK;
                    handler.sendMessage(msg);//发送信息给主线程
                }
            }
        }).start();
    }

    void set() {
        //获取组件
        head = findViewById(R.id.info_user_head);//获取头像imageview组件
        id = findViewById(R.id.info_user_name);
        nickname = findViewById(R.id.info_user_nickname);
        phone = findViewById(R.id.info_user_phone);
        address = findViewById(R.id.info_user_address);

        //初始化信息
        init();

        bundle.putCharSequence("id", Id);
        bundle.putCharSequence("nick", Nickname);
        bundle.putCharSequence("phone", Phone);
        bundle.putCharSequence("address", Address);
        bundle.putCharSequence("head", Head);

        //修改用户信息
        Button modify = findViewById(R.id.info_user_info_change);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "跳转到修改用户信息界面");
                intent2 = new Intent(Info_User.this, Info_User_Modify.class);
                intent2.putExtras(bundle);
                startActivityForResult(intent2, REQUEST_CODE, bundle);
            }
        });
    }

    void init() {
        //设置信息
        head.setImageResource(getResources().getIdentifier(Head, "drawable", getBaseContext().getPackageName()));//设置头像
        id.setText(Id);
        nickname.setText(Nickname);
        phone.setText(Phone);
        address.setText(Address);
    }

    @Override
    protected void onActivityResult(int reQuestCode, int resultCode, Intent data) {
        if (reQuestCode == REQUEST_CODE && resultCode == Info_User_Modify.REQUEST_CODE) {
            bundle = data.getExtras();
            Head = bundle.getString("head");
            Id = bundle.getString("id");
            Nickname = bundle.getString("nick");
            Phone = bundle.getString("phone");
            Address = bundle.getString("address");
            init();//更新信息
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        Intent intent1 = new Intent(Info_User.this, MainActivity.class);
        setResult(REQUEST_CODE, intent1.putExtras(bundle));
        finish();
    }

}
