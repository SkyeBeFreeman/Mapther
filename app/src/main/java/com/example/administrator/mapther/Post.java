package com.example.administrator.mapther;

import java.io.Serializable;

/**
 * Created by zhtian on 2016/12/9.
 */

public class Post implements Serializable {

    private int post_id;
    private String user_name;
    private String content;
    private String phone;

    public Post(int post_id, String user_name, String content, String phone) {
        this.post_id = post_id;
        this.user_name = user_name;
        this.phone = phone;
        this.content = content;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
