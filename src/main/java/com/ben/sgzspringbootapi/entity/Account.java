package com.ben.sgzspringbootapi.entity;

public class Account {
    private Integer userID;
    private String accountID;
    private String accountName;
    private double totalBonus;
    private double availBonus;
    private Integer withdrawPercent;
    private Integer status;
    private double totalOrderAmt;

    public Account(){

    }

    public Account(Integer userID, String accountID, String accountName, double totalBonus, double availBonus, Integer withdrawPercent, Integer status, double totalOrderAmt) {
        this.userID = userID;
        this.accountID = accountID;
        this.accountName = accountName;
        this.totalBonus = totalBonus;
        this.availBonus = availBonus;
        this.withdrawPercent = withdrawPercent;
        this.status = status;
        this.totalOrderAmt = totalOrderAmt;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(double totalBonus) {
        this.totalBonus = totalBonus;
    }

    public double getAvailBonus() {
        return availBonus;
    }

    public void setAvailBonus(double availBonus) {
        this.availBonus = availBonus;
    }

    public Integer getWithdrawPercent() {
        return withdrawPercent;
    }

    public void setWithdrawPercent(Integer withdrawPercent) {
        this.withdrawPercent = withdrawPercent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getTotalOrderAmt() {
        return totalOrderAmt;
    }

    public void setTotalOrderAmt(double totalOrderAmt) {
        this.totalOrderAmt = totalOrderAmt;
    }
}
