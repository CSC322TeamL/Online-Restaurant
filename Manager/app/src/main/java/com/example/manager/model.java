package com.example.manager;

public class model {
    private String fromid, toid, subject, context,caseid;

    public model(String fromid, String toid, String subject, String context,String caseid) {
        this.caseid = caseid;
        this.fromid = fromid;
        this.toid = toid;
        this.subject = subject;
        this.context = context;
    }

    public String getCaseid(){
        return caseid;
    }
    public void setCaseid(){
        this.caseid = caseid;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
