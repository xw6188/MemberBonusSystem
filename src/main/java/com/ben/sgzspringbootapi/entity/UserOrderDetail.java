package com.ben.sgzspringbootapi.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UserOrderDetail extends UserOrder{

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserOrderDetail(String userName) {
        this.userName = userName;
    }

    public UserOrderDetail(String orderId, Integer userId, double amount, Timestamp createTime, Integer discount, String userName) {
        super(orderId, userId, amount, discount, createTime);
        this.userName = userName;
    }

    public UserOrderDetail(String orderId, Integer userId, double amount, Integer discount, Timestamp createTime, BigDecimal rawAmount, String userName) {
        super(orderId, userId, amount, discount, createTime, rawAmount);
        this.userName = userName;
    }


    public UserOrderDetail(String orderId, Integer userId, double amount, Integer discount, Timestamp createTime, String userName) {
        super(orderId, userId, amount, discount, createTime);
        this.userName = userName;
    }
}
