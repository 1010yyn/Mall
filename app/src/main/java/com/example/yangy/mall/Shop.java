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
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class Shop extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private final static int REQUEST_CODE = 6;//请求标识

    private Bundle bundle = new Bundle();
    private Intent intent, intent1;

    private Button star, hate;
    private RecyclerView list_goods;

    private String Name;

    private Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的OpenRecordBean对象
    private CreateData createData = new CreateData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shop);
        intent = getIntent();
        bundle = intent.getExtras();
        Name = bundle.getString("name");

        //TODO——根据Name检索店铺信息
        data_cart_bean = createData.getdata(this.getBaseContext());

        star = findViewById(R.id.shop_star);
        hate = findViewById(R.id.shop_hate);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——收藏
            }
        });

        hate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——拉黑
            }
        });

        //获取recycleview
        list_goods = findViewById(R.id.shop_list);
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
                intent1 = new Intent(Shop.this, Goods.class);
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

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }
}
