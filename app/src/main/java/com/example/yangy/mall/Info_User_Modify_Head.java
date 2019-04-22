package com.example.yangy.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

public class Info_User_Modify_Head extends AppCompatActivity {

    private final static String TAG = "MYTAG";
    public final static int REQUEST_CODE = 4;//请求标识

    private Intent intent;
    private Bundle bundle = new Bundle();
    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "成功跳转至修改头像界面");

        setContentView(R.layout.layout_info__user__modify__head);
        intent = getIntent();

        //获取radiogroup
        RadioGroup selection1 = findViewById(R.id.head_selection1);
        RadioGroup selection2 = findViewById(R.id.head_selection2);
        selection1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.hd1:
                        choice = "head1";
                        confirm();
                        break;
                    case R.id.hd2:
                        choice = "head2";
                        confirm();
                        break;
                    case R.id.hd3:
                        choice = "head3";
                        confirm();
                        break;
                    case R.id.hd4:
                        choice = "head4";
                        confirm();
                        break;
                }
            }
        });
        selection2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.hd5:
                        choice = "head5";
                        confirm();
                        break;
                    case R.id.hd6:
                        choice = "head6";
                        confirm();
                        break;
                    case R.id.hd7:
                        choice = "head7";
                        confirm();
                        break;
                    case R.id.hd8:
                        choice = "head8";
                        confirm();
                        break;
                }
            }
        });
    }

    private void confirm() {
        AlertDialog.Builder ok;
        ok = new AlertDialog.Builder(Info_User_Modify_Head.this);
        ok.setTitle("系统提示：");
        ok.setMessage("确定选择该头像吗？");
        ok.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "取消修改头像");//不做操作
                dialog.dismiss();//关闭对话框
            }
        });
        ok.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "确认修改头像");//删除该条记录
                bundle.putCharSequence("head", choice);
                setResult(REQUEST_CODE, intent.putExtras(bundle));//返回头像ID
                finish();
            }
        });
        ok.create().show();
    }

    public void onBackPressed() {
        Log.i(TAG, "点击返回键");
        finish();
    }

}
