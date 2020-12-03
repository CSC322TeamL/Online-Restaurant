package com.example.simplerestaurant.beans;

import java.util.List;

public class MenuBean {
    private String title;
    private List<DishBean> dishes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DishBean> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishBean> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "MenuBean{" +
                "title='" + title + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}
