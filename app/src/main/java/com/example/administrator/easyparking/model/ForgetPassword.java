package com.example.administrator.easyparking.model;

/**
 * Created by Administrator on 2015/10/8 0008.
 */
public class ForgetPassword {
    int result;
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "Login{" +
                "result=" + result +
                ", message='" + message +
                '}';
    }
}
