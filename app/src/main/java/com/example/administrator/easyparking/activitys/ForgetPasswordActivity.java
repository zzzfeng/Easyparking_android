package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.administrator.easyparking.R;


public class ForgetPasswordActivity extends Activity {

    private ImageButton backwardImageButton;
    private Button getCodeButton, nextstepButton;
    private EditText mobileEditText, codeEditText;
    private ButtonTimer buttonTimer;
    private String mobileString, codeString;
    private boolean flag = false;
    private ImageButton btn_heard_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgetpassword);
        btn_heard_back = (ImageButton) findViewById(R.id.btnHeaderBack);
//		backwardImageButton=(ImageButton)findViewById(R.id.faImageButtonId);
        getCodeButton = (Button) findViewById(R.id.fGetcodeButtonId);
        nextstepButton = (Button) findViewById(R.id.fNextstepButtonId);
        mobileEditText = (EditText) findViewById(R.id.fMobileEditTextId);
        codeEditText = (EditText) findViewById(R.id.fCodeEditTextId);
        buttonTimer = new ButtonTimer(60000, 1000);

		/*backwardImageButton.setOnClickListener(new OnClickListener(){
            @Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(ForgetPasswordActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});*/

        nextstepButton.getBackground().setAlpha(180);
        nextstepButton.setEnabled(false);

        btn_heard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobileString = s.toString();
                if (mobileString.length() == 11) {
                    flag = true;
                } else {
                    nextstepButton.getBackground().setAlpha(180);
                    nextstepButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        codeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeString = s.toString();
                if (codeString.equals("123456")) {
                    if (flag == true) {
                        nextstepButton.setEnabled(true);
                        nextstepButton.getBackground().setAlpha(255);
                    }
                } else {
                    nextstepButton.setEnabled(false);
                    nextstepButton.getBackground().setAlpha(180);
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
                    v.setBackgroundResource(R.drawable.button_green);
                    if (mobileEditText.getText().toString().length() == 11) {
                        buttonTimer.start();
                        Toast.makeText(ForgetPasswordActivity.this, "系统成功将验证码发送到输入的手机号上，请注意查收", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.parseColor("#388e8e"));
                    v.setBackgroundResource(R.drawable.button_green);
                }
                return false;
            }

        });

        nextstepButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(180);
                    Intent intent = new Intent();
                    intent.setClass(ForgetPasswordActivity.this, SetPasswordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", mobileEditText.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
//					Toast.makeText(ForgetPasswordActivity.this,"验证码已失效，请您重新获取！",Toast.LENGTH_SHORT).show();
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
            getCodeButton.setText("重新获取验证码");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
