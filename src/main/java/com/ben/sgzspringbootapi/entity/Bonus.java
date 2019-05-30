package com.ben.sgzspringbootapi.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Bonus {
    private Integer bonusID;
    private Integer userID;
    private Integer relatedUserID;
    private String bonusType;
    private double bonusAmt;
    private String bonusdetail;
    private Date bonusDate;

    public Bonus(){

    }

    public Bonus(Integer bonusID, Integer userID, Integer relatedUserID, String bonusType, double bonusAmt, String bonusdetail, Date bonusDate){
        this.bonusID = bonusID;
        this.userID = userID;
        this.relatedUserID = relatedUserID;
        this.bonusType = bonusType;
        this.bonusAmt = bonusAmt;
        this.bonusdetail = bonusdetail;
        this.bonusDate = bonusDate;
    }

    public Integer getBonusID() {
        return bonusID;
    }

    public void setBonusID(Integer bonusID) {
        this.bonusID = bonusID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getRelatedUserID() {
        return relatedUserID;
    }

    public String getBonusdetail() {
        return bonusdetail;
    }

    public void setBonusdetail(String bonusdetail) {
        this.bonusdetail = bonusdetail;
    }

    public void setRelatedUserID(Integer relatedUserID) {
        this.relatedUserID = relatedUserID;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    public double getBonusAmt() {
        return bonusAmt;
    }

    public void setBonusAmt(double bonusAmt) {
        this.bonusAmt = bonusAmt;
    }

    public Date getBonusDate() {
        return bonusDate;
    }

    public void setBonusDate(Date bonusDate) {
        this.bonusDate = bonusDate;
    }
}
