package com.example.administrator.easyparking.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class EasyParkingApplication extends Application {

    {
        PlatformConfig.setWeixin("wxb0e2eb091a70c6a8", "c2539051c2e04aa784fbb37c2ea40cd8");
        PlatformConfig.setSinaWeibo("1819089202", "0fcff16bae3c49b48eb3165a1a032a4f");
        PlatformConfig.setQQZone("1105842805", "pA1faBhnafX28dYt");
    }

    private static Context context;
    private static RequestQueue requestQueue;
    private static String city;
    private static String username;
    private static int reminder_time;


    @Override
    public void onCreate() {
        super.onCreate();
//        Config.DEBUG = true;//这个用于开启Umeng debug模式
        UMShareAPI.get(this);
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);
    }

    public static int getReminder_time() {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences("user_reminder_time", MODE_PRIVATE);
            int time = sp.getInt("reminder_time", 0);
            return time;
        } else {
            return 0;
        }
//        return reminder_time;
    }

    public static void setReminder_time(int reminder_time) {
        EasyParkingApplication.reminder_time = reminder_time;
    }

    public static Context getContext() {
        return context;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static void setUsername(String u) {
        username = u;
    }

    public static String getSessionId() {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences("login_information", MODE_PRIVATE);
            String _sessionId = sp.getString("session_id", "");
            return _sessionId;
        } else {
            return "";
        }
    }

    public static void setCity(String s) {
        city = s;
    }

    public static String getCity() {
        return city;
    }

    public static String getUsername() {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences("login_information", MODE_PRIVATE);
            String _sessionId = sp.getString("username", "");
            return _sessionId;
        } else {
            return "";
        }
    }

}
