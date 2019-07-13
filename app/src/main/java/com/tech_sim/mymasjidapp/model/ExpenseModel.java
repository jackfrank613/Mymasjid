package com.tech_sim.mymasjidapp.model;

import com.tech_sim.mymasjidapp.activity.Expense;

public class ExpenseModel {
    private String type;
    private String amount;
    private String detail;
    private String date;



    private String id;
    public ExpenseModel(){}
    public ExpenseModel(String id,String type,String amount,String detail,String date)
    {
        this.type=type;
        this.amount=amount;
        this.detail=detail;
        this.date=date;
        this.id=id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
