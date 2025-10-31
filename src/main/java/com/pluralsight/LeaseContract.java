package com.pluralsight;

import com.pluralsight.dealership.Vehicle;

public class LeaseContract extends Contract{
    private double expectedEndingValue;
    private double leaseFee;

    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);
        this.leaseFee = getLeaseFee();
        this.expectedEndingValue = getExpectedEndingValue();
    }

    @Override
    public double getTotalPrice() {
        return (getVehicleSold().getPrice() - expectedEndingValue) + leaseFee;
    }

    @Override
    public double getMonthlyPayment() {
        int numberOfPayments = 36;
        double interestRate = 4.0 / 1200;
        double monthlyPayment = getTotalPrice() * (interestRate * Math.pow(1 + interestRate, numberOfPayments)) / (Math.pow(1 + interestRate, numberOfPayments) - 1);
        monthlyPayment = Math.round(monthlyPayment * 100);
        monthlyPayment /= 100;
        return monthlyPayment;
    }
    public double getLeaseFee(){
        return getVehicleSold().getPrice()*0.07;
    }

    public double getExpectedEndingValue() {
        return getVehicleSold().getPrice()*0.5;
    }
}
