package com.example.simplerestaurant;

import com.example.simplerestaurant.beans.OrderBean;

import java.util.ArrayList;

public class DeliveryDataStore {
    private static DeliveryDataStore store;
    private static ArrayList<OrderBean> orderSending;
    private static ArrayList<OrderBean> orderWaiting;
    private static ArrayList<OrderBean> orderFinished;

    final public static int TYPE_SENDING = 1;
    final public static int TYPE_WAITING = 2;
    final public static int TYPE_FINISHED = 3;
    final public static int TYPE_BOTH = 4;

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

    public void addOrder2Finished(OrderBean newOrder){
        orderFinished.add(newOrder);
    }
    public void resetOrderFinished(ArrayList<OrderBean> newList){
        orderFinished.clear();
        orderFinished.addAll(newList);
    }
    public ArrayList<OrderBean> getOrderFinished(){
        return orderFinished;
    }

    public void addOrder2Waiting(OrderBean newOrder){
        orderWaiting.add(newOrder);
    }
    public void resetOrderWaiting(ArrayList<OrderBean> newList){
        orderWaiting.clear();
        orderWaiting.addAll(newList);
    }
    public ArrayList<OrderBean> getOrderWaiting() {
        return orderWaiting;
    }

    public OrderBean getOrderFromFinished(String orderID){
        for (OrderBean order :
                orderFinished) {
            if(orderID.equals(order.get_id())){
                return order;
            }
        }
        return null;
    }
}
