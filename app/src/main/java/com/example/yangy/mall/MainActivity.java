package com.example.yangy.mall;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String TAG = "MYTAG";
    Button btn_food, btn_cls, btn_mkup, btn_excs, btn_fur, btn_elc, btn_home, btn_cart;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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
            mainpage();//加载主界面
        } catch (Exception e) {
            Log.e(TAG, "加载主界面出现错误");
            Log.e(TAG, e.getMessage());
        }
    }

    void mainpage() {
        Log.i(TAG, "加载主界面");
        setContentView(R.layout.activity_main);
        btn_food = (Button) findViewById(R.id.Food);
        btn_cls = (Button) findViewById(R.id.Clothes);
        btn_mkup = (Button) findViewById(R.id.Makeup);
        btn_excs = (Button) findViewById(R.id.Exercise);
        btn_fur = (Button) findViewById(R.id.Furniture);
        btn_elc = (Button) findViewById(R.id.Electronic);
        btn_home = (Button) findViewById(R.id.home);
        btn_cart = (Button) findViewById(R.id.cart);
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
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——跳转页面（主页）
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.main_page);
        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "点击" + item.getTitle().toString());
                //TODO——点击切换界面
                if (item.getTitle().toString().equals("个人信息")) {
                    Log.i(TAG, "跳转个人信息界面");
                    //TODO——切换到个人信息界面
                } else if (item.getTitle().toString().equals("软件信息")) {
                    //TODO——切换到软件信息界面
                    Log.i(TAG, "跳转软件信息界面");
                } else if (item.getTitle().toString().equals("退出")) {
                    Log.i(TAG, "退出App");
                    onDestroy();
                }
                drawerLayout.closeDrawer(navigationView);//关闭菜单
                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        //TODO——保存退出前的状态
        super.onDestroy();
    }
}
