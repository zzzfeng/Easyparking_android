package com.example.administrator.easyparking.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/22 0022.
 */
public  class JsonPostRequest extends Request<JSONObject> {
    private Map<String,String> mMap;
    private Response.Listener<JSONObject> mListener;


    public JsonPostRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map map) {
        super(Request.Method.POST, url, errorListener);
        mListener=listener;
        mMap=map;
        setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return mMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            Log.d("TAG","JsonPostRequest---jsonString:"+ jsonString);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

}