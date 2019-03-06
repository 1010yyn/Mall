package com.example.yangy.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Info_User_Modify extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    private final static int REQUEST_CODE = 3;//请求标识

    private Intent intent;
    private Bundle bundle = new Bundle();
    private TextView id, nickname, phone, address;
    private ImageView head;
    private Button confirm;
    private AlertDialog.Builder ok;
    private String Head, Id, Nickname, Phone, Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info__user__modify);
        intent = getIntent();
        bundle = intent.getExtras();

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
        confirm = findViewById(R.id.info_user_modify_confirm);

        //设置原信息显示
        id.setText(Id);
        nickname.setText(Nickname);
        phone.setText(Phone);
        address.setText(Address);

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "跳转更换头像");
                //TODO——单击头像切换头像
                intent = new Intent(Info_User_Modify.this, Info_User_Modify_Head.class);
                startActivityForResult(intent, 4);
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
                        //bundle=new Bundle();
                        bundle.putCharSequence("id", Id);
                        bundle.putCharSequence("nick", Nickname);
                        bundle.putCharSequence("phone", Phone);
                        bundle.putCharSequence("address", Address);
                        bundle.putCharSequence("head", Head);
                        //TODO——更新数据库
                        //返回数据
                        setResult(3, intent.putExtras(bundle));
                        finish();
                    }
                });
                ok.create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int reQuestCode, int resultCode, Intent data) {
        if (reQuestCode == REQUEST_CODE && resultCode == 4) {
            Bundle bundle_head = data.getExtras();
            Head = bundle_head.getString("head");//获取头像ID
        }
    }
}