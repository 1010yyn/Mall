package com.example.yangy.mall;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import java.util.List;

public class Show_result extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int REQUEST_CODE = 7;//请求标识

    private Intent intent1;
    private Bundle bundle = new Bundle();

    private CreateData createData = new CreateData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_result);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        String str = bundle.getString("str");
        Log.i(TAG, "成功跳转到搜索结果页面，本次搜索关键词为：" + str);
        //TODO——根据关键词检索数据库

        search();


        //示例数据
        Data_Cart_Bean data_cart_bean = createData.getdata(this.getBaseContext());

        RecyclerView list_goods = findViewById(R.id.result_list);
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

    protected void search() {
        final SearchView searchView = findViewById(R.id.result_search_bar);

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
                //TODO——搜索，然后刷新list
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

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }

}
