package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.model.Register;

/**
 * Created by Administrator on 2015/10/29 0029.
 */
public class RegisterActivity extends Activity {
    private Handler handler;
    private EditText teltphone_Edittext, password_Edittext, verticode_Edittext;
    Button verticode_btn, register_btn;
    private Activity mActivity;
    private int time;
    private ImageView img_header_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_layout);
        InitView();
        setListener();

    }

    private void setListener() {
        TextWatcher textWatcher = new textWatcher();
        teltphone_Edittext.addTextChangedListener(textWatcher);
        password_Edittext.addTextChangedListener(textWatcher);
        verticode_Edittext.addTextChangedListener(textWatcher);
        verticode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verticode_btn.setClickable(false);
                teltphone_Edittext.setEnabled(false);
                Thread td = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 60;
                        while (i >= 0) {
                            Message message = new Message();
                            message.obj = Integer.toString(i) + "秒可再获取";
                            handler.sendMessage(message);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            i--;
                        }
                    }
                });
                td.start();

            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teltphone_Edittext.setEnabled(false);
                password_Edittext.setEnabled(false);
                verticode_btn.setEnabled(false);
                verticode_Edittext.setEnabled(false);
                register_btn.setText("正在注册....");
                register_btn.setEnabled(false);
                new Service(mActivity).register(teltphone_Edittext.getText().toString(), password_Edittext.getText().toString());
            }
        });

        img_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void InitView() {
        mActivity = this;
        img_header_back = (ImageView) findViewById(R.id.btnHeaderBack);
        teltphone_Edittext = (EditText) findViewById(R.id.telephone_edittext);
        password_Edittext = (EditText) findViewById(R.id.password_edittext);
        verticode_Edittext = (EditText) findViewById(R.id.verticode_edittext);
        verticode_btn = (Button) findViewById(R.id.verticode_btn);
        register_btn = (Button) findViewById(R.id.register_btn);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (((String) msg.obj).equals("0秒可再获取")) {
                    verticode_btn.setText("获取验证码");
                    verticode_btn.setClickable(true);
                    teltphone_Edittext.setEnabled(true);
                } else {
                    verticode_btn.setText((String) msg.obj);
                }
            }
        };
    }

    class textWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean t1 = teltphone_Edittext.length() > 0;
            boolean t2 = password_Edittext.length() > 0;
            boolean t3 = verticode_Edittext.length() > 0;
            if (t1) {
                verticode_btn.setEnabled(true);
            } else {
                verticode_btn.setEnabled(false);
            }
            if (t1 && t2 && t3) {
                register_btn.setEnabled(true);

            } else {
                register_btn.setEnabled(false);

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void registerResult(Register register) {
        if (register == null) {
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            teltphone_Edittext.setEnabled(true);
            password_Edittext.setEnabled(true);
            verticode_btn.setEnabled(true);
            verticode_Edittext.setEnabled(true);
            register_btn.setText("注册");
            register_btn.setEnabled(true);
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
                teltphone_Edittext.setEnabled(true);
                password_Edittext.setEnabled(true);
                verticode_btn.setEnabled(true);
                verticode_Edittext.setEnabled(true);
                register_btn.setText("注册");
                register_btn.setEnabled(true);
            } else {
                //注册成功

                //将之前保留的用户信息删除
                SharedPreferences sp = getSharedPreferences("login_information", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.putBoolean("checked", true);
                editor.commit();

                //跳转到Login进行登陆
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
