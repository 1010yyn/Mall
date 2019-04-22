package com.example.yangy.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Shop_Manager extends AppCompatActivity {

    private final static String TAG = "MYTAG";

    private int GET_SHOP_OK = 1;
    private int DLT_SHOP_OK = 2;//删除购物车数据OK
    private int SHOP_EMPTY = 3;
    private int ADD_ERROR = 0;

    private Bundle bundle = new Bundle();
    private Intent intent1;

    private String shop_name;
    private int id;

    private CreateData createData = new CreateData();

    private Data_Cart_Bean data_cart_bean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_SHOP_OK) {
                data_cart_bean = (Data_Cart_Bean) msg.obj;
                setData();//加载信息
            } else if (msg.what == SHOP_EMPTY) {
                data_cart_bean = null;
                setData();//加载信息
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shop__manager);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getInt("id");//shop_id
        shop_name = bundle.getString("shop_name");//获取商店name
        Log.i(TAG, "成功跳转商店管理界面");
    }

    void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject req = new JSONObject();
                    req.put("shop_name", shop_name);
                    req.put("type", "SG");
                    ArrayList<String> rst = createData.post_m(req);//查询商品信息

                    JSONObject goods = null;
                    Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean goods_bean;//临时商品信息
                    Data_Cart_Bean.Data_Shop_Bean shop_bean;//临时店铺信息
                    List<Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean> data_goods_beans;//临时商品列表

                    Data_Cart_Bean data_cart_bean = new Data_Cart_Bean();//网络请求成功返回的Bean对象

                    data_goods_beans = new ArrayList<>();//商店商品列表初始化
                    shop_bean = new Data_Cart_Bean.Data_Shop_Bean();//商店信息初始化

                    List<Data_Cart_Bean.Data_Shop_Bean> data_shop_beans = new ArrayList<>();//临时店铺列表

                    int i = 0;
                    while (i < rst.size()) {
                        goods = new JSONObject(rst.get(i));//转换数据
                        //添加商品信息，加入商店商品列表
                        goods_bean = new Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean();
                        goods_bean.setShopname(goods.getString("shop_name"));//设定商店名称
                        goods_bean.setShopid(goods.getString("shop_id"));//设定商店id
                        goods_bean.setGoodsname(goods.getString("goods_name"));//设定商品名称
                        goods_bean.setGoodsid(goods.getString("goods_id"));//设定商品id
                        goods_bean.setPrice(goods.getInt("price"));//设定商品价格
                        goods_bean.setPhoto(goods.getString("photo"));
                        goods_bean.setSum(goods.getString("sum"));
                        goods_bean.setDescription(goods.getString("description"));//设定商品描述
                        //添加商品至临时商品列表
                        data_goods_beans.add(goods_bean);
                        i++;
                    }
                    //如果店铺商品非空
                    if (rst.size() > 0) {
                        shop_bean.setName("");//设定商店名
                        shop_bean.setId(goods.getString("shop_id"));//设定商店id
                        shop_bean.setGoodsData(data_goods_beans);//将商品列表加入店铺信息
                        data_shop_beans.add(shop_bean);//将商店加入购物车列表
                        //将所有的店铺列表加入购物车
                        data_cart_bean.setShopData(data_shop_beans);
                        Message msg = Message.obtain();
                        msg.obj = data_cart_bean;
                        msg.what = GET_SHOP_OK;
                        handler.sendMessage(msg);
                    } else handler.sendEmptyMessage(SHOP_EMPTY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void setData() {
        //获取recycleview
        RecyclerView list_manager = findViewById(R.id.manager_list);
        // 将网络请求获取到的json字符串转成的对象进行二次重组，生成集合List<Object>
        List<Object> list = MainActivity.sortData(data_cart_bean);
        //创建布局管理
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list_manager.setLayoutManager(manager);
        final Cart_Shop_Item_adapter adapter = new Cart_Shop_Item_adapter(list);
        //设置列表分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list_manager.addItemDecoration(divider);
        list_manager.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "单击商品进行修改");
                intent1 = new Intent(Shop_Manager.this, Goods_Manager.class);
                bundle = new Bundle();//清空数据
                bundle.putCharSequence("shop_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getShopid());
                bundle.putCharSequence("goods_id", ((Data_Cart_Bean.Data_Shop_Bean.Data_Goods_Bean) adapter.getItem(position)).getGoodsid());
                bundle.putCharSequence("shop_name", shop_name);
                bundle.putCharSequence("type", "edit");
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                Log.i(TAG, "长按删除商品" + position);
                AlertDialog.Builder delete_goods = new AlertDialog.Builder(Shop_Manager.this);//确认删除对话框
                delete_goods.setTitle("系统提示：");
                delete_goods.setMessage("确定要删除该商品吗？");
                delete_goods.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "取消删除");//不做操作
                        dialog.dismiss();//关闭对话框
                    }
                });
                delete_goods.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "确认删除");//删除该条记录
                        delete_cart_goods((Cart_Shop_Item_adapter) adapter, position);
                    }
                });
                delete_goods.create().show();
                return true;
            }
        });

        //设置商店名称
        TextView name = findViewById(R.id.manager_name);
        name.setText(shop_name);

        Button delete = findViewById(R.id.manager_delete);
        Button add = findViewById(R.id.manager_add);

        delete.setOnClickListener(new View.OnClickListener() {
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "单击添加商品");
                intent1 = new Intent(Shop_Manager.this, Goods_Manager.class);
                bundle = new Bundle();//清空数据
                bundle.putString("shop_id", id + "");
                bundle.putCharSequence("shop_name", shop_name);
                bundle.putCharSequence("type", "new");
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }

    void delete_cart(final JSONObject req) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String rst = createData.post_m(req).get(0);
                if (rst.equals("true")) {
                    handler.sendEmptyMessage(DLT_SHOP_OK);
                }
            }
        }).start();
    }

    private void delete_cart_goods(Cart_Shop_Item_adapter adapter, int position) {
        //删除数据库中数据
        final JSONObject req = new JSONObject();
        try {
            req.put("type", "GD");
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

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }
}
