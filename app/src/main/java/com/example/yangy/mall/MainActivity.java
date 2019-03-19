package com.example.yangy.mall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int REQUEST_CODE = 1;//请求标识

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView name;//侧边栏用户昵称
    private ImageView head;//侧边栏用户头像

    private AlertDialog.Builder delete_cart;//确认删除购物车商品对话框
    private Button delete_goods;

    private Intent intent;
    private Bundle bundle = new Bundle();

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的OpenRecordBean对象
    private CreateData createData = new CreateData();

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
        String name1 = "啊哦";
        //侧边栏用户名,头像文件名
        String head_Name = "head5";
        //侧边栏头像id
        int head1 = getResources().getIdentifier(head_Name, "drawable", getBaseContext().getPackageName());

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
                        Log.i(TAG, "正在跳转页面到个人信息页面");
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                    case "收藏夹":
                        intent = new Intent(MainActivity.this, Star.class);//为个人信息界面创建intent
                        Log.i(TAG, "正在跳转页面到收藏夹页面");
                        startActivity(intent);
                        break;
                    case "黑名单":
                        intent = new Intent(MainActivity.this, Hate.class);//为个人信息界面创建intent
                        Log.i(TAG, "正在跳转页面到黑名单页面");
                        startActivity(intent);
                        break;
                    case "软件信息":
                        Log.i(TAG, "跳转软件信息界面");
                        //TODO——切换到软件信息界面（最后做！）
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

        search();//搜索栏

        //TODO——获取商品列表数据
        data_cart_bean = createData.getdata(this.getBaseContext());//所有商品放置到同一个店铺中便于展示
        //获取主页list
        RecyclerView list_goods = findViewById(R.id.home_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
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
                bundle.putCharSequence("name", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getName());
                bundle.putInt("photo", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getPhoto());
                bundle.putCharSequence("price", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getPrice());
                bundle.putCharSequence("description", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getDescription());
                bundle.putCharSequence("shop", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopname());
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
                intent.putExtra("name", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String selection = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " LIKE '%" + newText + "%' " + " OR "
                        + ContactsContract.RawContacts.SORT_KEY_PRIMARY + " LIKE '%" + newText + "%' ";
                // String[] selectionArg = { queryText };
                //mCursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, PROJECTION, selection, null, null);
                //mAdapter.swapCursor(mCursor); // 交换指针，展示新的数据
                return true;
            }
        });
    }

    protected void cart() {

        //TODO——获取购物车数据
        data_cart_bean = createData.getdata(this.getBaseContext());

        //获取recycleview
        //主页和购物车的商品列表
        RecyclerView list_cart = findViewById(R.id.cart_list_shop);
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
                        startActivity(intent);
                        break;
                    case R.id.cart_shop_goods__photo:
                    case R.id.cart_shop_goods__name:
                        Log.i(TAG, "单击购物车商品" + position);
                        intent = new Intent(MainActivity.this, Goods.class);//为商品结果界面创建intent
                        bundle = new Bundle();//清空数据
                        bundle.putCharSequence("name", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getName());
                        bundle.putInt("photo", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getPhoto());
                        bundle.putCharSequence("price", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getPrice());
                        bundle.putCharSequence("description", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getDescription());
                        bundle.putCharSequence("shop", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopname());
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
                        //TODO——获取该条记录，从数据库删除
                        delete_cart_goods((Cart_Shop_Item_adapter) adapter, position);
                    }
                });
                delete_cart.create().show();
                return true;
            }
        });


        delete_goods = findViewById(R.id.cart_delete);
        //购物车商品批量删除，购物车商品结算
        Button calculate_goods = findViewById(R.id.cart_calculate);

        delete_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——遍历列表将选中项目一并remove
                for (int i = adapter.getItemCount(); i > 0; i--) {
                    if (adapter.getItemViewType(i) == 2)//商品item
                        if (((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(i)).getStatus())//选中状态
                            delete_cart_goods(adapter, i);//删除item
                }
            }
        });

        calculate_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——获取选中项目，生成订单
                delete_goods.performClick();//删除订单中的商品
            }
        });

    }

    private void delete_cart_goods(Cart_Shop_Item_adapter adapter, int position) {
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
        //TODO——数据库中数据删除
    }

    public static List<Object> sortData(Data_Cart_Bean bean) {
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
        }
    }

    @Override
    protected void onDestroy() {
        //TODO——保存退出前的状态
        super.onDestroy();
        finish();
    }

}
