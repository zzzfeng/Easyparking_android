package com.example.administrator.easyparking.model;

import java.util.List;

/**
 * Created by Administrator on 2015/11/20 0020.
 */
public class freeplaceList {
    int result;
    float expense;
    List<Integer> list;
    String message;
    int parking_id;

    public float getExpense() {
        return expense;
    }

    public void setExpense(float expense) {
        this.expense = expense;
    }

    public String getMessage() {
        return message;
    }

    public void setMesage(String message) {
        this.message = message;
    }

    public int getParking_id() {
        return parking_id;
    }

    public void setParking_id(int parking_id) {
        this.parking_id = parking_id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "RcordList{" +
                "list=" + list +
                ", result=" + result +
                ", meesage=" + message +
                ", parking_id=" + parking_id +
                '}';
    }
}
