package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class CancelOrderPost extends Post {

    public CancelOrderPost(String order_id, PostRequestCallback postRequestCallback)
    {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/record/cancelOrder";
        map=new HashMap<>();
        map.put("order_id", order_id);
        send();
    }

}
