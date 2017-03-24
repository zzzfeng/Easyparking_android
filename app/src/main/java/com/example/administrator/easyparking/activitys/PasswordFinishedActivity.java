package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.administrator.easyparking.R;

public class PasswordFinishedActivity extends Activity {

    private Button finishButton;
    private ImageButton btn_heard_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.finishpassword);
        btn_heard_back = (ImageButton) findViewById(R.id.btnHeaderBack);
        finishButton = (Button) findViewById(R.id.fFinishButtonId);


        btn_heard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordFinishedActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        finishButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(180);
                    Intent intent = new Intent(PasswordFinishedActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(255);
                }
                return false;
            }

        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PasswordFinishedActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
