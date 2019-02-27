package com.example.yangy.mall;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "MYTAG";
    Button btn_food, btn_cls, btn_mkup, btn_excs, btn_fur, btn_elc;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Button menu;

    private int id;


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
            //mainpage();//加载主界面
            setContentView(R.layout.test);
            drawerLayout = (DrawerLayout) findViewById(R.id.activity_na);
            navigationView = (NavigationView) findViewById(R.id.nav);
            menu = (Button) findViewById(R.id.Food);
            View headerView = navigationView.getHeaderView(0);//获取头布局
            menu.setOnClickListener(this);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //item.setChecked(true);
                    Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(navigationView);
                    return true;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "加载主界面出现错误");
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Food://点击菜单，跳出侧滑菜单
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                break;
        }
    }

    void mainpage() {
        Log.i(TAG, "主界面");
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        btn_food = (Button) findViewById(R.id.Food);
        btn_cls = (Button) findViewById(R.id.Clothes);
        btn_mkup = (Button) findViewById(R.id.Makeup);
        btn_excs = (Button) findViewById(R.id.Exercise);
        btn_fur = (Button) findViewById(R.id.Furniture);
        btn_elc = (Button) findViewById(R.id.Electronic);
        //获取Button，设置监听
        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——跳转页面（食品类）
            }
        });
        btn_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——跳转页面（衣饰类）
            }
        });
        btn_mkup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——跳转页面（美妆类）
            }
        });
        btn_excs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——跳转页面（运动类）
            }
        });
        btn_fur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——跳转页面（家居类）
            }
        });
        btn_elc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——跳转页面（电子产品类）
            }
        });


    }
}
