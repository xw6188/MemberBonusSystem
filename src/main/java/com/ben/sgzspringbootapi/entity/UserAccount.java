package com.ben.sgzspringbootapi.entity;

import java.sql.Timestamp;

public class UserAccount extends User {

    private double totalBonus;
    private double availBonus;
    private double totalOrderAmt;
    private Timestamp createTime;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public double getTotalOrderAmt() {
        return totalOrderAmt;
    }

    public void setTotalOrderAmt(double totalOrderAmt) {
        this.totalOrderAmt = totalOrderAmt;
    }

    public UserAccount(double totalBonus, double availBonus, double totalOrderAmt) {
        this.totalBonus = totalBonus;
        this.availBonus = availBonus;
        this.totalOrderAmt = totalOrderAmt;
    }

    public UserAccount(Integer userId, Integer treePosition, Integer treeLeft, Integer treeRight, double calamount, Integer teampplNum, double teamtotal, double lastteamamt, double lastteambonus, String mobile, String userName, String nickName, Integer parent, Integer nominator, Integer level, Integer upgrade, Integer grade, Integer fund, double totalBonus, double availBonus, double totalOrderAmt, Timestamp createTime) {
        super(userId, treePosition, treeLeft, treeRight, calamount, teampplNum, teamtotal, lastteamamt, lastteambonus, mobile, userName, nickName, parent, nominator, level, upgrade, grade, fund);
        this.totalBonus = totalBonus;
        this.availBonus = availBonus;
        this.totalOrderAmt = totalOrderAmt;
        this.createTime = createTime;
    }
}
