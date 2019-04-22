package com.example.yangy.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {


    private final static String TAG = "MYTAG";

    public final static int REQUEST_CODE = 10;//请求标识
    private int LOGIN_OK = 1;
    private int REGIST_OK = 2;
    private int LOGIN_ERROR = 0;
    private int REGIST_ERROR = -1;

    private Intent intent;
    private Bundle bundle = new Bundle();

    CreateData createData = new CreateData();

    private EditText name, password;
    private Switch aSwitch, aSwitch1;

    String Name, Password, Shop_name;

    private Boolean flag = false;//T——注册，F——登录
    private Boolean flag1 = false;//T——商家，F——用户

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOGIN_OK) {
                //账号登录正确，跳转
                Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject js = new JSONObject(msg.obj.toString());
                    if (flag1) {
                        intent = new Intent(Login.this, Shop_Manager.class);
                        Log.i(TAG, "handleMessage: 商家");
                        bundle.putString("shop_name", js.getString("shop_name"));//商店名称
                    } else {
                        intent = new Intent(Login.this, MainActivity.class);
                        Log.i(TAG, "handleMessage: 用户");
                    }
                    bundle.putInt("id", js.getInt("id"));
                    startActivity(intent.putExtras(bundle));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == REGIST_OK) {
                Toast.makeText(Login.this, "注册成功！请重新登录！", Toast.LENGTH_SHORT).show();
                password.setText("");//密码清空
                aSwitch.setChecked(false);//switch修改成登录状态
            } else if (msg.what == LOGIN_ERROR) {
                Toast.makeText(Login.this, "登录失败，请重新尝试！", Toast.LENGTH_SHORT).show();
                password.setText("");
                name.setText("");
            } else if (msg.what == REGIST_ERROR) {
                Toast.makeText(Login.this, "注册失败，请重新尝试！", Toast.LENGTH_SHORT).show();
                password.setText("");
                name.setText("");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        name = findViewById(R.id.login_text_name);
        password = findViewById(R.id.login_text_password);
        aSwitch = findViewById(R.id.login_switch_switch);
        aSwitch1 = findViewById(R.id.login_switch1_switch);
        Button ok = findViewById(R.id.login_confirm);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag = isChecked;
            }
        });

        aSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag1 = isChecked;
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取数据
                Name = name.getText().toString();
                Password = password.getText().toString();
                Log.i(TAG, Name);
                if (flag) {//注册
                    if (flag1) {//商家
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("请完善商家信息");
                        //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                        View view = LayoutInflater.from(Login.this).inflate(R.layout.layout_login__shopkeeper, null);
                        //设置我们自己定义的布局文件作为弹出框的Content
                        builder.setView(view);

                        final TextView shop_userid = view.findViewById(R.id.login_shop_userid);
                        final EditText shop_username = view.findViewById(R.id.login_shop_username);
                        final EditText shop_userphone = view.findViewById(R.id.login_shop_userphone);

                        shop_userid.setText(Name);//设定id，不允许编辑

                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject req = new JSONObject();
                                try {
                                    req.put("type", "UA");
                                    req.put("table", "info_shop");//商家
                                    req.put("name", Name);//id
                                    req.put("password", Password);
                                    req.put("phone", shop_userphone.getText().toString());
                                    req.put("address", " ");
                                    req.put("head", "head1");
                                    req.put("nick", shop_username.getText().toString());
                                    regist(req);//进行注册
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i(TAG, e.getMessage());
                                }
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "取消注册");//不做操作
                                dialog.dismiss();//关闭对话框
                            }
                        });
                        builder.create().show();
                    } else {//用户
                        JSONObject req = new JSONObject();
                        try {
                            req.put("table", "info_user");//用户
                            req.put("type", "UA");
                            req.put("name", Name);
                            req.put("password", Password);
                            req.put("phone", " ");
                            req.put("address", " ");
                            req.put("head", "head1");
                            req.put("nick", Name);
                            regist(req);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    login();//登录
                }
            }
        });
    }

    void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject req = new JSONObject();
                try {
                    if (flag1)
                        req.put("table", "info_shop");//商家
                    else
                        req.put("table", "info_user");//用户
                    req.put("type", "UL");
                    req.put("name", Name);
                    req.put("password", Password);
                    String rst = createData.post_m(req).get(0);
                    if (rst.equals("false"))//登录失败
                        handler.sendEmptyMessage(LOGIN_ERROR);
                    else {
                        Message msg = Message.obtain();
                        msg.what = LOGIN_OK;
                        msg.obj = rst;
                        handler.sendMessage(msg);//登录成功，准备跳转activity
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void regist(final JSONObject req) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String rst = createData.post_m(req).get(0);
                if (rst.equals("false"))//注册失败
                    handler.sendEmptyMessage(REGIST_ERROR);
                else if (rst.equals("true"))//注册成功
                    handler.sendEmptyMessage(REGIST_OK);
            }
        }).start();
    }

}
