package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.content.Intent;
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

import com.example.administrator.easyparking.R;

/**
 * Created by Administrator on 2015/10/30 0030.
 */
public class ForgerPasswordActivity1 extends Activity {

    private ImageView img_header_back;
    private Handler handler;
    private EditText mobilePhone_edit;
    private EditText vertifiedCode_edit;
    private Button next, verticode_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgetpassword1_layout);

        initView();
    }

    private void initView() {
        img_header_back = (ImageView) findViewById(R.id.btnHeaderBack);
        mobilePhone_edit = (EditText) findViewById(R.id.moilePhone_edit);
        vertifiedCode_edit = (EditText) findViewById(R.id.verticode_edit);
        next = (Button) findViewById(R.id.next_FP1_btn);
        verticode_btn = (Button) findViewById(R.id.verticode_btn);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (((String) msg.obj).equals("0秒可再获取")) {
                    verticode_btn.setText("获取验证码");
                    verticode_btn.setClickable(true);
                    mobilePhone_edit.setEnabled(true);
                } else {
                    verticode_btn.setText((String) msg.obj);
                }
            }
        };
        setListener();

    }

    private void setListener() {
        MyTextWatcher mt = new MyTextWatcher();
        mobilePhone_edit.addTextChangedListener(mt);
        vertifiedCode_edit.addTextChangedListener(mt);
        verticode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verticode_btn.setClickable(false);
                mobilePhone_edit.setEnabled(false);
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username", mobilePhone_edit.getText().toString());
                Intent intent = new Intent(ForgerPasswordActivity1.this, ForgerPasswordActivity2.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }
        });

        img_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgerPasswordActivity1.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean t1 = mobilePhone_edit.length() > 0;
            boolean t2 = vertifiedCode_edit.length() > 0;
            if (t2) {
                verticode_btn.setEnabled(true);
            }
            if (t1 && t2) {
                next.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
