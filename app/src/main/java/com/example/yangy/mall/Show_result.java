package com.example.yangy.mall;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class Show_result extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int REQUEST_CODE = 7;//请求标识

    private Intent intent, intent1;
    private Bundle bundle = new Bundle();

    private RecyclerView list_goods;
    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_result);
        intent = getIntent();
        bundle = intent.getExtras();
        String str = bundle.getString("str");
        Log.i(TAG, "成功跳转到搜索结果页面，本次搜索关键词为：" + str);
        //TODO——根据关键词检索数据库

        //示例数据
        getdata();

        list_goods = findViewById(R.id.result_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_goods.setLayoutManager(manager);
        final Goods_Item_adapter adapter = new Goods_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.cart_divider));
        list_goods.addItemDecoration(divider);

        list_goods.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品");
                intent1 = new Intent(Show_result.this, Goods.class);
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


    }


    private void getdata() {
        String Head_Name = "head1";

        //临时存储信息
        Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
        Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
        List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表
        List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<Data_Cart_Bean.Data_Shop_Bean>();//临时店铺列表

        data_goods_beans = new ArrayList<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean>();
        //设置商品3信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("另一家店");
        goods_bean.setName("324");
        goods_bean.setPrice(123);
        goods_bean.setSum(1);
        goods_bean.setPhoto(getResources().getIdentifier(Head_Name, "drawable", getBaseContext().getPackageName()));
        goods_bean.setDescription("商品3");
        //添加商品至临时商品列表
        data_goods_beans.add(goods_bean);

        //设置商品4信息
        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
        goods_bean.setShopname("另一家店");
        goods_bean.setName("793");
        goods_bean.setPrice(432);
        goods_bean.setSum(3);
        goods_bean.setPhoto(getResources().getIdentifier(Head_Name, "drawable", getBaseContext().getPackageName()));
        goods_bean.setDescription("商品4");
        //添加商品至临时商品列表2
        data_goods_beans.add(goods_bean);

        //设置临时商铺信息2
        shop_bean = new Data_Cart_Bean.Data_Shop_Bean();
        shop_bean.setName("另一家店");
        //将商品列表加入临时店铺2
        shop_bean.setGoodsData(data_goods_beans);

        //将店铺加入临时店铺列表
        data_shop_beans.add(shop_bean);

        //将店铺列表加入购物车
        data_cart_bean.setShopData(data_shop_beans);
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }

}
