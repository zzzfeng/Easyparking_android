package com.example.administrator.easyparking.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.fragment.fragment_map;
import com.example.administrator.easyparking.fragment.fragment_mine;
import com.example.administrator.easyparking.fragment.fragment_order;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    Fragment[] mFragments = new Fragment[3];
    private long _doubleClickedTime = 0;
    private Button rbOne;
    private Button rbTwo;
    private Button rbThree;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        mFragments[0] = new fragment_order();
        mFragments[1] = new fragment_map();
        mFragments[2] = new fragment_mine();
        initDatas();
        setFragmentIndicator();
    }

    private void setFragmentIndicator() {
        rbOne = (Button) findViewById(R.id.item1);
        rbTwo = (Button) findViewById(R.id.item2);
        rbThree = (Button) findViewById(R.id.item3);
        rbOne.setOnClickListener(this);
        rbTwo.setOnClickListener(this);
        rbThree.setOnClickListener(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (!bundle.isEmpty()) {
                _resetNavButtons();
                num = (int) bundle.get("NUM");
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.id_fragment, mFragments[num]);
                transaction.commit();
                if (num != 1) {
                    if (num == 0) {
                        ((Button) findViewById(R.id.showinItem1)).setVisibility(View.VISIBLE);
                    } else {
                        ((Button) findViewById(R.id.showinItem3)).setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.id_fragment, mFragments[1]);
            transaction.commit();
        }

    }

    private void _resetNavButtons() {
        ((Button) findViewById(R.id.showinItem1)).setVisibility(View.GONE);
//        ((Button) findViewById(R.id.showinItem2)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.showinItem3)).setVisibility(View.GONE);
    }

    private void initDatas() {
        mTabs.add(mFragments[0]);
        mTabs.add(mFragments[1]);
        mTabs.add(mFragments[2]);
    }

    @Override
    public void onClick(View v) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (v.getId()) {
            case R.id.item1:
                _resetNavButtons();
                ((Button) findViewById(R.id.showinItem1)).setVisibility(View.VISIBLE);
                transaction.replace(R.id.id_fragment, mFragments[0]);
                Log.d("TAG", "1");
                break;

            case R.id.item2:
                _resetNavButtons();
                transaction.replace(R.id.id_fragment, mFragments[1]);
                Log.d("TAG", "2");
                break;

            case R.id.item3:
                _resetNavButtons();
                ((Button) findViewById(R.id.showinItem3)).setVisibility(View.VISIBLE);
                transaction.replace(R.id.id_fragment, mFragments[2]);
                Log.d("TAG", "3");
                break;
        }
        transaction.commit();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long clickedTime = System.currentTimeMillis();
            if (clickedTime - _doubleClickedTime <= 2000) {
                // 两次点击退出
                finish();
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                _doubleClickedTime = clickedTime;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
