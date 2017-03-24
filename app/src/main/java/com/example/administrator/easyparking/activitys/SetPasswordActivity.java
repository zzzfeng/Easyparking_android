package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.model.ForgetPassword;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

public class SetPasswordActivity extends Activity {

    private EditText passwordEditText;
    private Button nextstepButton;
    private String newPassword;
    private ImageButton btn_heard_back;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setpassword);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        btn_heard_back = (ImageButton) findViewById(R.id.btnHeaderBack);
        passwordEditText = (EditText) findViewById(R.id.sPasswordEditTextId);
        nextstepButton = (Button) findViewById(R.id.sNextstepButtonId);

        nextstepButton.getBackground().setAlpha(180);
        nextstepButton.setEnabled(false);


        btn_heard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetPasswordActivity.this, LoginActivity.class);
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
                newPassword = s.toString();
                if ((newPassword.length() > 5) && (newPassword.length() < 17)) {
                    nextstepButton.getBackground().setAlpha(255);
                    nextstepButton.setEnabled(true);
                } else {
                    nextstepButton.getBackground().setAlpha(180);
                    nextstepButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        nextstepButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(180);
                    Log.d("TAG", "username :" + username + "  passwrod: " + passwordEditText.getText().toString());
                    new Service(SetPasswordActivity.this).forgetPassword(username, passwordEditText.getText().toString());
                    nextstepButton.setText("正在修改...");
//                    Intent intent = new Intent();
//                    intent.setClass(SetPasswordActivity.this, PasswordFinishedActivity.class);
//                    startActivity(intent);
//                    finish();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(255);
                }
                return false;
            }

        });

    }

    public void forgetPasswordResult(ForgetPassword forgetPassword) {
        nextstepButton.setText("下一步");
        if (forgetPassword == null) {
            //获取失败
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            //这里可以继续进行

            return;
            //如果异常，不需要往下运行
        } else {
            int r = forgetPassword.getResult();
            String message = forgetPassword.getMessage();
            if (r <= 0) {
                new AlertDialog.Builder(this)
                        .setTitle("修改密码")
                        .setMessage(message)
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                //将之前保留的用户信息删除
                SharedPreferences sp = getSharedPreferences("login_information", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username", EasyParkingApplication.getUsername());
                editor.putString("password", "");
                editor.putBoolean("checked", true);
                editor.commit();

                //跳转到Login进行登陆
//                Intent intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
//                finish();
                Intent intent = new Intent();
                intent.setClass(SetPasswordActivity.this, PasswordFinishedActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
