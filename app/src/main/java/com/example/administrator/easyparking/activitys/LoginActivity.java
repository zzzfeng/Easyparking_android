package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.model.Login;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

public class LoginActivity extends Activity {

    private Activity mActivity;
    private EditText mobileEditText, passwordEditText;
    private Button loginButton;
    private TextView forgetTextView, registerTextView;
    private CheckBox passwordCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        init();
        setListener();
    }

    private void setListener() {


        loginButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.parseColor("#ffffff"));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(Color.parseColor("#DCDCDC"));

                    if (mobileEditText.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this,
                                "请输入手机号码", Toast.LENGTH_SHORT).show();
                    } else if (passwordEditText.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this,
                                "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {
//                        editor.putString("MOBILE", mobileValue);
//                        editor.commit();
//                        if (passwordCheckBox.isChecked() == true) {
//                            mobileValue = mobileEditText.getText().toString();
//                            passwordValue = passwordEditText.getText().toString();
//                            editor.putString("PASSWORD", passwordValue);
//                            editor.commit();
                        ChangeTheButtonState("正在登陆...", false);
                        String username = mobileEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        new Service(LoginActivity.this).login(username, password);
                    }
                }
                return true;
            }

        });
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mobileEditText.getText().toString().equals("")) {
//                    Toast.makeText(LoginActivity.this,
//                            "请输入手机号码", Toast.LENGTH_SHORT).show();
//                } else if (passwordEditText.getText().toString().equals("")) {
//                    Toast.makeText(LoginActivity.this,
//                            "请输入密码", Toast.LENGTH_SHORT).show();
//                } else {
//                    ChangeTheButtonState("正在登陆...", false);
//                    String username = mobileEditText.getText().toString();
//                    String password = passwordEditText.getText().toString();
//                    new Service(LoginActivity.this).login(username, password);
//                }
//            }
//        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RegisterMobileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

//              forgetTextView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this, ForgetPasswordActivity.class);
//                startActivity(intent);
//
//            }s
//
//        });
//
//        registerTextView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this, RegisterMobileActivity.class);
//                startActivity(intent);
//            }
//
//        });

    }

    private void init() {
        mActivity = this;
        mobileEditText = (EditText) findViewById(R.id.lMobileEditTextId);
        passwordEditText = (EditText) findViewById(R.id.lPasswordEditTextId);
        loginButton = (Button) findViewById(R.id.lButtonId);
        forgetTextView = (TextView) findViewById(R.id.lForgetTextViewId);
        passwordCheckBox = (CheckBox) findViewById(R.id.lCheckBoxId);
        registerTextView = (TextView) findViewById(R.id.lSignUpTextViewId);

        SharedPreferences sp = this.getSharedPreferences("login_information", MODE_PRIVATE);
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        Boolean checked = sp.getBoolean("checked", true);
        if (!username.equals("") && !password.equals("")) {
            // 已保存用户名密码
            mobileEditText.setText(username);
            passwordEditText.setText(password);
        }
        passwordCheckBox.setChecked(checked);
    }

    private void ChangeTheButtonState(String text, boolean b) {
        loginButton.setText(text);
        loginButton.setEnabled(b);
    }

    public void loginResult(Login login) {
        if (login == null) {
            // 网络异常
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            ChangeTheButtonState("登陆", true);
            return;
            //如果异常，不需要往下运行
        }
        int r = login.getResult();

        if (r <= 0) {
            // 登录失败
            String message = login.getMessage();
            new AlertDialog.Builder(this)
                    .setTitle("登陆失败")
                    .setMessage(message)
                    .setPositiveButton("我知道了", null)
                    .show();
            ChangeTheButtonState("登陆", true);
        } else {
            //登陆成功
            EasyParkingApplication.setUsername(mobileEditText.getText().toString());
            SharedPreferences sp = getSharedPreferences("login_information", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (passwordCheckBox.isChecked()) {
                //如果选择保存密码
                editor.putString("username", mobileEditText.getText().toString());
                editor.putString("password", passwordEditText.getText().toString());
                editor.putBoolean("checked", true);
            } else {
                //不保存
                editor.clear();
                editor.putBoolean("checked", false);
            }
            editor.putString("session_id", login.getSession_id());
            editor.commit();
            // 跳转到主页面
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
