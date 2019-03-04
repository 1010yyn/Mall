package com.example.yangy.mall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class Info_User extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int RESULT_CODE = 1;

    private Button address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info__user);
        Log.i(TAG, "成功跳转到个人信息页面");

        address = findViewById(R.id.info_user_address_change);
        //TODO——显示用户信息

    }
}
