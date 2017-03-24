package com.example.administrator.easyparking;

import android.util.Log;

import com.example.administrator.easyparking.model.CancelOrder;
import com.example.administrator.easyparking.model.ForgetPassword;
import com.example.administrator.easyparking.model.GetChange;
import com.example.administrator.easyparking.model.Login;
import com.example.administrator.easyparking.model.MotifyPassword;
import com.example.administrator.easyparking.model.Parkinglots;
import com.example.administrator.easyparking.model.ParkinglotsRatio;
import com.example.administrator.easyparking.model.RecordBean;
import com.example.administrator.easyparking.model.RecordList;
import com.example.administrator.easyparking.model.Register;
import com.example.administrator.easyparking.model.UserInfo;
import com.example.administrator.easyparking.model.freeplaceList;
import com.example.administrator.easyparking.model.orderParkingUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class Utility {
    private static Gson gson = new Gson();

    //检测返回值result为-1时的情况
    private static boolean checkResult(JSONObject jsonObject) {
        try {
            String result = jsonObject.getString("result");
            if (result.equals("-1")) {
                return false;
            }
        } catch (JSONException e) {
            Log.d("TAG", "Utility checkResult:" + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static Login handleLogin(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), Login.class);
        }
        return null;
    }

    public static Register handleRegister(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), Register.class);
        }
        return null;
    }

    public static RecordList handleRecordList(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            RecordList recordList = gson.fromJson(String.valueOf(jsonObject), RecordList.class);

            try {
                if (recordList.getResult() == 1) {
                    List<RecordBean> list = gson.fromJson(jsonObject.getString("list"), new TypeToken<List<RecordBean>>() {
                    }.getType());
                    recordList.setList(list);
                }
                return recordList;
            } catch (JSONException e) {
                Log.d("TAG", "Utility handleRecordList :" + e.getMessage(), e);
            }
        }
        return null;
    }

    public static MotifyPassword handleMotifyPassword(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), MotifyPassword.class);
        }
        return null;
    }

    public static ForgetPassword handleForgetPassword(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), ForgetPassword.class);
        }
        return null;
    }

    public static freeplaceList handlefreeplacesList(JSONObject jsonObject) {

        if (jsonObject != null && checkResult(jsonObject)) {
            freeplaceList list = gson.fromJson(String.valueOf(jsonObject), freeplaceList.class);

            if (list.getResult() == 1) {
                try {
                    List<Integer> parking_list = gson.fromJson(jsonObject.getString("list"), new TypeToken<List<Integer>>() {
                    }.getType());
                    list.setList(parking_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
        return null;
    }


    public static UserInfo handleUserInfo(JSONObject jsonObject) {

        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), UserInfo.class);
        }

        return null;
    }

    public static orderParkingUtil handleOrderParking(JSONObject jsonObject) {

        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), orderParkingUtil.class);
        }

        return null;
    }

    public static Parkinglots handleGetParkinglots(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            Parkinglots parkinglots = gson.fromJson(String.valueOf(jsonObject), Parkinglots.class);

            if (parkinglots.getResult() == 1) {
                ArrayList<Double> list_charge_days = null;
                ArrayList<Double> list_charge_nights = null;
                ArrayList<Integer> frees = null;
                try {
                    list_charge_days = gson.fromJson(jsonObject.getString("charge_days"), new TypeToken<List<Double>>() {
                    }.getType());
                    list_charge_nights = gson.fromJson(jsonObject.getString("charge_nights"), new TypeToken<List<Double>>() {

                    }.getType());
                    frees = gson.fromJson(jsonObject.getString("frees"), new TypeToken<List<Integer>>() {

                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                parkinglots.setCharge_days(list_charge_days);
                parkinglots.setCharge_nights(list_charge_nights);
                parkinglots.setFrees(frees);
            }
            return parkinglots;
        }
        return null;
    }

    public static ParkinglotsRatio handleGetParkinglotsRatio(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            ParkinglotsRatio parkinglotsRatio = gson.fromJson(String.valueOf(jsonObject), ParkinglotsRatio.class);

            if (parkinglotsRatio.getResult() == 1) {

                ArrayList<Integer> ratio_list = null;
                try {
                    ratio_list = gson.fromJson(jsonObject.getString("ratio_list"), new TypeToken<List<Integer>>() {

                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                parkinglotsRatio.setRatio_list(ratio_list);
            }
            return parkinglotsRatio;
        }
        return null;
    }

    public static GetChange handleGetUserChange(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), GetChange.class);
        }
        return null;
    }

    public static GetChange handleCharge(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), GetChange.class);
        }
        return null;
    }

    public static CancelOrder handleCancelOrder(JSONObject jsonObject) {
        if (jsonObject != null && checkResult(jsonObject)) {
            return gson.fromJson(String.valueOf(jsonObject), CancelOrder.class);
        }
        return null;
    }
}
