package com.example.administrator.easyparking.model;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class UserInfo {
    private String username;
    private String carnum;
    int result;
    int constimes;
    int noconstimes;
    int change;

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int reuslt) {
        this.result = reuslt;
    }

    public int getConstimes() {
        return constimes;
    }

    public void setConstimes(int constimes) {
        this.constimes = constimes;
    }

    public int getNocontimes() {
        return noconstimes;
    }

    public void setNocontimes(int nocontimes) {
        this.noconstimes = nocontimes;
    }

    @Override
    public String toString() {
        return "UserInfo { " +
                "username :" + username +
                "carnum :" + carnum +
                "result :" + result +
                "constimes :" + constimes +
                "noconstimes :" + noconstimes +
                "}";

    }
}
