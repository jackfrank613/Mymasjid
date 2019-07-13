package com.tech_sim.mymasjidapp.model;

public class History {


    private String payment;
    private String create;
    private String amount;
    private String child_name;
    public History(){}
    public History(String payment,String create,String amount,String child_name)
    {
        this.amount=amount;
        this.payment=payment;
        this.create=create;
        this.child_name=child_name;
    }
    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }
}
