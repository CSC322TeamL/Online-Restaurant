package com.example.simplerestaurant.beans;

import java.io.Serializable;

public class SimpleResponseBean implements Serializable {
    private int code;
    private String content;

    public SimpleResponseBean(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "UserLoginBean{" +
                "code=" + code +
                ", content='" + content + '\'' +
                '}';
    }
}
