package com.ben.sgzspringbootapi.entity;

import java.sql.Timestamp;

public class Withdraw {
    private Integer withdrawID;
    private Integer userID;
    private String mobile;
    private String accountName;
    private String accountType;
    private String accountBank;
    private String accountID;
    private String withdrawDetail;
    private double amount;
    private String status;
    private Timestamp createTime;

    public Withdraw(){

    }

    public Withdraw(Integer withdrawID, Integer userID, String mobile, String accountName, String accountType, String accountBank, String accountID, String withdrawDetail, double amount, String status, Timestamp createTime) {
        this.withdrawID = withdrawID;
        this.userID = userID;
        this.mobile = mobile;
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountBank = accountBank;
        this.accountID = accountID;
        this.withdrawDetail = withdrawDetail;
        this.amount = amount;
        this.status = status;
        this.createTime = createTime;
    }

    public Integer getWithdrawID() {
        return withdrawID;
    }

    public void setWithdrawID(Integer withdrawID) {
        this.withdrawID = withdrawID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getWithdrawDetail() {
        return withdrawDetail;
    }

    public void setWithdrawDetail(String withdrawDetail) {
        this.withdrawDetail = withdrawDetail;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


}
