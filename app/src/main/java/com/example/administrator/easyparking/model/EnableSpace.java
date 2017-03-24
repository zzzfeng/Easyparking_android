package com.example.administrator.easyparking.model;

import java.util.List;

/**
 * Created by tan on 2017/1/2 0002.
 */
public class EnableSpace {

    private List<Integer> spacesnumber;

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

}
