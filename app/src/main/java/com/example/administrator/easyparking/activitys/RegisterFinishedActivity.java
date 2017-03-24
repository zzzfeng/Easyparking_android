package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.administrator.easyparking.R;

public class RegisterFinishedActivity extends Activity {
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registerfinished);

        finishButton = (Button) findViewById(R.id.rFFinishButtonId);
//
//        finishButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.getBackground().setAlpha(180);
//                Intent intent = new Intent(RegisterFinishedActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        finishButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(180);
                    Intent intent = new Intent(RegisterFinishedActivity.this, LoginActivity.class);
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
        Intent intent = new Intent(RegisterFinishedActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
