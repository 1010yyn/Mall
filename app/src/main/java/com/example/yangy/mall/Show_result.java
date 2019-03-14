package com.example.yangy.mall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Show_result extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_result);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = bundle.getString("str");
        Log.i(TAG, "成功跳转到搜索结果页面，本次搜索关键词为：" + str);
        //TODO——根据关键词检索数据库
    }
}
