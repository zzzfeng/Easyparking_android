package com.example.administrator.easyparking.activitys;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.administrator.easyparking.R;
import com.example.administrator.easyparking.utils.EasyParkingApplication;

/**
 * Created by Administrator on 2015/11/22 0022.
 */
public class GoToPay extends Activity {

    private Button btn_finisher, btn_back;
    private AlarmManager alarmManager;
    private String start_time, end_time;
    private int distance;
    private long temp, require;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.orderfinished);
        Bundle bundle = getIntent().getExtras();
        start_time = bundle.getString("start_time");
        end_time = bundle.getString("end_time");
        calcalateTime();
        InitView();
        setListener();

    }

    private void calcalateTime() {
        String[] e_temp = end_time.split(" ");
        String[] e_temp2 = e_temp[1].split(":");
        int e_minute = Integer.valueOf(e_temp2[0]) * 60 + Integer.valueOf(e_temp2[1]);
        String[] s_temp = start_time.split(" ");
        String[] s_temp2 = s_temp[1].split(":");
        int s_minute = Integer.valueOf(s_temp2[0]) * 60 + Integer.valueOf(s_temp2[1]);
        int reminder_time = EasyParkingApplication.getReminder_time();
        if (reminder_time != 0) {
            require = (s_minute - reminder_time) * 60 * 1000;
            setBorderCast(require);
        }
        Log.d("TAG", "s_minute :" + s_minute + "e_minute " + e_minute + "   " + "minuteDisance:" + (e_minute - s_minute));
    }

    private void setBorderCast(long require) {
        alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(GoToPay.this, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(GoToPay.this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC, require, pIntent);

    }

    private void setListener() {
        btn_finisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoToPay.this, RecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShow", true);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoToPay.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("NUM", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    private void InitView() {
        btn_finisher = (Button) findViewById(R.id.btn_finishorder);
        btn_back = (Button) findViewById(R.id.btn_back);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("NUM", 0);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
