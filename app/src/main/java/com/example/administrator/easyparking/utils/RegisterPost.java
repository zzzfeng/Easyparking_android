package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/10 0010.
 */
public class RegisterPost extends Post {
    public RegisterPost(String username, String password, PostRequestCallback postRequestCallback) {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/user/registerUser";
        map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
//        Log.d("TAG", "username :" + username + " , password : " + password);
        send();
    }
}
