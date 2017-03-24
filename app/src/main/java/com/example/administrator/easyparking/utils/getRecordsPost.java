package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/10 0010.
 */
public class getRecordsPost extends Post {
    public getRecordsPost(String username, PostRequestCallback postRequestCallback) {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/record/getRecords";
        map = new HashMap<>();
        map.put("username", username);
        send();
    }
}
