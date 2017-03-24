package com.example.administrator.easyparking.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

public class Post {
    RequestQueue requestQueue = EasyParkingApplication.getRequestQueue();
    Map<String, Object> map;
    PostRequestCallback postRequestCallback;
    String url = null;

    public Post(PostRequestCallback postRequestCallback) {
        this.postRequestCallback = postRequestCallback;
    }


    protected void send() {
        JsonPostRequest jsonPostRequest = new JsonPostRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                postRequestCallback.onFinish(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError)//Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒
                {
                    Log.d("TAG", "Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒:" + volleyError.getMessage(), volleyError);
                } else if (volleyError instanceof NoConnectionError)//客户端没有网络连接
                {
                    Log.d("TAG", "客户端没有网络连接:" + volleyError.getMessage(), volleyError);
                } else if (volleyError instanceof AuthFailureError)//HTTP的身份验证错误
                {
                    Log.d("TAG", "HTTP的身份验证错误:" + volleyError.getMessage(), volleyError);
                } else if (volleyError instanceof ServerError)//服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码
                {
                    Log.d("TAG", "服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码:" + volleyError.getMessage(), volleyError);
                } else if (volleyError instanceof NetworkError)//Socket关闭，服务器宕机，DNS错误都会产生这个错误
                {
                    Log.d("TAG", "Socket关闭，服务器宕机，DNS错误都会产生这个错误:" + volleyError.getMessage(), volleyError);
                } else if (volleyError instanceof ParseError)//在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
                {
                    Log.d("TAG", "在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。:" + volleyError.getMessage(), volleyError);
                }
                postRequestCallback.onError(volleyError);
            }
        }, map);
        requestQueue.add(jsonPostRequest);
    }
}
