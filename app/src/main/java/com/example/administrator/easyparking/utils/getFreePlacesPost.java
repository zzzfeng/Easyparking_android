package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/10 0010.
 */
public class getFreePlacesPost extends Post {
    public getFreePlacesPost(String parking_name,String local ,String start_time,String end_time ,PostRequestCallback postRequestCallback) {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/record/getFreeplaceList2?";
         map = new HashMap<>();
        map.put("parking_name", parking_name);
        map.put("local",local);
        map.put("start_time",start_time);
        map.put("end_time",end_time);
        send();
    }
}
