package com.example.chef;

public class model {
    private String custid, dishs, ordernote,ordertotal, orderid, btn_finishorder, ordernumber;

    public model(String custid, String dishs, String ordernote, String ordertotal, String orderid, String ordernumber) {
        this.custid = custid;
        this.dishs = dishs;
        this.ordernote = ordernote;
        this.ordertotal = ordertotal;
        this.orderid = orderid;
        this.ordernumber = ordernumber;
    }




    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getDishs() {
        return dishs;
    }

    public void setDishs(String dishs) {
        this.dishs = dishs;
    }

    public String getOrdernote() {
        return ordernote;
    }

    public void setOrdernote(String ordernote) {
        this.ordernote = ordernote;
    }

    public String getOrdertotal() {
        return ordertotal;
    }

    public void setOrdertotal(String ordertotal) {
        this.ordertotal = ordertotal;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }


}
