package com.example.simplerestaurant.beans;

import java.util.ArrayList;

public class DiscussionDetailBean {
    private String context;
    private String createDate;
    private String status;
    private int tabooCount;
    private ArrayList<String> tabooIDs;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTabooCount() {
        return tabooCount;
    }

    public void setTabooCount(int tabooCount) {
        this.tabooCount = tabooCount;
    }

    public ArrayList<String> getTabooIDs() {
        return tabooIDs;
    }

    public void setTabooIDs(ArrayList<String> tabooIDs) {
        this.tabooIDs = tabooIDs;
    }

    @Override
    public String toString() {
        return "DiscussionDetailBean{" +
                "context='" + context + '\'' +
                ", createDate='" + createDate + '\'' +
                ", status='" + status + '\'' +
                ", tabooCount=" + tabooCount +
                ", tabooIDs=" + tabooIDs +
                '}';
    }
}
