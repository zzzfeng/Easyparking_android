package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by tan on 2017/1/8 0008.
 */
public class getParkinglotsRatioPost extends Post {
    public getParkinglotsRatioPost(String str_list_parkingname, String str_list_local, PostRequestCallback postRequestCallback) {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/record/getParkinglotsRatio?";
        map = new HashMap<>();
        map.put("list_parkingname", str_list_parkingname);
        map.put("list_local", str_list_local);
        map.put("username",EasyParkingApplication.getUsername());
        send();
    }
}