package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class LoginPost extends Post {

    public LoginPost(String username,String password,PostRequestCallback postRequestCallback)
    {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/user/login";
        map=new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        send();
    }

}
