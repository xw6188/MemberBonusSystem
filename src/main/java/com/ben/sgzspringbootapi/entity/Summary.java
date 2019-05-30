package com.ben.sgzspringbootapi.entity;

import java.util.Date;

public class Summary {
    private Integer summaryID;
    private Integer userID;
    private Date summaryDate;
    private double ttorder;
    private double ttbonus;
    private double teamttorder;
    private double teamttbonus;
    private double teampercent;
    private String summarydetail;

    public Summary(){

    }

    public Summary(Integer summaryID, Integer userID, Date summaryDate, double ttorder, double ttbonus, double teamttorder, double teamttbonus, double teampercent, String summarydetail) {
        this.summaryID = summaryID;
        this.userID = userID;
        this.summaryDate = summaryDate;
        this.ttorder = ttorder;
        this.ttbonus = ttbonus;
        this.teamttorder = teamttorder;
        this.teamttbonus = teamttbonus;
        this.teampercent = teampercent;
        this.summarydetail = summarydetail;
    }

    public Integer getSummaryID() {
        return summaryID;
    }

    public void setSummaryID(Integer summaryID) {
        this.summaryID = summaryID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Date getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }

    public double getTtorder() {
        return ttorder;
    }

    public void setTtorder(double ttorder) {
        this.ttorder = ttorder;
    }

    public double getTtbonus() {
        return ttbonus;
    }

    public void setTtbonus(double ttbonus) {
        this.ttbonus = ttbonus;
    }

    public double getTeamttorder() {
        return teamttorder;
    }

    public void setTeamttorder(double teamttorder) {
        this.teamttorder = teamttorder;
    }

    public double getTeamttbonus() {
        return teamttbonus;
    }

    public void setTeamttbonus(double teamttbonus) {
        this.teamttbonus = teamttbonus;
    }

    public double getTeampercent() {
        return teampercent;
    }

    public void setTeampercent(double teampercent) {
        this.teampercent = teampercent;
    }

    public String getSummarydetail() {
        return summarydetail;
    }

    public void setSummarydetail(String summarydetail) {
        this.summarydetail = summarydetail;
    }
}
