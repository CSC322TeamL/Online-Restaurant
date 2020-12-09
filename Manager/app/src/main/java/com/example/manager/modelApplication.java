package com.example.manager;

public class modelApplication {
    private String email, context, caseid;

    modelApplication(String email, String context, String caseid){
        this.context = context;
        this.email = email;
        this.caseid = caseid;
    }

    public String getCaseid() {
        return caseid;
    }

    public void setCaseid(String caseid) {
        this.caseid = caseid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
