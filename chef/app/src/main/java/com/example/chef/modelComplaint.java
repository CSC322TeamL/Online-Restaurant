package com.example.chef;

public class modelComplaint {
    private String toid, subject, context,caseid;

    public modelComplaint(String toid, String subject, String context, String caseid){

        this.toid = toid;
        this.caseid = caseid;
        this.context = context;
        this.subject = subject;
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

    public String getCaseid() {
        return caseid;
    }

    public void setCaseid(String caseid) {
        this.caseid = caseid;
    }
}
