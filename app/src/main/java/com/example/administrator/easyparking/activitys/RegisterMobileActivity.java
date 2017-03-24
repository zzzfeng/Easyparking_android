package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.easyparking.R;

public class RegisterMobileActivity extends Activity {

    private EditText mobileEditText;
    private Button getCodeButton;
    private CheckBox protocolCheckBox;
    private TextView protocolTextView;
    private String mobileNum;
    private String protocol = "停车服务协议";
    private boolean flag = false;
    private ImageButton btn_heard_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registermobile);
        btn_heard_back = (ImageButton) findViewById(R.id.btnHeaderBack);
        mobileEditText = (EditText) findViewById(R.id.rMMobileEditTextId);
        getCodeButton = (Button) findViewById(R.id.rMGetcodeButtonId);
        protocolCheckBox = (CheckBox) findViewById(R.id.rMProtocolCheckBoxId);
        protocolTextView = (TextView) findViewById(R.id.rMProtocolTextViewId);
        SpannableStringBuilder protocolSpannable = new SpannableStringBuilder(protocol);

        getCodeButton.getBackground().setAlpha(180);
        getCodeButton.setEnabled(false);

        protocolSpannable.setSpan(new UnderlineSpan(), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocolTextView.setText(protocolSpannable);

        btn_heard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterMobileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        protocolCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    flag = true;

                    if ((mobileNum != null) && (mobileNum.length() == 11)) {
                        getCodeButton.getBackground().setAlpha(255);
                        getCodeButton.setEnabled(true);
                    }
                } else {
                    getCodeButton.getBackground().setAlpha(180);
                    getCodeButton.setEnabled(false);
                    flag = false;
                }

            }

        });

        mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobileNum = s.toString();
                if (mobileNum.length() == 11) {
                    if (flag == true) {
                        getCodeButton.getBackground().setAlpha(255);
                        getCodeButton.setEnabled(true);
                    }
                } else {
                    getCodeButton.getBackground().setAlpha(180);
                    getCodeButton.setEnabled(false);
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
                    Intent intent = new Intent();
                    intent.setClass(RegisterMobileActivity.this, RegisterCodeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username",mobileEditText.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });

//        protocolTextView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(RegisterMobileActivity.this, ProtocolActivity.class);
//                startActivity(intent);
//            }
//
//        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
