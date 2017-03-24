package com.example.administrator.easyparking.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class orderParkPost extends Post {

    public orderParkPost(String username, String start_time, String end_time, String order_time, int parking_id, int park_id, int flag, float expense, float paid, float unpaid, PostRequestCallback postRequestCallback) {
        super(postRequestCallback);
        url = "http://139.199.6.218:8080/easyparking/record/orderParking";
        map = new HashMap<>();
        map.put("parking_id", String.valueOf(parking_id));//车场的id
        map.put("parking_num",String.valueOf(park_id));//具体的车位号
        map.put("start_time", start_time);
        map.put("end_time", end_time);
        map.put("order_time", order_time);
        map.put("complete", String.valueOf(flag));//订单是否有效，默认为0 即订单没有结束
        map.put("expense", String.valueOf(expense));
        map.put("username", username);
//        map.put("paid", String.valueOf(paid));
//        map.put("unpaid", String.valueOf(unpaid));
        send();
    }

}
