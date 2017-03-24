package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.model.Register;

public class RegisterPasswordActivity extends Activity {

    private EditText passwordEditText;
    private Button finishButton;
    private String password;
    private ImageButton btn_header_back;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registerpassword);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        btn_header_back = (ImageButton) findViewById(R.id.btnHeaderBack);
        passwordEditText = (EditText) findViewById(R.id.rPPasswordEditTextId);
        finishButton = (Button) findViewById(R.id.rPFinishButtonId);

        finishButton.getBackground().setAlpha(180);
        finishButton.setEnabled(false);

        btn_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
                if ((password.length() > 5) && (password.length()) < 17) {
                    finishButton.getBackground().setAlpha(255);
                    finishButton.setEnabled(true);
                } else {
                    finishButton.getBackground().setAlpha(180);
                    finishButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        finishButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(180);
                    new Service(RegisterPasswordActivity.this).register(username, passwordEditText.getText().toString());
                    finishButton.setText("正在注册...");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(255);
                }
                return false;
            }

        });

    }
    public void registerResult(Register register) {
        if (register == null) {
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            finishButton.setText("完成");
            return;
            //如果异常，不需要往下运行
        } else {
            int r = register.getResult();
            if (r <= 0) {
                //注册失败
                String message = register.getMessage();
                new AlertDialog.Builder(this)
                        .setTitle("注册失败")
                        .setMessage(message)
                        .setPositiveButton("我知道了", null)
                        .show();
                finishButton.setText("完成");
            } else {
                //注册成功
                finishButton.setText("完成");
                //将之前保留的用户信息删除
                SharedPreferences sp = getSharedPreferences("login_information", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.putBoolean("checked", true);
                editor.commit();
                Intent intent = new Intent();
                intent.setClass(RegisterPasswordActivity.this, RegisterFinishedActivity.class);
                startActivity(intent);
                finish();
//                //跳转到Login进行登陆
//                Intent intent = new Intent(RegisterPasswordActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
