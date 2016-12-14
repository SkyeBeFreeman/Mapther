package com.example.administrator.mapther;

/**
 * Created by chenjg on 2016/12/8.
 */

public class DeliverItem {
    private String time;
    private String date;
    private String context;

    public DeliverItem(String time, String date, String context) {
        this.context = context;
        this.date = date;
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

    public String getDate() {
        return this.date;

    }

    public String getContext() {
        return this.context;
    }

}
