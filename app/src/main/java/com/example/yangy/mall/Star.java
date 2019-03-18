package com.example.yangy.mall;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class Star extends TabActivity {

    private String TAG = "MYTAG";
    public final static int REQUEST_CODE = 8;//请求标识

    private Intent intent, intent1;
    private Bundle bundle = new Bundle();

    private TabHost tabHost;

    private RecyclerView list_goods;
    private RecyclerView list_shop;

    private AlertDialog.Builder delete;

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的OpenRecordBean对象

    private CreateData createData = new CreateData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        bundle = intent.getExtras();

        tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.layout_star, tabHost.getTabContentView(), true);
        tabHost.addTab(tabHost.newTabSpec("layout_star_goods").setIndicator("商品").setContent(R.id.star_tab__goods));
        tabHost.addTab(tabHost.newTabSpec("layout_star_shop").setIndicator("商店").setContent(R.id.star_tab__shop));

        //获取数据
        data_cart_bean = createData.getdata(this.getBaseContext());
        add_goods();//商品列表
        add_shop();//商店列表
    }

    private void add_goods() {
        //获取recycleview
        list_goods = findViewById(R.id.star__goods_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_goods.setLayoutManager(manager);
        final Goods_Item_adapter adapter = new Goods_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_goods.addItemDecoration(divider);
        list_goods.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品");
                intent1 = new Intent(Star.this, Goods.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("name", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getName());
                bundle.putInt("photo", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getPhoto());
                bundle.putCharSequence("price", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getPrice());
                bundle.putCharSequence("description", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getDescription());
                bundle.putCharSequence("shop", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopname());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                Log.i(TAG, "长按购物车商品" + position);
                delete = new AlertDialog.Builder(Star.this);//确认删除对话框
                delete.setTitle("系统提示：");
                delete.setMessage("确定要删除吗？");
                delete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "取消删除");//不做操作
                        dialog.dismiss();//关闭对话框
                    }
                });
                delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "确认删除");
                        adapter.remove(position);//删除商品item
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                        //TODO——清除数据库内容
                    }
                });
                delete.create().show();
                return true;
            }
        });

    }

    private void add_shop() {
        //获取recycleview
        list_shop = findViewById(R.id.star__shop_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_shop.setLayoutManager(manager);
        final Shop_Item_adapter adapter = new Shop_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_shop.addItemDecoration(divider);
        list_shop.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品");
                intent1 = new Intent(Star.this, Shop.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopname());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                Log.i(TAG, "长按购物车商品" + position);
                delete = new AlertDialog.Builder(Star.this);//确认删除对话框
                delete.setTitle("系统提示：");
                delete.setMessage("确定要删除吗？");
                delete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "取消删除");//不做操作
                        dialog.dismiss();//关闭对话框
                    }
                });
                delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "确认删除");
                        adapter.remove(position);//删除商品item
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                        //TODO——清除数据库内容
                    }
                });
                delete.create().show();
                return true;
            }
        });
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }
}
