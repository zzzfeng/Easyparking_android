package com.example.administrator.easyparking.utils;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/22 0022.
 */
public interface PostRequestCallback {
    public void onFinish(JSONObject jsonObject);
    public void onError(VolleyError volleyError);
}
