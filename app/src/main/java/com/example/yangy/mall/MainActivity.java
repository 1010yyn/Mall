package com.example.yangy.mall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int REQUEST_CODE = 1;//请求标识

    private int ERROR = 0;
    private int GET_CART_OK = 1;//获取购物车数据OK
    private int GET_USER_OK = 2;//获取用户头像idOK
    private int DLT_CART_OK = 3;//删除购物车数据OK
    private int CART_EMPTY = 4;
    private int GET_HOME_OK = 5;//主页数据OK

    private int idOfuser = 1;//默认设置为0，等待用户登录
    private String address;//用户地址

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView name;//侧边栏用户昵称
    private ImageView head;//侧边栏用户头像

    //侧边栏用户名,头像文件名
    String name1, head_Name;
    int head1;

    private AlertDialog.Builder delete_cart;//确认删除购物车商品对话框
    private Button delete_goods;

    private Intent intent;
    private Bundle bundle = new Bundle();

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//购物车数据
    private Data_Cart_Bean data_cart_bean1 = new Data_Cart_Bean();//主页列表随机商品数据

    private CreateData createData = new CreateData();

    private boolean flag_cart = false;
    private boolean flag_user = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_CART_OK) {
                try {
                    Log.i(TAG, "handleMessage: 获取列表数据成功");
                    data_cart_bean = (Data_Cart_Bean) msg.obj;
                } catch (Exception e) {
                    Log.e(TAG, "handleMessage:转换数据失败 ");
                    e.printStackTrace();
                }
            } else if (msg.what == GET_USER_OK) {
                try {
                    JSONObject js = new JSONObject(msg.obj.toString());
                    name1 = js.getString("name");
                    head_Name = js.getString("head");
                    address = js.getString("address");
                    flag_user = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == CART_EMPTY) {
                data_cart_bean = null;
                data_cart_bean1 = null;
                flag_cart = true;
            } else if (msg.what == GET_HOME_OK) {
                data_cart_bean1 = (Data_Cart_Bean) msg.obj;
                flag_cart = true;
            } else if (msg.what == DLT_CART_OK) {
                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                flag_user = false;
            }
            if (flag_user && flag_cart)
                setData();//加载主界面内容
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "开屏");
        setContentView(R.layout.layout_start);
        Log.i(TAG, "开屏停留3s");
    }

    void setData() {
        homepage();//加载首页界面
        cart();//加载购物车界面
    }

    void homepage() {
        Log.i(TAG, "加载主界面");
        setContentView(R.layout.layout_main);

        //侧边栏头像id
        head1 = getResources().getIdentifier(head_Name, "drawable", getBaseContext().getPackageName());

        //侧边栏
        drawerLayout = findViewById(R.id.main_page);
        navigationView = findViewById(R.id.nav);
        head = navigationView.getHeaderView(0).findViewById(R.id.header_head);
        name = navigationView.getHeaderView(0).findViewById(R.id.header_name);
        name.setText(name1);
        head.setImageResource(head1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "点击" + item.getTitle().toString());
                switch (item.getTitle().toString()) {
                    case "个人信息":
                        intent = new Intent(MainActivity.this, Info_User.class);//为个人信息界面创建intent
                        intent.putExtra("id", idOfuser);
                        Log.i(TAG, "正在跳转页面到个人信息页面");
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                    case "收藏夹":
                        intent = new Intent(MainActivity.this, Star.class);//为个人信息界面创建intent
                        intent.putExtra("id", idOfuser);
                        Log.i(TAG, "正在跳转页面到收藏夹页面");
                        startActivity(intent);
                        break;
                    case "黑名单":
                        intent = new Intent(MainActivity.this, Hate.class);//为个人信息界面创建intent
                        intent.putExtra("id", idOfuser);
                        Log.i(TAG, "正在跳转页面到黑名单页面");
                        startActivity(intent);
                        break;
                    case "切换用户":
                        Log.i(TAG, "切换用户");
                        intent = new Intent(MainActivity.this, Login.class);
                        Log.i(TAG, "正在跳转至登陆界面");
                        startActivity(intent);
                        finish();
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
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.layout_main_home, tabHost.getTabContentView());//设置主页选项卡
        inflater.inflate(R.layout.layout_main_cart, tabHost.getTabContentView());//设置购物车选项卡
        tabHost.addTab(tabHost.newTabSpec("layout_main_home").setIndicator("首页").setContent(R.id.main_left));
        tabHost.addTab(tabHost.newTabSpec("layout_main_cart").setIndicator("购物车").setContent(R.id.main_right));

        //获取分类Button，设置监听
        Button btn_food = findViewById(R.id.Food);
        Button btn_cls = findViewById(R.id.Clothes);
        Button btn_mkup = findViewById(R.id.Makeup);
        Button btn_excs = findViewById(R.id.Exercise);
        Button btn_fur = findViewById(R.id.Furniture);
        //商品分类Button
        Button btn_elc = findViewById(R.id.Electronic);
        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                //intent.putExtra("key", "Food");
                intent.putExtra("key", "西瓜");
                intent.putExtra("id", idOfuser);
                Log.i(TAG, "正在跳转页面到食品类搜索结果页面");
                startActivity(intent);
            }
        });
        btn_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                //intent.putExtra("key", "Clothes");
                intent.putExtra("key", "西瓜");
                intent.putExtra("id", idOfuser);
                Log.i(TAG, "正在跳转页面到衣饰类搜索结果页面");
                startActivity(intent);
            }
        });
        btn_mkup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                //intent.putExtra("key", "Makeup");
                intent.putExtra("key", "西瓜");
                intent.putExtra("id", idOfuser);
                Log.i(TAG, "正在跳转页面到美妆类搜索结果界面");
                startActivity(intent);
            }
        });
        btn_excs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                //intent.putExtra("key", "Exercise");
                intent.putExtra("key", "西瓜");
                intent.putExtra("id", idOfuser);
                Log.i(TAG, "正在跳转页面到运动类搜索结果");
                startActivity(intent);
            }
        });
        btn_fur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                //intent.putExtra("key", "Furniture");
                intent.putExtra("key", "西瓜");
                intent.putExtra("id", idOfuser);
                Log.i(TAG, "正在跳转页面到家居类搜索结果");
                startActivity(intent);
            }
        });
        btn_elc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Show_result.class);//为搜索结果界面创建intent
                //intent.putExtra("key", "Electronic");
                intent.putExtra("key", "西瓜");
                intent.putExtra("id", idOfuser);
                Log.i(TAG, "正在跳转页面到电子产品类搜索结果");
                startActivity(intent);
            }
        });

        search();//搜索栏

        //获取主页list
        RecyclerView list_goods = findViewById(R.id.home_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = sortData(data_cart_bean1);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_goods.setLayoutManager(manager);
        final Goods_Item_adapter adapter = new Goods_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_goods.addItemDecoration(divider);
        list_goods.setFocusable(false);//解决数据加载完成后, 没有停留在顶部的问题

        list_goods.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品");
                intent = new Intent(MainActivity.this, Goods.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
                bundle.putCharSequence("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
                bundle.putInt("id", idOfuser);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    protected void search() {
        final SearchView searchView = findViewById(R.id.search_bar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus(); // 不获取焦点
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                }
                Log.i(TAG, "进行搜索，关键词为：" + query);
                intent = new Intent(MainActivity.this, Show_result.class);//跳转到搜索结果页
                intent.putExtra("key", query);
                intent.putExtra("id", idOfuser);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String selection = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " LIKE '%" + newText + "%' " + " OR "
                        + ContactsContract.RawContacts.SORT_KEY_PRIMARY + " LIKE '%" + newText + "%' ";
                return true;
            }
        });
    }

    protected void cart() {
        RecyclerView list_cart = findViewById(R.id.cart_list_shop); //获取recycleview
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_cart.setLayoutManager(manager);
        final Cart_Shop_Item_adapter adapter = new Cart_Shop_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_cart.addItemDecoration(divider);

        list_cart.setAdapter(adapter);

        //设置单击事件
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.cart_shop_title__title:
                        Log.i(TAG, "单击店铺" + position);
                        intent = new Intent(MainActivity.this, Shop.class);//为店铺结果界面创建intent
                        intent.putExtra("name", (String) adapter.getItem(position));
                        intent.putExtra("id", idOfuser);
                        startActivity(intent);
                        break;
                    case R.id.cart_shop_goods__photo:
                    case R.id.cart_shop_goods__name:
                        Log.i(TAG, "单击购物车商品" + position);
                        intent = new Intent(MainActivity.this, Goods.class);//为商品结果界面创建intent
                        bundle = new Bundle();//清空数据
                        bundle.putCharSequence("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
                        bundle.putCharSequence("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
                        bundle.putInt("id", idOfuser);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
        //设置长按监听,长按删除商品
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                Log.i(TAG, "长按购物车商品" + position);
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
                        delete_cart_goods((Cart_Shop_Item_adapter) adapter, position);
                    }
                });
                delete_cart.create().show();
                return true;
            }
        });

        //购物车商品批量删除，
        delete_goods = findViewById(R.id.cart_delete);
        delete_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //遍历列表将选中项目一并remove
                for (int i = adapter.getItemCount(); i > 0; i--) {
                    if (adapter.getItemViewType(i) == 2)//商品item
                        if (((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(i)).getStatus())//选中状态
                            delete_cart_goods(adapter, i);//删除item
                }
            }
        });
        //购物车商品结算
        Button calculate_goods = findViewById(R.id.cart_calculate);
        calculate_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> rst = new ArrayList<>();//商品list
                for (int i = adapter.getItemCount(); i > 0; i--)
                    if (adapter.getItemViewType(i) == 2)//商品item
                        if (((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(i)).getStatus())//选中状态
                            rst.add((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(i));//加入列表
                intent = new Intent(MainActivity.this, Order.class);
                bundle = new Bundle();//清空数据
                bundle.putSerializable("list", rst);
                bundle.putString("address", address);
                bundle.putInt("id", idOfuser);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    protected void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表
                try {
                    JSONObject req = new JSONObject();
                    req.put("type", "CG");
                    req.put("id", idOfuser);
                    ArrayList<String> rst = createData.post_m(req);

                    JSONObject goods;//存储查询商品简略信息
                    int shop = -1;//记录shopid，便于合并商品

                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean = null;//临时店铺信息
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans = null;//临时商品列表

                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    int i = 0;
                    while (i < rst.size()) {
                        goods = new JSONObject(rst.get(i));//转换数据
                        //判定是否同一家店商品
                        //不是同一家店，则先将商店加入购物车列表然后新建商店，否则不做处理
                        if (shop != goods.getInt("shop_id")) {
                            //将店铺加入临时店铺列表
                            if (i != 0)//若是第一件商品，则需要创建新商店item，否则，将先当前商店加入购物车列表
                            {//将商店加入购物车内商店列表
                                shop_bean.setGoodsData(data_goods_beans);//将商品列表加入店铺信息
                                data_shop_beans.add(shop_bean);//将商店加入购物车列表
                            }
                            //创建新商店和商店商品列表并初始化
                            data_goods_beans = new ArrayList<>();//商店商品列表初始化
                            shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化
                            shop_bean.setName(goods.getString("shop_name"));//设定商店名
                            shop_bean.setId(goods.getString("shop_id"));//设定商店id
                            //更新当前商店id
                            shop = Integer.parseInt(goods.getString("shop_id"));
                        }
                        //添加商品信息，加入商店商品列表
                        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean.setShopname(goods.getString("shop_name"));//设定商店名称
                        goods_bean.setShopid(goods.getString("shop_id"));//设定商店id
                        goods_bean.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean.setSum(goods.getString("sum"));//设定数量
                        goods_bean.setPhoto(goods.getString("photo"));//获取图片资源
                        goods_bean.setDescription(goods.getString("description"));//设定商品描述
                        //添加商品至临时商品列表
                        data_goods_beans.add(goods_bean);//购物车
                        i++;
                    }
                    //如果购物车非空
                    if (rst.size() > 0) {
                        //当前店铺还未进行加入购物车操作，跳出循环后继续
                        shop_bean.setGoodsData(data_goods_beans);//将商品列表加入店铺信息
                        data_shop_beans.add(shop_bean);//将商店加入购物车列表
                        //将所有的店铺列表加入购物车
                        data_cart_bean.setShopData(data_shop_beans);
                        Message msg = Message.obtain();
                        msg.obj = data_cart_bean;
                        msg.what = GET_CART_OK;
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(CART_EMPTY);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans1 = new ArrayList<>();//临时店铺列表
                //获取首页商品列表
                try {
                    JSONObject req = new JSONObject();
                    req.put("type", "HG");
                    ArrayList<String> rst = createData.post_m(req);

                    JSONObject goods = null;//存储查询商品简略信息

                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean1;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean1;//临时店铺信息（主页用
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans1;//临时商品列表（主页列表用
                    Data_Cart_Bean data_cart_bean1 = new Data_Cart_Bean();//网络请求成功返回的Bean对象(主页列表用

                    data_goods_beans1 = new ArrayList<>();//商店商品列表初始化
                    shop_bean1 = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化

                    int i = 0;
                    while (i < rst.size()) {
                        goods = new JSONObject(rst.get(i));//转换数据
                        //主页
                        goods_bean1 = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean1.setShopname(goods.getString("shop_name"));//设定商店名称
                        goods_bean1.setShopid(goods.getString("shop_id"));//设定商店id
                        goods_bean1.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean1.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean1.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean1.setSum("");//设定数量为空
                        goods_bean1.setPhoto(goods.getString("photo"));//获取图片资源
                        goods_bean1.setDescription(goods.getString("description"));//设定商品描述
                        //添加商品至临时商品列表
                        data_goods_beans1.add(goods_bean1);//主页展示列表
                        i++;
                    }
                    //主页
                    if (rst.size() > 0) {
                        shop_bean1.setName(goods.getString("shop_name"));//设定商店名
                        shop_bean1.setId(goods.getString("shop_id"));//设定商店id
                        shop_bean1.setGoodsData(data_goods_beans1);//将商品列表加入店铺信息
                        data_shop_beans1.add(shop_bean1);//将商店加入购物车列表
                        //将所有的店铺列表加入购物车
                        data_cart_bean1.setShopData(data_shop_beans1);
                        Message msg = Message.obtain();
                        msg.obj = data_cart_bean1;
                        msg.what = GET_HOME_OK;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //获取用户头像和昵称
                try {
                    JSONObject req = new JSONObject();
                    req.put("type", "UG");
                    req.put("id", idOfuser + "");
                    Log.i(TAG, "run: 试图获取个人信息");
                    Message msg = Message.obtain();
                    msg.obj = createData.post_m(req).get(0);
                    msg.what = GET_USER_OK;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void delete_cart(final JSONObject req) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String rst = createData.post_m(req).get(0);
                if (rst.equals("true")) {
                    handler.sendEmptyMessage(DLT_CART_OK);
                }
            }
        }).start();
    }

    private void delete_cart_goods(Cart_Shop_Item_adapter adapter, int position) {
        //删除数据库中数据
        final JSONObject req = new JSONObject();
        try {
            req.put("type", "CD");
            req.put("id", idOfuser);
            req.put("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
            req.put("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //删除列表中数据
        if ((adapter.getItemViewType(position - 1) == 1 && adapter.getItemViewType(position + 1) == 1) || (adapter.getItemViewType(position - 1) == 1 && adapter.getItemCount() == position + 1)) {
            Log.i(TAG, "该商品上下条目是店铺title");
            adapter.remove(position);//删除商品item
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            adapter.remove(position - 1);//删除店铺title
            adapter.notifyItemRemoved(position - 1);
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
        } else {
            Log.i(TAG, "该商品上下条目不是店铺title");
            adapter.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
        }
        delete_cart(req);
    }

    public static List<Object> sortData(Data_Cart_Bean bean) {
        if (bean == null)
            return null;
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
        if (reQuestCode == REQUEST_CODE && resultCode == Info_User.REQUEST_CODE) {
            Log.i(TAG, "个人信息——返回主页");
            bundle = data.getExtras();
            head.setImageResource(getResources().getIdentifier(bundle.getString("head"), "drawable", getBaseContext().getPackageName()));//重新设置头像
            name.setText(bundle.getString("nick"));
        } else if (reQuestCode == REQUEST_CODE && resultCode == Login.REQUEST_CODE) {
            Log.i(TAG, "登录成功！");
            bundle = data.getExtras();
            idOfuser = Integer.valueOf(bundle.getString("id"));//获取用户id;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            flag_user = false;
            flag_cart = false;
            getData();//获取用户数据
        } catch (Exception e) {
            Log.e(TAG, "加载主界面失败");
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
