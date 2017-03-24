package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.administrator.easyparking.R;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class safeToCheckOut extends Activity {
    private Button sure, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_out);
        initView();
    }

    private void initView() {
        sure = (Button) findViewById(R.id.sure);
        cancel = (Button) findViewById(R.id.cancel);
        setListener();
    }

    private void setListener() {
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("login_information", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.putBoolean("checked", false);
                editor.commit();

                Intent intent = new Intent(safeToCheckOut.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
