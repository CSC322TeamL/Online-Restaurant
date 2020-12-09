package com.example.manager;

public class model_handledispute {
    String userid, backid, complaint, dispute;

    public model_handledispute(String userid, String backid, String complaint, String dispute){
        this.backid = backid;
        this.userid = userid;
        this.complaint = complaint;
        this.dispute = dispute;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBackid() {
        return backid;
    }

    public void setBackid(String backid) {
        this.backid = backid;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDispute() {
        return dispute;
    }

    public void setDispute(String dispute) {
        this.dispute = dispute;
    }
}
