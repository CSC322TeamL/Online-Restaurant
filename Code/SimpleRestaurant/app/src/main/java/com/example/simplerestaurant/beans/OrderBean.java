package com.example.simplerestaurant.beans;

import java.util.ArrayList;
import java.util.List;

public class OrderBean {
    private String _id = "";
    private String customerID = "";
    private float orderTotal = 0;
    private float orderCharged = 0;
    private float discount = 0;
    private List<DishInCart> dishDetail = new ArrayList<>();
    private String createDate = "";
    private String status = "";
    private String isDelivery = "";

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public float getOrderCharged() {
        return orderCharged;
    }

    public void setOrderCharged(float orderCharged) {
        this.orderCharged = orderCharged;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public List<DishInCart> getDishDetail() {
        return dishDetail;
    }

    public void setDishDetail(List<DishInCart> dishDetail) {
        this.dishDetail = dishDetail;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(String isDelivery) {
        this.isDelivery = isDelivery;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "_id='" + _id + '\'' +
                ", customerID='" + customerID + '\'' +
                ", orderTotal=" + orderTotal +
                ", orderCharged=" + orderCharged +
                ", discount=" + discount +
                ", dishDetail=" + dishDetail +
                ", createDate='" + createDate + '\'' +
                ", status='" + status + '\'' +
                ", isDelivery='" + isDelivery + '\'' +
                '}';
    }
}
