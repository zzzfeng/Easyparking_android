package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class chargePost extends Post {

    public chargePost(String username, int price, PostRequestCallback postRequestCallback) {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/user/charge";
        map = new HashMap<>();
        map.put("username", username);
        map.put("num", "" + price);
        send();
    }

}
