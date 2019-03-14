package com.example.yangy.mall;

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
import android.widget.Button;
import android.widget.ImageView;
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
    private AlertDialog.Builder delete_cart;//确认删除日程对话框

    private Intent intent;
    private Bundle bundle = new Bundle();

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的OpenRecordBean对象

    private int Head;//侧边栏头像
    private String Name, Head_Name;//侧边栏用户名,头像文件名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "开屏");
        setContentView(R.layout.layout_start);
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
        setContentView(R.layout.layout_main);

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
        inflater.inflate(R.layout.layout_main_home, tabHost.getTabContentView());//设置主页选项卡
        inflater.inflate(R.layout.layout_main_cart, tabHost.getTabContentView());//设置购物车选项卡
        tabHost.addTab(tabHost.newTabSpec("layout_main_home").setIndicator("首页").setContent(R.id.left));
        tabHost.addTab(tabHost.newTabSpec("layout_main_cart").setIndicator("购物车").setContent(R.id.right));

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

        //TODO——获取购物车数据
        getdata_cart();

        //获取recycleview
        list_cart = findViewById(R.id.cart_list_shop);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = sortData(data_cart_bean);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_cart.setLayoutManager(manager);
        Cart_Shop_Item_adapter adapter = new Cart_Shop_Item_adapter(list);
        list_cart.setAdapter(adapter);

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
        List<Data_Cart_Bean.Data_Shop_Bean> arrays = bean.getShopData();
        // 用来进行数据重组的新的集合arrays_obj，之所以泛型设为Object，是因为该例中的集合元素既可能为String有可能是一个bean
        List<Object> arrays_obj = new ArrayList<>();
        for (Data_Cart_Bean.Data_Shop_Bean array : arrays) {
            List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> logs = array.getGoodsData();//商品列表
            // 拿到String值添加进集合arrays_obj
            arrays_obj.add(array.getName());
            // 如果该标题下的集合里面有数据的话，遍历拿到添加进新集合arrays_obj
            if (logs != null && logs.size() > 0) {
                for (Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean log : logs) {
                    arrays_obj.add(log);
                }
            }
        }
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

    private void getdata_cart() {
        //临时存储信息
        Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
        Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
        List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表
        List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<Data_Cart_Bean.Data_Shop_Bean>();//临时店铺列表

        data_goods_beans = new ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean>();
        //设置商品1信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setName("123");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(getResources().getIdentifier(Head_Name, "drawable", getBaseContext().getPackageName()));
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品2信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setName("541");
        goods_bean.setPrice(432);
        goods_bean.setSum(3);
        goods_bean.setPhoto(getResources().getIdentifier(Head_Name, "drawable", getBaseContext().getPackageName()));
        //添加商品至临时商品列表1
        data_goods_beans.add(goods_bean);

        //设置临时商铺信息1
        shop_bean = new Data_Cart_Bean.Data_Shop_Bean();
        shop_bean.setName("一家店");
        //将商品列表加入临时店铺1
        shop_bean.setGoodsData(data_goods_beans);

        //将店铺加入临时店铺列表
        data_shop_beans.add(shop_bean);


        data_goods_beans = new ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean>();
        //设置商品3信息
        goods_bean.setName("324");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(getResources().getIdentifier(Head_Name, "drawable", getBaseContext().getPackageName()));
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品4信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setName("793");
        goods_bean.setPrice(432);
        goods_bean.setSum(3);
        goods_bean.setPhoto(getResources().getIdentifier(Head_Name, "drawable", getBaseContext().getPackageName()));
        //添加商品至临时商品列表2
        data_goods_beans.add(goods_bean);

        //设置临时商铺信息2
        shop_bean = new Data_Cart_Bean.Data_Shop_Bean();
        shop_bean.setName("另一家店");
        //将商品列表加入临时店铺2
        shop_bean.setGoodsData(data_goods_beans);

        //将店铺加入临时店铺列表
        data_shop_beans.add(shop_bean);

        //设置购物车内店铺数量
        data_cart_bean.setAmount(1);
        //将店铺列表加入购物车
        data_cart_bean.setShopData(data_shop_beans);


    }


    @Override
    protected void onDestroy() {
        //TODO——保存退出前的状态
        super.onDestroy();
    }
}
