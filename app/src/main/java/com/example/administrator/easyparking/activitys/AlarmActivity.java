package com.example.administrator.easyparking.activitys;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.example.administrator.easyparking.R;

import java.io.IOException;

public class AlarmActivity extends Activity {
    private Vibrator vibrator;
    private MediaPlayer alarmMusic;

    @Override
    protected void onCreate(Bundle savedInsatnceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInsatnceState);

        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{1000, 1000, 1000, 1000}, 0);

        alarmMusic = MediaPlayer.create(this, R.raw.piano);
        try {
            alarmMusic.prepare();    //准备
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        alarmMusic.setLooping(true);
        alarmMusic.start();

        AlertDialog.Builder cDialog = new AlertDialog.Builder(AlarmActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_dialog, null);
        cDialog.setView(view);
        cDialog.setPositiveButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vibrator.cancel();
                alarmMusic.stop();
                alarmMusic.release();
                AlarmActivity.this.finish();
            }
        });
        cDialog.create();
        cDialog.show();
    }

}
