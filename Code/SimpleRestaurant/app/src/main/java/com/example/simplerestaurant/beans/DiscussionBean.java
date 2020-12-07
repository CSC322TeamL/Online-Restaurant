package com.example.simplerestaurant.beans;

public class DiscussionBean {
    private String _id;
    private DiscussionDetailBean detail;
    private String discussionOn;
    private String subjectID;
    private String title;
    private String userID;
    private String displayName;

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

    public String getDiscussionOn() {
        return discussionOn;
    }

    public void setDiscussionOn(String discussionOn) {
        this.discussionOn = discussionOn;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "DiscussionBean{" +
                "_id='" + _id + '\'' +
                ", detail=" + detail +
                ", discussionOn='" + discussionOn + '\'' +
                ", subjectID='" + subjectID + '\'' +
                ", title='" + title + '\'' +
                ", userID='" + userID + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
