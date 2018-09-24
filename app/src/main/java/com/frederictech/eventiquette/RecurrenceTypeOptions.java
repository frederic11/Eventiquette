package com.frederictech.eventiquette;

public class RecurrenceTypeOptions {
    public final String description;
    public final String amount;

    public RecurrenceTypeOptions(String amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return "DonationOption{" +
                "description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
