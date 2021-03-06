package com.example.administrator.easyparking.model;

/**
 * Created by Administrator on 2015/11/20 0020.
 */
public class RecordBean {
    private String record_id;
    private String parking_name;
    private int parking_num;
    private boolean complete;
    private float expense;
    private String start_time;
    private String end_time;
    private String order_time;
    private boolean enable;
    private String remark;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String order_id) {
        this.record_id = order_id;
    }

    public String getParking_name() {
        return parking_name;
    }

    public void setParking_name(String parking_name) {
        this.parking_name = parking_name;
    }

    public int getParking_num() {
        return parking_num;
    }

    public void setParking_num(int parking_num) {
        this.parking_num = parking_num;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public float getExpense() {
        return expense;
    }

    public void setExpense(float expense) {
        this.expense = expense;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    @Override
    public String toString() {
        return "Record{" +
                "order_id =" + record_id +
                ", parking_name ='" + parking_name +
                ", expense='" + expense +
                '}';
    }
}
