package com.example.manager;

public class model_performance {
    private String userid, complaint, compliment, demote, promote, backid;

    public model_performance(String userid, String complaint, String compliment, String demote, String promote, String backid){
        this.userid = userid;
        this.complaint = complaint;
        this.compliment = compliment;
        this.demote = demote;
        this.promote = promote;
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

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getCompliment() {
        return compliment;
    }

    public void setCompliment(String compliment) {
        this.compliment = compliment;
    }

    public String getDemote() {
        return demote;
    }

    public void setDemote(String demote) {
        this.demote = demote;
    }

    public String getPromote() {
        return promote;
    }

    public void setPromote(String promote) {
        this.promote = promote;
    }
}
