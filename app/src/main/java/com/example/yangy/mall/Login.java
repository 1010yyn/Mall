package com.example.yangy.mall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class Login extends AppCompatActivity {


    private final static String TAG = "MYTAG";

    public final static int REQUEST_CODE = 10;//请求标识

    private Switch aSwitch;
    private EditText name, password;
    private Button ok;

    private Intent intent;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        name = findViewById(R.id.login_text_name);
        password = findViewById(R.id.login_text_password);
        aSwitch = findViewById(R.id.login_switch_switch);
        ok = findViewById(R.id.login_confirm);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO——查询数据库
                //账号登录正确，跳转
                intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
