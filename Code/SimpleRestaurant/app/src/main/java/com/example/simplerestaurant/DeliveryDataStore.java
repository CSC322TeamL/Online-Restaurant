package com.example.simplerestaurant;

import com.example.simplerestaurant.beans.OrderBean;

import java.util.ArrayList;

public class DeliveryDataStore {
    private static DeliveryDataStore store;
    private static ArrayList<OrderBean> orderSending;
    private static ArrayList<OrderBean> orderWaiting;
    private static ArrayList<OrderBean> orderFinished;

    public static DeliveryDataStore getInstance(){
        if(null == store){
            store = new DeliveryDataStore();
            orderFinished = new ArrayList<>();
            orderWaiting = new ArrayList<>();
            orderSending = new ArrayList<>();
        }
        return store;
    }

    public void addOrder2Sending(OrderBean newOrder){
        orderSending.add(newOrder);
    }
    public void resetOrderSending(ArrayList<OrderBean> newList){
        orderSending.clear();
        orderSending.addAll(newList);
    }
    public ArrayList<OrderBean> getOrderSending(){
        return orderSending;
    }
}
