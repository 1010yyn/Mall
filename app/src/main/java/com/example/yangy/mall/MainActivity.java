package com.example.yangy.mall;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String TAG = "MYTAG";
    Button btn_food, btn_cls, btn_mkup, btn_excs, btn_fur, btn_elc;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TabHost tabHost;
    private LayoutInflater inflater;
    private ListView list_goods, list_cart;
    private List<String> array_goods = new ArrayList<>();//商品列表
    private List<String> array_cart = new ArrayList<>();//购物车列表
    private AlertDialog.Builder delete_cart;//确认删除日程对话框

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

        //侧边栏
        drawerLayout = findViewById(R.id.main_page);
        navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "点击" + item.getTitle().toString());
                switch (item.getTitle().toString()) {
                    case "个人信息":
                        Log.i(TAG, "跳转个人信息界面");
                        //TODO——切换到个人信息界面
                        break;
                    case "软件信息":
                        Log.i(TAG, "跳转个人信息界面");
                        //TODO——切换到软件信息界面
                        break;
                    case "退出":
                        Log.i(TAG, "退出App");
                        onDestroy();
                        break;
                }
                drawerLayout.closeDrawer(navigationView);//关闭菜单
                return true;
            }
        });

        try {
            //选项卡
            tabHost = findViewById(android.R.id.tabhost);//获取tabhost
            tabHost.setup();
            inflater = LayoutInflater.from(this);
            inflater.inflate(R.layout.layout_home, tabHost.getTabContentView());//设置主页选项卡
            inflater.inflate(R.layout.layout_cart, tabHost.getTabContentView());//设置购物车选项卡
            tabHost.addTab(tabHost.newTabSpec("layout_home").setIndicator("首页").setContent(R.id.left));
            tabHost.addTab(tabHost.newTabSpec("layout_cart").setIndicator("购物车").setContent(R.id.right));
        } catch (Exception e) {
            Log.e(TAG, "选项卡加载错误");
        }

        //获取Button，设置监听
        btn_food = findViewById(R.id.Food);
        btn_cls = findViewById(R.id.Clothes);
        btn_mkup = findViewById(R.id.Makeup);
        btn_excs = findViewById(R.id.Exercise);
        btn_fur = findViewById(R.id.Furniture);
        btn_elc = findViewById(R.id.Electronic);
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

        //获取主页listview
        list_goods = findViewById(R.id.home_list);
        //设置单击事件
        list_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "单击商品");
                //TODO——跳转页面显示商品详细信息
            }
        });

        //获取购物车listview
        list_cart = findViewById(R.id.cart_list);
        //设置单击事件
        list_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "单击购物车商品");
                //TODO——显示商品详细信息
            }
        });
        //设置长按监听,长按删除商品
        list_cart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i(TAG, "长按购物车商品");
                delete_cart = new AlertDialog.Builder(MainActivity.this);//确认删除对话框
                delete_cart.setTitle("系统提示：");
                delete_cart.setMessage("确定要删除该商品吗？");
                delete_cart.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "取消删除");//不做操作
                        dialog.dismiss();//关闭对话框
                    }
                });
                delete_cart.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "确认删除");//删除该条记录
                        //TODO——获取该条记录，从数据库删除，并刷新列表
                    }
                });
                delete_cart.create().show();
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
