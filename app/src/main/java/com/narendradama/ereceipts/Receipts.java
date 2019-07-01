package com.narendradama.ereceipts;

public class Receipts{

    public String receiptNumber;
    public String date;
    public String place;
    public String amount;
    public String savings;
    
    public Receipts(String receiptNumber, String date, String place, String  amount, String savings) {
        this.receiptNumber = receiptNumber;
        this.date = date;
        this.place = place;
        this.amount = amount;
        this.savings = savings;
    }
}
