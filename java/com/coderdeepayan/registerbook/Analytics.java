package com.coderdeepayan.registerbook;

public class Analytics {
    private String creditorName, totalAmount, givenBackAmount, remainingAmount, givenPercentage;


    public Analytics(String creditorName, String totalAmount, String givenBackAmount,
                     String remainingAmount, String givenPercentage) {
        this.creditorName = creditorName;
        this.totalAmount = totalAmount;
        this.givenBackAmount = givenBackAmount;
        this.remainingAmount = remainingAmount;
        this.givenPercentage = givenPercentage;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getGivenBackAmount() {
        return givenBackAmount;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public String getGivenPercentage() {
        return givenPercentage;
    }
}
