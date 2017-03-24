package com.example.administrator.easyparking;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.administrator.easyparking.activitys.ChargePayAcitivy;
import com.example.administrator.easyparking.activitys.LoginActivity;
import com.example.administrator.easyparking.activitys.MyInfo;
import com.example.administrator.easyparking.activitys.RecordActivity;
import com.example.administrator.easyparking.activitys.RegisterPasswordActivity;
import com.example.administrator.easyparking.activitys.SetPasswordActivity;
import com.example.administrator.easyparking.activitys.freePlaceList;
import com.example.administrator.easyparking.activitys.motifyPasswordActivity;
import com.example.administrator.easyparking.activitys.orderlistActivity;
import com.example.administrator.easyparking.fragment.fragment_map;
import com.example.administrator.easyparking.fragment.fragment_mine;
import com.example.administrator.easyparking.model.CancelOrder;
import com.example.administrator.easyparking.model.ForgetPassword;
import com.example.administrator.easyparking.model.GetChange;
import com.example.administrator.easyparking.model.Login;
import com.example.administrator.easyparking.model.MotifyPassword;
import com.example.administrator.easyparking.model.Parkinglots;
import com.example.administrator.easyparking.model.ParkinglotsRatio;
import com.example.administrator.easyparking.model.RecordList;
import com.example.administrator.easyparking.model.Register;
import com.example.administrator.easyparking.model.UserInfo;
import com.example.administrator.easyparking.model.freeplaceList;
import com.example.administrator.easyparking.model.orderParkingUtil;
import com.example.administrator.easyparking.utils.CancelOrderPost;
import com.example.administrator.easyparking.utils.EasyParkingApplication;
import com.example.administrator.easyparking.utils.ForgetPasswordPost;
import com.example.administrator.easyparking.utils.GetUserChangePost;
import com.example.administrator.easyparking.utils.GetUserInfoPost;
import com.example.administrator.easyparking.utils.LoginPost;
import com.example.administrator.easyparking.utils.MotifyPasswordPost;
import com.example.administrator.easyparking.utils.PostRequestCallback;
import com.example.administrator.easyparking.utils.RegisterPost;
import com.example.administrator.easyparking.utils.chargePost;
import com.example.administrator.easyparking.utils.getFreePlacesPost;
import com.example.administrator.easyparking.utils.getParkinglotsPost;
import com.example.administrator.easyparking.utils.getParkinglotsRatioPost;
import com.example.administrator.easyparking.utils.getRecordsPost;
import com.example.administrator.easyparking.utils.orderParkPost;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class Service {
    private static ExecutorService execute = Executors.newFixedThreadPool(1);
    private Activity _activity;
    private Fragment _fragment;
    private String sessionId = EasyParkingApplication.getSessionId();

    public Service(Activity activity) {
        this._activity = activity;
    }

    public Service(Fragment fragment) {
        this._fragment = fragment;
        this._activity = fragment.getActivity();
    }

    //执行任务，并捕捉线程等错误
    private void executeTheAssignment(Runnable runnable, String name) {
        try {
            execute.execute(runnable);
        } catch (Exception e) {
            Log.d("TAG", "Service " + name + " :" + e.getMessage(), e);
        }
    }

    public void login(String username, String password) {
        executeTheAssignment(new _login(username, password), "login");
    }

    private class _login implements Runnable {
        String username;
        String password;

        public _login(String... params) {
            username = params[0];
            password = params[1];
        }

        public void run() {
            LoginPost loginPost = new LoginPost(username, password, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    Login login = Utility.handleLogin(jsonObject);
                    ((LoginActivity) _activity).loginResult(login);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    ((LoginActivity) _activity).loginResult(null);
                    Log.d("TAG", "volley login:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }


    public void cancelOrder(String record_id) {
        executeTheAssignment(new _cancelOrder(record_id), "cancelOrder");
    }

    private class _cancelOrder implements Runnable {
        String record_id;

        public _cancelOrder(String... params) {
            record_id = params[0];
        }

        public void run() {
            CancelOrderPost post = new CancelOrderPost(record_id, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    CancelOrder cancelOrder = Utility.handleCancelOrder(jsonObject);
                    ((RecordActivity) _activity).cencelOrderResult(cancelOrder);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    CancelOrder cancelOrder = Utility.handleCancelOrder(null);
                    Log.d("TAG", "volley login:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }

    public void getParkinglots(String list_title, String list_local) {
        executeTheAssignment(new _getParkinglots(list_title, list_local), "getParkinglots");
    }

    public void register(String username, String password) {
        executeTheAssignment(new _register(username, password), "register");
    }

    private class _register implements Runnable {
        String username;
        String password;

        public _register(String... params) {
            username = params[0];
            password = params[1];
        }

        @Override
        public void run() {
            RegisterPost registerPost = new RegisterPost(username, password, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    Register register = Utility.handleRegister(jsonObject);
                    ((RegisterPasswordActivity) _activity).registerResult(register);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    ((RegisterPasswordActivity) _activity).registerResult(null);
                    Log.d("TAG", "volley register:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }

    public void getRecords(String username) {
        executeTheAssignment(new _getRecords(username), "getRecords");
    }

    private class _getRecords implements Runnable {
        String username;

        public _getRecords(String... params) {
            this.username = params[0];
        }

        @Override
        public void run() {
            getRecordsPost getrecordspost = new getRecordsPost(username, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    RecordList recordList = Utility.handleRecordList(jsonObject);
                    ((RecordActivity) _activity).getRecordResult(recordList);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    ((RecordActivity) _activity).getRecordResult(null);
                    Log.d("TAG", "volley getRecords:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }

    public void motifyPassword(String username, String oldpassword, String newpassword) {
        executeTheAssignment(new _motifyPassword(username, oldpassword, newpassword), "motifypassword");
    }

    private class _motifyPassword implements Runnable {
        String username, oldpassword, newpassword;

        public _motifyPassword(String username, String oldpassword, String newpassword) {
            this.username = username;
            this.oldpassword = oldpassword;
            this.newpassword = newpassword;
        }

        @Override
        public void run() {
            MotifyPasswordPost motifyPasswordPost = new MotifyPasswordPost(username, oldpassword, newpassword, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    MotifyPassword motifyPassword = Utility.handleMotifyPassword(jsonObject);
                    ((motifyPasswordActivity) _activity).getMotifyResult(motifyPassword);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    ((motifyPasswordActivity) _activity).getMotifyResult(null);
                    Log.d("TAG", "volley motifyPassword:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }


    public void forgetPassword(String username, String newpassword) {
        executeTheAssignment(new _ForgetPassword(username, newpassword), "forgetpassword");
    }

    private class _ForgetPassword implements Runnable {
        String username, newpassword;

        public _ForgetPassword(String username, String newpassword) {
            this.username = username;
            this.newpassword = newpassword;
        }

        @Override
        public void run() {
            ForgetPasswordPost forgetPasswordPost = new ForgetPasswordPost(username, newpassword, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    ForgetPassword forgetPassword = Utility.handleForgetPassword(jsonObject);
                    ((SetPasswordActivity) _activity).forgetPasswordResult(forgetPassword);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    ((SetPasswordActivity) _activity).forgetPasswordResult(null);
                    Log.d("TAG", "volley motifyPassword:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }

    public void getfreeplacesList(String parking_name, String local, String start_time, String end_time) {
        executeTheAssignment(new _getfreeplaceList(parking_name, local, start_time, end_time), "getfreeplaceslist");
    }


    private class _getfreeplaceList implements Runnable {
        String parking_name, local, start_time, end_time;

        public _getfreeplaceList(String parking_name, String local, String start_time, String end_time) {
            this.parking_name = parking_name;
            this.local = local;
            this.start_time = start_time;
            this.end_time = end_time;
        }

        @Override
        public void run() {
            getFreePlacesPost getfreeplacePost = new getFreePlacesPost(parking_name, local, start_time, end_time, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    freeplaceList freeplacelist = Utility.handlefreeplacesList(jsonObject);
                    ((freePlaceList) _activity).getfreeplaceResult(freeplacelist);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    ((freePlaceList) _activity).getfreeplaceResult(null);
                    Log.d("TAG", "volley getfreeplace:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }


    public void getUserInfo(String username) {
        executeTheAssignment(new _getUserInfo(username), "getUserInfo");
    }

    private class _getUserInfo implements Runnable {
        private String username;

        public _getUserInfo(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            GetUserInfoPost getUserInfoPost = new GetUserInfoPost(username, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    UserInfo userInfo = Utility.handleUserInfo(jsonObject);
                    ((MyInfo) _activity).getUserInfoResult(userInfo)
                    ;
                }

                @Override
                public void onError(VolleyError volleyError) {
                    UserInfo userInfo = Utility.handleUserInfo(null);
                    Log.d("TAG", "volley getUserInfo:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }

    public void orderParking(String username, String start_time, String end_time, String order_time, int parking_id, int park_id, int flag, float expense, float paid, float unpaid) {
        executeTheAssignment(new _orderParking(username, start_time, end_time, order_time, parking_id, park_id, flag, expense, paid, unpaid), "orderParking");

    }

    private class _orderParking implements Runnable {

        String username, start_time, end_time, order_time;
        int flag, parking_id, park_id;
        float expense, paid, unpaid;

        public _orderParking(String username, String start_time, String end_time, String order_time, int parking_id, int park_id, int flag, float expense, float paid, float unpaid) {
            this.username = username;
            this.start_time = start_time;
            this.end_time = end_time;
            this.order_time = order_time;
            this.parking_id = parking_id;
            this.park_id = park_id;
            this.flag = flag;
            this.expense = expense;
            this.paid = paid;
            this.unpaid = unpaid;
        }

        @Override
        public void run() {
            orderParkPost orderparkpost = new orderParkPost(username, start_time, end_time, order_time, parking_id, park_id, flag, expense, paid, unpaid, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    orderParkingUtil orderparkingutil = Utility.handleOrderParking(jsonObject);
                    ((freePlaceList) _activity).getOrderParkingResult(orderparkingutil);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    orderParkingUtil orderparkingutil = Utility.handleOrderParking(null);
                    Log.d("TAG", "volley orderPost:" + volleyError.getMessage(), volleyError);
                }
            });
        }

    }


    private class _getParkinglots implements Runnable {

        private String list_parkingname, list_local;

        public _getParkinglots(String list_title, String list_local) {
            this.list_parkingname = list_title;
            this.list_local = list_local;
        }

        @Override
        public void run() {
            getParkinglotsPost getParkinglotspost = new getParkinglotsPost(list_parkingname, list_local, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    Parkinglots parkinglots = Utility.handleGetParkinglots(jsonObject);
                    ((orderlistActivity) _activity).getParkinglotsResult(parkinglots);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    Parkinglots parkinglots = Utility.handleGetParkinglots(null);
                    Log.d("TAG", "volley getParkinglotsPost:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }

    public void getParkinglotsRatio(String parkingnames, String locals) {
        executeTheAssignment(new _getParkinglotsRatio(parkingnames, locals), "getParkinglotsRatio");
    }

    private class _getParkinglotsRatio implements Runnable {

        private String list_parkingname, list_local;

        public _getParkinglotsRatio(String list_title, String list_local) {
            this.list_parkingname = list_title;
            this.list_local = list_local;
        }

        @Override
        public void run() {
            getParkinglotsRatioPost post = new getParkinglotsRatioPost(list_parkingname, list_local, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    ParkinglotsRatio parkinglotsRatio = Utility.handleGetParkinglotsRatio(jsonObject);
                    ((fragment_map) _fragment).getParkinglotsRatioResult(parkinglotsRatio);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    ParkinglotsRatio parkinglotsRatio = Utility.handleGetParkinglotsRatio(null);
                    Log.d("TAG", "volley getParkinglotsRatioPost:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }

    public void getUserChange(String username) {
        executeTheAssignment(new _getUserChange(username), "getUserChange");
    }

    private class _getUserChange implements Runnable {

        private String username;

        public _getUserChange(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            GetUserChangePost post = new GetUserChangePost(username, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    GetChange getChange = Utility.handleGetUserChange(jsonObject);
                    ((fragment_mine) _fragment).getUserChangeResult(getChange);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    GetChange getChange = Utility.handleGetUserChange(null);
                    Log.d("TAG", "volley getUserChange:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }


    public void charge(String username, int price) {
        executeTheAssignment(new _charge(username, price), "charge");
    }


    private class _charge implements Runnable {

        private String username;
        private int price;

        public _charge(String username, int price) {
            this.username = username;
            this.price = price;
        }

        @Override
        public void run() {
            chargePost post = new chargePost(username, price, new PostRequestCallback() {
                @Override
                public void onFinish(JSONObject jsonObject) {
                    GetChange getChange = Utility.handleCharge(jsonObject);
                    ((ChargePayAcitivy) _activity).getChargeResult(getChange);
                }

                @Override
                public void onError(VolleyError volleyError) {
                    GetChange getChange = Utility.handleCharge(null);
                    Log.d("TAG", "volley charge:" + volleyError.getMessage(), volleyError);
                }
            });
        }
    }

}
