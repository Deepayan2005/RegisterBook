package com.coderdeepayan.registerbook;

public class DataRecord {
    private String creditor,id,purpose,dateView;
    private double amount;
    private int type;
    public static final int RECORD = 256;
    public static final int UTILITIES = 459;

    public DataRecord(String creditor, String id, String purpose, String dateView, double amount, int type) {
        this.creditor = creditor;
        this.id = id;
        this.purpose = purpose;
        this.dateView = dateView;
        this.amount = amount;
        this.type = type;
    }

    public DataRecord(int type) {
        this.type = type;
    }

    public String getCreditor() {
        return creditor;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getDateView() {
        return dateView;
    }

    public double getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
