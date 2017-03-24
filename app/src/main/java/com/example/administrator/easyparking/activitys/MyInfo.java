package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.Service;
import com.example.administrator.easyparking.model.UserInfo;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class MyInfo extends Activity {

    private TextView phone_txt, carnum_txt, honesttime_txt, unhonesttime_txt;
    private ImageView heard1, heard2, heard3, heard4, heard5;
    private ImageButton img_heard_back;
    private int honest_time, unhonest_time, numofheard;
    private String username, carnum;
    private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myinfo);
        ininView();
        setListener();
        new Service(MyInfo.this).getUserInfo(EasyParkingApplication.getUsername());
    }

    private void setListener() {
        img_heard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInfo.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("NUM", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ininView() {
        progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("请稍等...");
        progDialog.show();
        phone_txt = (TextView) findViewById(R.id.telephon_txt);
        carnum_txt = (TextView) findViewById(R.id.car_num_txt);
        honesttime_txt = (TextView) findViewById(R.id.honest_time_txt);
        unhonesttime_txt = (TextView) findViewById(R.id.unhonest_time_txt);
        heard1 = (ImageView) findViewById(R.id.heard1);
        heard2 = (ImageView) findViewById(R.id.heard2);
        heard3 = (ImageView) findViewById(R.id.heard3);
        heard4 = (ImageView) findViewById(R.id.heard4);
        heard5 = (ImageView) findViewById(R.id.heard5);
        img_heard_back = (ImageButton) findViewById(R.id.btnHeaderBack);
    }

    public void getUserInfoResult(UserInfo userInfo) {
        if (userInfo == null) {
            //获取失败
            new AlertDialog.Builder(this)
                    .setTitle("网络异常")
                    .setMessage("网络异常，请稍后重试")
                    .setPositiveButton("我知道了", null)
                    .show();
            //这里可以继续进行

            return;
            //如果异常，不需要往下运行
        } else {
            int r = userInfo.getResult();
            if (r <= 0) {
                new AlertDialog.Builder(this)
                        .setTitle("获取信息失败")
                        .setPositiveButton("我知道了", null)
                        .show();
            } else {
                //获取成功
                username = userInfo.getUsername();
                carnum = userInfo.getCarnum();
                honest_time = userInfo.getConstimes();
                unhonest_time = userInfo.getNocontimes();
                numofheard = (int) (5 * honest_time * 1.0 / (honest_time + unhonest_time));
                setViewData();
            }
        }
    }

    private void setViewData() {
        phone_txt.setText(username);
        carnum_txt.setText(carnum);
        honesttime_txt.setText(String.valueOf(honest_time));
        unhonesttime_txt.setText(String.valueOf(unhonest_time));
        switch (numofheard) {
            case 5:
                heard1.setVisibility(View.VISIBLE);
                heard2.setVisibility(View.VISIBLE);
                heard3.setVisibility(View.VISIBLE);
                heard4.setVisibility(View.VISIBLE);
                heard5.setVisibility(View.VISIBLE);
                break;
            case 4:
                heard1.setVisibility(View.VISIBLE);
                heard2.setVisibility(View.VISIBLE);
                heard3.setVisibility(View.VISIBLE);
                heard4.setVisibility(View.VISIBLE);
                break;
            case 3:
                heard1.setVisibility(View.VISIBLE);
                heard2.setVisibility(View.VISIBLE);
                heard3.setVisibility(View.VISIBLE);
                break;
            case 2:
                heard1.setVisibility(View.VISIBLE);
                heard2.setVisibility(View.VISIBLE);
                break;
            case 1:
                heard1.setVisibility(View.VISIBLE);
                break;
            case 0:
                heard1.setVisibility(View.VISIBLE);
                heard2.setVisibility(View.VISIBLE);
                heard3.setVisibility(View.VISIBLE);
                heard4.setVisibility(View.VISIBLE);
                heard5.setVisibility(View.VISIBLE);
                break;
        }
        progDialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("NUM", 2);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
