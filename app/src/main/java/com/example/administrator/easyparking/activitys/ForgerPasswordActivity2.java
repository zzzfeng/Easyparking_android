package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.model.ForgetPassword;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class ForgerPasswordActivity2 extends Activity {

    private EditText newPassword_edit, confirm_newPassword_edit;
    private Button finish_btn;
    private ImageView img_header_back;
    private TextView isTheSame;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        setContentView(R.layout.forgetpassword2_layout);
        initView();
    }

    private void initView() {
        isTheSame = (TextView) findViewById(R.id.isTheSame);
        img_header_back = (ImageView) findViewById(R.id.btnHeaderBack);
        newPassword_edit = (EditText) findViewById(R.id.newpassword_edit);
        confirm_newPassword_edit = (EditText) findViewById(R.id.confirm_newpassword_edit);
        finish_btn = (Button) findViewById(R.id.finish_btn);
        setListener();
    }

    private void setListener() {

        MyTextWatcher mt = new MyTextWatcher();
        newPassword_edit.addTextChangedListener(mt);
        confirm_newPassword_edit.addTextChangedListener(mt);

        img_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgerPasswordActivity2.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Service(ForgerPasswordActivity2.this).forgetPassword(username, confirm_newPassword_edit.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgerPasswordActivity2.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void forgetPasswordResult(ForgetPassword forgetPassword) {
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
                finish_btn.setText("完成");
                finish_btn.setEnabled(true);
            } else {
                //将之前保留的用户信息删除
                SharedPreferences sp = getSharedPreferences("login_information", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username", EasyParkingApplication.getUsername());
                editor.putString("password", "");
                editor.putBoolean("checked", true);
                editor.commit();

                //跳转到Login进行登陆
                Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean t1 = newPassword_edit.length() > 0;
            boolean t2 = confirm_newPassword_edit.length() > 0;

            if (t1 && t2) {
                String s1 = newPassword_edit.getText().toString();
                String s2 = confirm_newPassword_edit.getText().toString();
                if (s1.equals(s2)) {
                    isTheSame.setVisibility(View.GONE);
                    finish_btn.setEnabled(true);
                } else {
                    isTheSame.setVisibility(View.VISIBLE);
                    finish_btn.setEnabled(false);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
