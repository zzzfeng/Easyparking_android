package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class RegisterCodeActivity extends Activity {

    private EditText codeEditText;
    private Button getCodeButton, nextStepButton;
    private String code;
    private ButtonTimer buttonTimer;
    private ImageButton btn_heard_back;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registercode);
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        btn_heard_back = (ImageButton) findViewById(R.id.btnHeaderBack);
        codeEditText = (EditText) findViewById(R.id.rCCodeEditTextId);
        getCodeButton = (Button) findViewById(R.id.rCGetcodeButtonId);
        nextStepButton = (Button) findViewById(R.id.rCNextstepButtonId);
        buttonTimer = new ButtonTimer(60000, 1000);

        buttonTimer.start();

        nextStepButton.getBackground().setAlpha(180);
        nextStepButton.setEnabled(false);
        btn_heard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterCodeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        codeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = s.toString();
                if (code.length() == 6) {
                    nextStepButton.getBackground().setAlpha(255);
                    nextStepButton.setEnabled(true);
                } else {
                    nextStepButton.getBackground().setAlpha(180);
                    nextStepButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        getCodeButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(180);
                    buttonTimer.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });

        nextStepButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(180);
                    Intent intent = new Intent();
                    intent.setClass(RegisterCodeActivity.this, RegisterPasswordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });
    }

    class ButtonTimer extends CountDownTimer {

        public ButtonTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCodeButton.getBackground().setAlpha(180);
            getCodeButton.setEnabled(false);
            getCodeButton.setText(millisUntilFinished / 1000 + "秒后重新发送");

        }

        @Override
        public void onFinish() {
            getCodeButton.getBackground().setAlpha(255);
            getCodeButton.setEnabled(true);
            getCodeButton.setText("重新获得验证码");

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterCodeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
