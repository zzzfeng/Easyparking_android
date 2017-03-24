package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.model.MotifyPassword;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class motifyPasswordActivity extends Activity {

    private TextView isTheSame;
    private EditText oldpassword_edit, newpassword_edit, confirm_newpassword_edit;
    private Button complicate_btn;
    private ImageButton img_header_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.motify_password);
        initView();
    }

    private void initView() {
        oldpassword_edit = (EditText) findViewById(R.id.old_password_edit);
        newpassword_edit = (EditText) findViewById(R.id.newpassword_edit);
        confirm_newpassword_edit = (EditText) findViewById(R.id.confirm_newpassword_edit);
        complicate_btn = (Button) findViewById(R.id.complicate_btn);
        isTheSame = (TextView) findViewById(R.id.isTheSame);
        img_header_back = (ImageButton) findViewById(R.id.btnHeaderBack);
        setListener();
    }

    private void setListener() {
        MyTextWatcher mt = new MyTextWatcher();
        oldpassword_edit.addTextChangedListener(mt);
        newpassword_edit.addTextChangedListener(mt);
        confirm_newpassword_edit.addTextChangedListener(mt);
        img_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(motifyPasswordActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("NUM", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        complicate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = EasyParkingApplication.getUsername();
                String oldpassword = oldpassword_edit.getText().toString();
                String newpassword = confirm_newpassword_edit.getText().toString();
                complicate_btn.setText("正在修改...");
                complicate_btn.setEnabled(false);
                Log.d("TAG", "UserName:  " + username + "   oldPassword:" + oldpassword + "   New Password: " + newpassword);
                new Service(motifyPasswordActivity.this).motifyPassword(username, oldpassword, newpassword);
            }
        });

    }

    public void getMotifyResult(MotifyPassword motifyPassword) {
        if (motifyPassword == null) {
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
            int r = motifyPassword.getResult();
            String message = motifyPassword.getMessage();
            if (r <= 0) {
                new AlertDialog.Builder(this)
                        .setTitle("修改密码")
                        .setMessage(message)
                        .setPositiveButton("我知道了", null)
                        .show();
                complicate_btn.setText("完成");
                complicate_btn.setEnabled(true);
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
            Boolean t1, t2, t3;
            if (oldpassword_edit.getText().toString().equals("")) {
                t1 = false;
            } else {
                t1 = true;
            }
            if (newpassword_edit.getText().toString().equals("")) {
                t2 = false;
            } else {
                t2 = true;
            }
            if (confirm_newpassword_edit.getText().toString().equals("")) {
                t3 = false;
            } else {
                t3 = true;
            }

            if (t2 && t3) {
                String s1 = newpassword_edit.getText().toString();
                String s2 = confirm_newpassword_edit.getText().toString();
                if (s1.equals(s2)) {
                    isTheSame.setVisibility(View.GONE);
                } else {
                    isTheSame.setVisibility(View.VISIBLE);
                }
            } else {
                isTheSame.setVisibility(View.GONE);
            }

            if (t1 && t2 && t3) {
                complicate_btn.setEnabled(true);
            } else {
                complicate_btn.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("NUM", 2);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
