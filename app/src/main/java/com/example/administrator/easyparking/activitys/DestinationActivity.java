package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.administrator.easyparking.R;

/**
 * Created by tan on 2017/3/23 0023.
 */
public class DestinationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.destination);
    }
}
