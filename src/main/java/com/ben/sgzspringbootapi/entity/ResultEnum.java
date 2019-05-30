package com.ben.sgzspringbootapi.entity;

public enum ResultEnum {
    ERROR(-1, "ERROR"),
    OK(0, "OK"),
    NULLDATA(1, "数据为空"),
    USER_NOT_EXIST(2, "用户不存在"),
    USER_EXISTS(3, "用户已存在"),
    ORDER_NOT_EXIST(4, "订单不存在"),
    ORDER_EXISTS(5, "订单已存在"),
    ACCOUNT_NOT_EXIST(6, "账户不存在"),
    ACCOUNT_EXISTS(7, "账户已存在"),
    EXCCEED_ACCOUNT(8, "超出了可提现金额"),
    SMALL_ACCOUNT(9, "最低提现金额不得少于100元"),
    WITHDRAW_NOT_EXIST(10, "提现记录不存在"),
    LEVEL_NOT_EXIST(11, " 会员级别不存在"),
    ;
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
