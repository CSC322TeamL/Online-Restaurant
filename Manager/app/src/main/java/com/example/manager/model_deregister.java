package com.example.manager;

public class model_deregister {
    private String userid, spent,balance,warning, backid;

    public model_deregister(String userid, String spent, String balance, String warning, String backid){
        this.balance = balance;
        this.spent = spent;
        this.userid = userid;
        this.warning = warning;
        this.backid = backid;
    }

    public String getBackid() {
        return backid;
    }

    public void setBackid(String backid) {
        this.backid = backid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSpent() {
        return spent;
    }

    public void setSpent(String spent) {
        this.spent = spent;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}
