package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class MotifyPasswordPost extends Post {

    public MotifyPasswordPost(String username, String password, String newpassword, PostRequestCallback postRequestCallback) {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/user/forgetPassword";
        map = new HashMap<>();
        map.put("username", username);
        map.put("oldPassword", password);
        map.put("newPassword", newpassword);
        send();
    }

}
