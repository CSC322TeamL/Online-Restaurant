package com.example.simplerestaurant.beans;

public class UserBasicInfoBean {
    private String _id = "";
    private float balance = 0;
    private UserPersonalInfoBean basicInfo = new UserPersonalInfoBean();
    private ContactBean contact = new ContactBean();
    private String displayName = "";
    private float spent = 0;
    private String userID = "";
    private String userRole = "";
    private int warnings = 0;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public UserPersonalInfoBean getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(UserPersonalInfoBean basicInfo) {
        this.basicInfo = basicInfo;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public float getSpent() {
        return spent;
    }

    public void setSpent(float spent) {
        this.spent = spent;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    @Override
    public String toString() {
        return "UserBasicInfoBean{" +
                "_id='" + _id + '\'' +
                ", balance=" + balance +
                ", basicInfo=" + basicInfo +
                ", contact=" + contact +
                ", displayName='" + displayName + '\'' +
                ", spent=" + spent +
                ", userID='" + userID + '\'' +
                ", userRole='" + userRole + '\'' +
                ", warnings=" + warnings +
                '}';
    }
}
