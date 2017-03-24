package com.example.administrator.easyparking.model;

import java.io.Serializable;

/**
 * Created by tan on 2017/3/4 0004.
 */
public class ItemModel implements Serializable {

    public static final int ONE = 1001;
    public static final int TWO = 1002;

    public int type;
    public Object data;

    public ItemModel(int type, Object data) {
        this.type = type;
        this.data = data;
    }
}
