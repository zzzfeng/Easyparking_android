package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class GetUserChangePost extends Post {

    public GetUserChangePost(String username, PostRequestCallback postRequestCallback) {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/user/getChange";
        map = new HashMap<>();
        map.put("username", username);
        send();
    }

}
