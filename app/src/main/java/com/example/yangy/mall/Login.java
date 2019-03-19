package com.example.yangy.mall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class Login extends AppCompatActivity {


    private final static String TAG = "MYTAG";

    public final static int REQUEST_CODE = 10;//请求标识

    private Intent intent;
    private Bundle bundle = new Bundle();

    private Boolean flag = false;//T——注册，F——登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        final EditText name = findViewById(R.id.login_text_name);
        final EditText password = findViewById(R.id.login_text_password);
        Switch aSwitch = findViewById(R.id.login_switch_switch);
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
                String Name = name.getText().toString();
                String Password = password.getText().toString();
                if (flag) {
                    //注册(成功&失败)

                } else {
                    //登录
                    //TODO——查询数据库
                    //账号登录正确，跳转
                    intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
