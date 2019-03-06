package com.example.yangy.mall;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.jar.Attributes;

public class Info_User extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private final static int REQUEST_CODE = 2;//请求标识

    private Button modify;
    private Intent intent;
    private Bundle bundle = new Bundle();
    private TextView id, nickname, phone, address;
    private ImageView head;

    private String Head, Id, Nickname, Phone, Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info__user);
        Log.i(TAG, "成功跳转到个人信息页面");

        //TODO——获取用户信息
        Head = "R.drawable.sample_info_user_head";
        Id = "是狗蛋本人";
        Nickname = "啊哦";
        Phone = "177****5127";
        Address = "XXXX/XXXX/XXX";

        //获取组件
        head = findViewById(R.id.info_user_head);//获取头像imageview组件
        id = findViewById(R.id.info_user_name);
        nickname = findViewById(R.id.info_user_nickname);
        phone = findViewById(R.id.info_user_phone);
        address = findViewById(R.id.info_user_address);

        //初始化信息
        init();

        //修改用户信息
        modify = findViewById(R.id.info_user_info_change);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "跳转到修改用户信息界面");
                intent = new Intent(Info_User.this, Info_User_Modify.class);
                bundle.putCharSequence("id", Id);
                bundle.putCharSequence("nick", Nickname);
                bundle.putCharSequence("phone", Phone);
                bundle.putCharSequence("address", Address);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE, bundle);
                //TODO——修改用户信息
            }
        });

    }

    @Override
    protected void onActivityResult(int reQuestCode, int resultCode, Intent data) {
        if (reQuestCode == REQUEST_CODE && resultCode == 3) {
            bundle = data.getExtras();
            Head = bundle.getString("head");
            Id = bundle.getString("id");
            Nickname = bundle.getString("nick");
            Phone = bundle.getString("phone");
            Address = bundle.getString("address");
            init();//更新信息
        }
    }

    void init() {
        //设置信息
        head.setImageResource(R.drawable.sample_info_user_head);//设置头像
        id.setText(Id);
        nickname.setText(Nickname);
        phone.setText(Phone);
        address.setText(Address);
    }
}
