package com.bank.izbank.Job;

public class Driver extends Job {
    private final String name="Driver";
    private final String maxCreditAmount="300000";
    private final String maxCreditInstallment ="40";

    public Driver() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMaxCreditAmount() {
        return maxCreditAmount;
    }

    @Override
    public String getMaxCreditInstallment() {
        return maxCreditInstallment;
    }
}