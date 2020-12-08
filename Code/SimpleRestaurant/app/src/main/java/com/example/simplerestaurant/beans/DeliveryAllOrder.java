package com.example.simplerestaurant.beans;

import java.util.ArrayList;

public class DeliveryAllOrder {
    private ArrayList<OrderBean> orderPicked;
    private ArrayList<OrderBean> orderDelivered;

    public ArrayList<OrderBean> getOrderPicked() {
        return orderPicked;
    }

    public void setOrderPicked(ArrayList<OrderBean> orderPicked) {
        this.orderPicked = orderPicked;
    }

    public ArrayList<OrderBean> getOrderDelivered() {
        return orderDelivered;
    }

    public void setOrderDelivered(ArrayList<OrderBean> orderDelivered) {
        this.orderDelivered = orderDelivered;
    }

    @Override
    public String toString() {
        return "DeliveryAllOrder{" +
                "orderPicked=" + orderPicked +
                ", orderDelivered=" + orderDelivered +
                '}';
    }
}
