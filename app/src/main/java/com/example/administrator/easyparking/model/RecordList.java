package com.example.administrator.easyparking.model;

import java.util.List;

/**
 * Created by Administrator on 2015/11/20 0020.
 */
public class RecordList {
    int result;
    List<RecordBean> list;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<RecordBean> getList() {
        return list;
    }

    public void setList(List<RecordBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "RcordList{" +
                "list=" + list +
                ", result=" + result +
                '}';
    }
}
