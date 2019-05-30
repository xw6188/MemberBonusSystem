package com.ben.sgzspringbootapi.entity;

import java.security.SecureRandom;

public class User {
    private Integer userId;
    private Integer treePosition;
    private Integer treeLeft;
    private Integer treeRight;
    private double calamount;
    private Integer teampplNum;
    private double teamtotal;
    private double lastteamamt;
    private double lastteambonus;
    private String mobile;
    private String userName;
    private String nickName;
    private Integer parent;
    private Integer nominator;
    private Integer level;
    private Integer upgrade;
    private Integer grade;
    private Integer fund;

    public User(){
    }

    public User(Integer userId, Integer treePosition, Integer treeLeft, Integer treeRight, double calamount, Integer teampplNum, double teamtotal, double lastteamamt, double lastteambonus, String mobile, String userName, String nickName, Integer parent, Integer nominator, Integer level, Integer upgrade, Integer grade, Integer fund) {
        this.userId = userId;
        this.treePosition = treePosition;
        this.treeLeft = treeLeft;
        this.treeRight = treeRight;
        this.calamount = calamount;
        this.teampplNum = teampplNum;
        this.teamtotal = teamtotal;
        this.lastteamamt = lastteamamt;
        this.lastteambonus = lastteambonus;
        this.mobile = mobile;
        this.userName = userName;
        this.nickName = nickName;
        this.parent = parent;
        this.nominator = nominator;
        this.level = level;
        this.upgrade = upgrade;
        this.grade = grade;
        this.fund = fund;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getFund() {
        return fund;
    }

    public void setFund(Integer fund) {
        this.fund = fund;
    }

    public Integer getTeampplNum() {
        return teampplNum;
    }

    public void setTeampplNum(Integer teampplNum) {
        this.teampplNum = teampplNum;
    }

    public double getTeamtotal() {
        return teamtotal;
    }

    public void setTeamtotal(double teamtotal) {
        this.teamtotal = teamtotal;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTreePosition() {
        return treePosition;
    }

    public Integer getTreeLeft() {
        return treeLeft;
    }

    public Integer getTreeRight() {
        return treeRight;
    }

    public void setTreeLeft(Integer treeLeft) {
        this.treeLeft = treeLeft;
    }

    public void setTreeRight(Integer treeRight) {
        this.treeRight = treeRight;
    }

    public double getCalamount() {
        return calamount;
    }

    public String getMobile(){ return mobile;}

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public Integer getParent() {
        return parent;
    }

    public Integer getNominator() {
        return nominator;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getUpgrade() {
        return upgrade;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTreePosition(Integer treePosition) {
        this.treePosition = treePosition;
    }

    public void setCalamount(double calamount) {
        this.calamount = calamount;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public void setNominator(Integer nominator) {
        this.nominator = nominator;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setUpgrade(Integer upgrade) {
        this.upgrade = upgrade;
    }

    public double getLastteamamt() {
        return lastteamamt;
    }

    public void setLastteamamt(double lastteamamt) {
        this.lastteamamt = lastteamamt;
    }

    public double getLastteambonus() {
        return lastteambonus;
    }

    public void setLastteambonus(double lastteambonus) {
        this.lastteambonus = lastteambonus;
    }

    @Override
    public String toString() {
        return this.userId.toString()+this.treePosition.toString()+this.mobile+this.userName+this.nickName+this.parent.toString();
    }
}
