package com.example.yangy.mall;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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
    private Switch aSwitch;

    String Name, Password;

    private Boolean flag = false;//T——注册，F——登录

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOGIN_OK) {
                //账号登录正确，跳转
                Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_SHORT).show();
                intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("id", msg.obj.toString());
                startActivity(intent);
                finish();
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
        Button ok = findViewById(R.id.login_confirm);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag = isChecked;
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取数据
                Name = name.getText().toString();
                Password = password.getText().toString();
                if (flag) {//注册
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("type", "UA");
                                req.put("name", Name);
                                req.put("password", Password);
                                req.put("phone", " ");
                                req.put("address", " ");
                                req.put("head", "head1");
                                req.put("nick", Name);
                                String rst = createData.post(req);
                                if (rst.equals("false"))//注册失败
                                    handler.sendEmptyMessage(REGIST_ERROR);
                                else if (rst.equals("true"))//注册成功
                                    handler.sendEmptyMessage(REGIST_OK);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {//登录
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("type", "UL");
                                req.put("name", Name);
                                req.put("password", Password);
                                String rst = createData.post(req);
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
            }
        });
    }
}
