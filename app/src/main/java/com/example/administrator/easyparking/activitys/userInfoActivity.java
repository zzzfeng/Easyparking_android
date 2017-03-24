package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.administrator.easyparking.R;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class userInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.userinfo_layout);




    }
}
