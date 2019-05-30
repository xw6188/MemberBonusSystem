package com.ben.sgzspringbootapi.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UserOrder {
    private String orderId;
    private Integer userId;
    private double amount;
    private Integer discount;
    private Timestamp createTime;
    //订单基础金额
    private BigDecimal rawAmount;

    public UserOrder(String orderId, Integer userId, double amount, Integer discount, Timestamp createTime, BigDecimal rawAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.discount = discount;
        this.createTime = createTime;
        this.rawAmount = rawAmount;
    }

    public BigDecimal getRawAmount() {
        return rawAmount;
    }

    public void setRawAmount(BigDecimal rawAmount) {
        this.rawAmount = rawAmount;
    }

    public UserOrder(){

    }

    public UserOrder(String orderId, Integer userId, double amount, Integer discount, Timestamp createTime){
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.discount = discount;
        this.createTime = createTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
