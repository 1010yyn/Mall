package com.example.yangy.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Info_User_Modify extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int REQUEST_CODE = 3;//请求标识
    private static int UPDATE_OK = 1;
    private static int UPDATE_ERROR = 0;

    private Intent intent1, intent2;
    private Bundle bundle = new Bundle();
    private TextView id, nickname, phone, address;
    private ImageView head;
    private AlertDialog.Builder ok;
    private String Head, Id, Nickname, Phone, Address;

    private CreateData getdata = new CreateData();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_OK) {
                Toast.makeText(Info_User_Modify.this, "修改成功！", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "个人信息修改成功");
            } else if (msg.what == UPDATE_ERROR) {
                Toast.makeText(Info_User_Modify.this, "修改失败！", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "个人信息修改失败");
            }
            setResult(3, intent1.putExtras(bundle));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info__user__modify);
        intent1 = getIntent();
        bundle = intent1.getExtras();
        Log.i(TAG, "成功跳转信息修改界面");

        //获取个人信息界面信息，填充至修改界面
        Head = bundle.getString("head");
        Id = bundle.getString("id");
        Nickname = bundle.getString("nick");
        Phone = bundle.getString("phone");
        Address = bundle.getString("address");

        //获取控件
        head = findViewById(R.id.info_user_modify_head);
        id = findViewById(R.id.info_user_modify_name);
        nickname = findViewById(R.id.info_user_modify_nickname);
        phone = findViewById(R.id.info_user_modify_phone);
        address = findViewById(R.id.info_user_modify_address);
        Button confirm = findViewById(R.id.info_user_modify_confirm);

        //设置原信息显示
        id.setText(Id);
        nickname.setText(Nickname);
        phone.setText(Phone);
        address.setText(Address);
        head.setImageResource(getResources().getIdentifier(Head, "drawable", getBaseContext().getPackageName()));

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "跳转更换头像");
                intent2 = new Intent(Info_User_Modify.this, Info_User_Modify_Head.class);
                startActivityForResult(intent2, REQUEST_CODE, bundle);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "确认修改");
                ok = new AlertDialog.Builder(Info_User_Modify.this);
                ok.setTitle("系统提示：");
                ok.setMessage("确定修改信息吗？");
                ok.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "取消修改个人信息");//不做操作
                        dialog.dismiss();//关闭对话框
                    }
                });
                ok.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "确认修改个人信息");//删除该条记录
                        Id = id.getText().toString();
                        Nickname = nickname.getText().toString();
                        Phone = phone.getText().toString();
                        Address = address.getText().toString();
                        bundle.putCharSequence("id", Id);
                        bundle.putCharSequence("nick", Nickname);
                        bundle.putCharSequence("phone", Phone);
                        bundle.putCharSequence("address", Address);
                        bundle.putCharSequence("head", Head);
                        JSONObject js = new JSONObject();
                        try {
                            js.put("type", "UU");
                            js.put("id", MainActivity.idOfuser + "");
                            js.put("nick", Nickname);
                            js.put("phone", Phone);
                            js.put("address", Address);
                            js.put("head", Head);
                            update(js);
                        } catch (Exception e) {
                            Log.e(TAG, "update info_user error!");
                        }
                    }
                });
                ok.create().show();
            }
        });
    }

    protected void update(final JSONObject req) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "试图更新信息");
                String rst = getdata.post_m(req).get(0);//更新并获取数据
                if (rst.equals("true"))//更新成功
                    handler.sendEmptyMessage(UPDATE_OK);
                else handler.sendEmptyMessage(UPDATE_ERROR);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int reQuestCode, int resultCode, Intent data) {
        if (reQuestCode == REQUEST_CODE && resultCode == Info_User_Modify_Head.REQUEST_CODE) {
            Bundle bundle_head = data.getExtras();
            Head = bundle_head.getString("head");//获取头像ID
            head.setImageResource(getResources().getIdentifier(Head, "drawable", getBaseContext().getPackageName()));//重新设置头像
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        setResult(REQUEST_CODE, intent1.putExtras(bundle));
        finish();
    }
}
