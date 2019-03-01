package com.example.yangy.mall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Info_User extends AppCompatActivity {

    private final static String TAG = "MYTAG";

    private Button address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info__user);
        address = findViewById(R.id.info_user_address_change);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "点击修改地址");
                //TODO——修改收货地址
            }
        });
        //TODO——显示用户信息
    }
}
