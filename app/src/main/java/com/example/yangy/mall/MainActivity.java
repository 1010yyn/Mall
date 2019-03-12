package com.example.yangy.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MYTAG";

    private final static int REQUEST_CODE = 1;//请求标识

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TabHost tabHost;
    private LayoutInflater inflater;

    private Button btn_food, btn_cls, btn_mkup, btn_excs, btn_fur, btn_elc;//商品分类Button
    private TextView name;
    private ImageView head;
    private RecyclerView list_goods, list_cart;//主页和购物车的商品列表
    private List<String> array_goods = new ArrayList<>();//主页商品列表信息存储
    private List<String> array_cart = new ArrayList<>();//购物车商品列表信息存储
    private AlertDialog.Builder delete_cart;//确认删除日程对话框

    private Intent intent;
    private Bundle bundle = new Bundle();

    private Data_Cart_Bean data_cart_bean;

    private int Head;//侧边栏头像
    private String Name, Head_Name;//侧边栏用户名,头像文件名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "开屏");
        setContentView(R.layout.start);
        //TODO——welcomeactivity切换
        Log.i(TAG, "开屏停留3s");
        //TODO——链接数据库，加载主界面内容
        Log.i(TAG, "链接数据库");
        //加载主界面内容
        homepage();//加载首页界面
        cart();//加载购物车界面
    }

    void homepage() {
        Log.i(TAG, "加载主界面");
        setContentView(R.layout.activity_main);

        //TODO——获取用户头像和昵称
        Name = "啊哦";
        Head_Name = "sample_info_user_head";
        Head = getResources().getIdentifier(Head_Name, "drawable", getBaseContext().getPackageName());

        //侧边栏
        drawerLayout = findViewById(R.id.main_page);
        navigationView = findViewById(R.id.nav);
        head = navigationView.getHeaderView(0).findViewById(R.id.header_head);
        name = navigationView.getHeaderView(0).findViewById(R.id.header_name);
        name.setText(Name);
        head.setImageResource(Head);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "点击" + item.getTitle().toString());
                switch (item.getTitle().toString()) {
                    case "个人信息":
                        intent = new Intent(MainActivity.this, Info_User.class);//为个人信息界面创建intent
                        Log.i(TAG, "正在跳转页面到个人信息页面");
                        startActivity(intent);
                        break;
                    case "软件信息":
                        Log.i(TAG, "跳转个人信息界面");
                        //TODO——切换到软件信息界面（最后做！）
                        break;
                    case "切换用户":
                        Log.i(TAG, "切换用户");
                        //TODO——切换到用户登录界面
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

        //选项卡
        tabHost = findViewById(android.R.id.tabhost);//获取tabhost
        tabHost.setup();
        inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.activity_main_home, tabHost.getTabContentView());//设置主页选项卡
        inflater.inflate(R.layout.activity_main_cart, tabHost.getTabContentView());//设置购物车选项卡
        tabHost.addTab(tabHost.newTabSpec("activity_main_home").setIndicator("首页").setContent(R.id.left));
        tabHost.addTab(tabHost.newTabSpec("activity_main_cart").setIndicator("购物车").setContent(R.id.right));

        //获取分类Button，设置监听
        btn_food = findViewById(R.id.Food);
        btn_cls = findViewById(R.id.Clothes);
        btn_mkup = findViewById(R.id.Makeup);
        btn_excs = findViewById(R.id.Exercise);
        btn_fur = findViewById(R.id.Furniture);
        btn_elc = findViewById(R.id.Electronic);
        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                intent.putExtra("str", "Food");
                Log.i(TAG, "正在跳转页面到食品类搜索结果页面");
                startActivity(intent);
            }
        });
        btn_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                intent.putExtra("str", "Clothes");
                Log.i(TAG, "正在跳转页面到衣饰类搜索结果页面");
                startActivity(intent);
            }
        });
        btn_mkup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                intent.putExtra("str", "Makeup");
                Log.i(TAG, "正在跳转页面到美妆类搜索结果界面");
                startActivity(intent);
            }
        });
        btn_excs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                intent.putExtra("str", "Exercise");
                Log.i(TAG, "正在跳转页面到运动类搜索结果");
                startActivity(intent);
            }
        });
        btn_fur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                intent.putExtra("str", "Furniture");
                Log.i(TAG, "正在跳转页面到家居类搜索结果");
                startActivity(intent);
            }
        });
        btn_elc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                intent.putExtra("str", "Electronic");
                Log.i(TAG, "正在跳转页面到电子产品类搜索结果");
                startActivity(intent);
            }
        });

        //获取主页list
        list_goods = findViewById(R.id.home_list);
        //init_list_goods();
        //TODO——设置单击事件
        //TODO——跳转页面显示商品详细信息
    }

    protected void cart() {
        //获取购物车listview
        list_cart = findViewById(R.id.cart_list_shop);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        list_cart.setLayoutManager(manager);
        List<Object> list = sortData(data_cart_bean);
        //initdata();
        // Cart_Shop_Item_adapter adapter = new Cart_Shop_Item_adapter(array_cart);
        //adapter.setOnRecyclerViewListener(this);
        //list_cart.setAdapter(adapter);


        //TODO——单击事件，长按事件
        //设置单击事件
//        list_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i(TAG, "单击购物车商品");
//                intent = new Intent(MainActivity.this, Goods.class);//为搜索结果界面创建intent
//                intent.putExtra("str", "Goods");
//                startActivity(intent);
//            }
//        });
        //设置长按监听,长按删除商品
//        list_cart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                Log.i(TAG, "长按购物车商品");
//                delete_cart = new AlertDialog.Builder(MainActivity.this);//确认删除对话框
//                delete_cart.setTitle("系统提示：");
//                delete_cart.setMessage("确定要删除该商品吗？");
//                delete_cart.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i(TAG, "取消删除");//不做操作
//                        dialog.dismiss();//关闭对话框
//                    }
//                });
//                delete_cart.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i(TAG, "确认删除");//删除该条记录
//                        //TODO——获取该条记录，从数据库删除，并刷新列表
//                    }
//                });
//                delete_cart.create().show();
//                return true;
//            }
//        });
    }

    private List<Object> sortData(Data_Cart_Bean bean) {
        List<Data_Shop_Bean> arrays = bean.getData();
        List<Object> arrays_obj = new ArrayList<>();
        return arrays_obj;
    }


    @Override
    protected void onActivityResult(int reQuestCode, int resultCode, Intent data) {
        if (reQuestCode == REQUEST_CODE) {
            if (resultCode == Show_result.RESULT_CODE) {
                Bundle bundle = data.getExtras();
                String str = bundle.getString("back");
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        //TODO——保存退出前的状态
        super.onDestroy();
    }
}
