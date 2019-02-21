package com.example.yangy.mall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    String TAG = "MYTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "开屏");
        setContentView(R.layout.start);
        //TODO——停留3s
        Log.i(TAG, "开屏停留3s");
        //TODO——链接数据库，加载主界面内容
        Log.i(TAG, "链接数据库");
        //加载主界面内容
        try {
            setContentView(R.layout.activity_main);
            Log.i(TAG, "主界面");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
