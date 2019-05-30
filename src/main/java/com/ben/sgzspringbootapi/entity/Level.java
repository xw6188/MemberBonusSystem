package com.ben.sgzspringbootapi.entity;

import java.util.Date;

public class Level {
    private Integer levelID;
    private String leveldesc;
    private double totalamt;
    private double nominate;
    private double duipeng;
    private double dplevel1;
    private double dplevel2;
    private double dplevel3;
    private double discount;
    private double dailylimit;
    private double allowance;
    private String levelType;

    public Level(){
    }

    public Level(Integer levelID, String leveldesc, double totalamt, double nominate, double duipeng, double dplevel1, double dplevel2, double dplevel3, double discount, double dailylimit, double allowance, String levelType) {
        this.levelID = levelID;
        this.leveldesc = leveldesc;
        this.totalamt = totalamt;
        this.nominate = nominate;
        this.duipeng = duipeng;
        this.dplevel1 = dplevel1;
        this.dplevel2 = dplevel2;
        this.dplevel3 = dplevel3;
        this.discount = discount;
        this.dailylimit = dailylimit;
        this.allowance = allowance;
        this.levelType = levelType;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public Integer getLevelID() {
        return levelID;
    }

    public void setLevelID(Integer levelID) {
        this.levelID = levelID;
    }

    public String getLeveldesc() {
        return leveldesc;
    }

    public void setLeveldesc(String leveldesc) {
        this.leveldesc = leveldesc;
    }

    public double getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(double totalamt) {
        this.totalamt = totalamt;
    }

    public double getNominate() {
        return nominate;
    }

    public void setNominate(double nominate) {
        this.nominate = nominate;
    }

    public double getDuipeng() {
        return duipeng;
    }

    public void setDuipeng(double duipeng) {
        this.duipeng = duipeng;
    }

    public double getDplevel1() {
        return dplevel1;
    }

    public void setDplevel1(double dplevel1) {
        this.dplevel1 = dplevel1;
    }

    public double getDplevel2() {
        return dplevel2;
    }

    public void setDplevel2(double dplevel2) {
        this.dplevel2 = dplevel2;
    }

    public double getDplevel3() {
        return dplevel3;
    }

    public void setDplevel3(double dplevel3) {
        this.dplevel3 = dplevel3;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDailylimit() {
        return dailylimit;
    }

    public void setDailylimit(double dailylimit) {
        this.dailylimit = dailylimit;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }
}
