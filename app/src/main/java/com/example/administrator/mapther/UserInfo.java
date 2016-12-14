package com.example.administrator.mapther;

/**
 * Created by chenjg on 2016/12/9.
 */

public class UserInfo {
    private int id;
    private String name;
    private String password;
    private String phone;

    public UserInfo(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public UserInfo(int id, String name, String password, String phone) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return this.id;
    }
}
