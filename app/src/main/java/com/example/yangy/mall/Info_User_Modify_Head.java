package com.example.yangy.mall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Info_User_Modify_Head extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private final static int REQUEST_CODE = 3;//请求标识

    private Intent intent;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info__user__modify__head);
        //TODO——头像列表
        //TODO——get头像ID
        //返回头像ID
        setResult(4, intent.putExtras(bundle));
        finish();
    }
}
