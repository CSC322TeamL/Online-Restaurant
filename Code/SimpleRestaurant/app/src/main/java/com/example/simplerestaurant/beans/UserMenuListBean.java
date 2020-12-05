package com.example.simplerestaurant.beans;

public class UserMenuListBean {
    // use type to indicate which viewholder should be applied
    final public static int TYPE_MENU = 0;
    final public static int TYPE_DISH = 1;
    private int type;
    private String title;
    private DishBean dish;

    public UserMenuListBean(String title){
        this.type = TYPE_MENU;
        this.title = title;
        this.dish = null;
    }

    public UserMenuListBean(DishBean dish){
        this.type = TYPE_DISH;
        this.dish = dish;
        this.title = null;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public DishBean getDish() {
        return dish;
    }

}
