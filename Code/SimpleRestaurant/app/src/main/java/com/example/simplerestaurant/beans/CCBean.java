package com.example.simplerestaurant.beans;

public class CCBean {
    private String _id;
    private String context;
    private String createDate;
    private String finalizedDate;
    private String fromID;
    private String isComplaint;
    private String orderID;
    private String reviewBy;
    private String status;
    private String subject;
    private String toID;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFinalizedDate() {
        return finalizedDate;
    }

    public void setFinalizedDate(String finalizedDate) {
        this.finalizedDate = finalizedDate;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getIsComplaint() {
        return isComplaint;
    }

    public void setIsComplaint(String isComplaint) {
        this.isComplaint = isComplaint;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(String reviewBy) {
        this.reviewBy = reviewBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    @Override
    public String toString() {
        return "CCBean{" +
                "_id='" + _id + '\'' +
                ", context='" + context + '\'' +
                ", createDate='" + createDate + '\'' +
                ", finalizedDate='" + finalizedDate + '\'' +
                ", fromID='" + fromID + '\'' +
                ", isComplaint='" + isComplaint + '\'' +
                ", orderID='" + orderID + '\'' +
                ", reviewBy='" + reviewBy + '\'' +
                ", status='" + status + '\'' +
                ", subject='" + subject + '\'' +
                ", toID='" + toID + '\'' +
                '}';
    }
}
