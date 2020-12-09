package com.example.simplerestaurant.Interfaces;

public interface DeliveryActionInterface {
    void onDeliveredClick(String orderID);
    void onPickUpClick(String orderID);
    void onComplaintClick(String orderID);

    void onSendingFragmentRefresh();
    void onWaitingFragmentRefresh();
    void onFinishedFragmentRefresh();
}
