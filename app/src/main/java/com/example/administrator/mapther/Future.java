package com.example.administrator.mapther;

/**
 * Created by Administrator on 2016/12/9.
 */

public class Future {
    private String week;
    private String weather;
    private String trange;

    public Future() {}

    public Future(String week, String weather, String trange) {
        this.week = week;
        this.weather = weather;
        this.trange = trange;
    }

    public String getWeek() {
        return week;
    }

    public String getWeather() {
        return weather;
    }

    public String getTrange() {
        return trange;
    }

}
