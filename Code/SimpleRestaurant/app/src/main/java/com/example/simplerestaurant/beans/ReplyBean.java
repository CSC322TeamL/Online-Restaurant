package com.example.simplerestaurant.beans;

public class ReplyBean {
    private String _id;
    private DiscussionDetailBean detail;
    private String displayName;
    private String targetDiscussion;
    private String userID;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public DiscussionDetailBean getDetail() {
        return detail;
    }

    public void setDetail(DiscussionDetailBean detail) {
        this.detail = detail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTargetDiscussion() {
        return targetDiscussion;
    }

    public void setTargetDiscussion(String targetDiscussion) {
        this.targetDiscussion = targetDiscussion;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "ReplyBean{" +
                "_id='" + _id + '\'' +
                ", detail=" + detail +
                ", displayName='" + displayName + '\'' +
                ", targetDiscussion='" + targetDiscussion + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
