package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */
public class AccountInfo implements Serializable {
    /**
     * "account": {
     * "enduserId": 3,
     * "balance": 0,
     * "point": 0,
     * "guaranteeDeposit": 0,
     * "ridingMileage": 0,
     * "savedCarbonEmission": 0,
     * "sportAchievement": 0
     * }
     */
    private String enduserId;
    private String balance;
    private String point;
    private String guaranteeDeposit;
    private String ridingMileage;
    private String savedCarbonEmission;
    private String sportAchievement;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEnduserId() {
        return enduserId;
    }

    public void setEnduserId(String enduserId) {
        this.enduserId = enduserId;
    }

    public String getGuaranteeDeposit() {
        return guaranteeDeposit;
    }

    public void setGuaranteeDeposit(String guaranteeDeposit) {
        this.guaranteeDeposit = guaranteeDeposit;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRidingMileage() {
        return ridingMileage;
    }

    public void setRidingMileage(String ridingMileage) {
        this.ridingMileage = ridingMileage;
    }

    public String getSavedCarbonEmission() {
        return savedCarbonEmission;
    }

    public void setSavedCarbonEmission(String savedCarbonEmission) {
        this.savedCarbonEmission = savedCarbonEmission;
    }

    public String getSportAchievement() {
        return sportAchievement;
    }

    public void setSportAchievement(String sportAchievement) {
        this.sportAchievement = sportAchievement;
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance='" + balance + '\'' +
                ", enduserId='" + enduserId + '\'' +
                ", guaranteeDeposit='" + guaranteeDeposit + '\'' +
                ", point='" + point + '\'' +
                ", ridingMileage='" + ridingMileage + '\'' +
                ", savedCarbonEmission='" + savedCarbonEmission + '\'' +
                ", sportAchievement='" + sportAchievement + '\'' +
                '}';
    }
}

