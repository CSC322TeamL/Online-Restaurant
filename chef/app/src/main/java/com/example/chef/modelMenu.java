package com.example.chef;

public class modelMenu {

    private String dishname, price, describe, dishid, name1,price1,describe1, key, raiting;
    private int imgpath;

    public modelMenu(String dishname, String price, String describe, int imgpath, String dishid, String name1,String price1,String describe1, String key, String raiting) {
        this.dishname = dishname;
        this.price = price;
        this.describe = describe;
        this.imgpath = imgpath;
        this.dishid = dishid;
        this.name1 = name1;
        this.price1 = price1;
        this.describe1 = describe1;
        this.key = key;
        this.raiting = raiting;
    }

    public String getRaiting() {
        return raiting;
    }

    public void setRaiting(String raiting) {
        this.raiting = raiting;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getDescribe1() {
        return describe1;
    }

    public void setDescribe1(String describe1) {
        this.describe1 = describe1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDishid() {
        return dishid;
    }

    public void setDishid(String dishid) {
        this.dishid = dishid;
    }

    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getImgpath() {
        return imgpath;
    }

    public void setImgpath(int imgpath) {
        this.imgpath = imgpath;
    }
}
