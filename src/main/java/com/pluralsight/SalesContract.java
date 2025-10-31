package com.pluralsight;

import com.pluralsight.dealership.Vehicle;

public class SalesContract extends Contract {
    private double salesTaxAmount;
    private final double recordingFee = 100;
    private double processingFee;
    private boolean isFinanced;

    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicleSold, boolean isFinanced) {
        super(date, customerName, customerEmail, vehicleSold);
        this.salesTaxAmount = getSalesTaxAmount();
        this.processingFee = getProcessingFee();
        this.isFinanced = isFinanced;
        super.setMonthlyPayment(getMonthlyPayment());
        super.setTotalPrice(getTotalPrice());
    }

    @Override
    public double getTotalPrice() {
        return getVehicleSold().getPrice() + salesTaxAmount + recordingFee + processingFee;
    }

    @Override
    public double getMonthlyPayment() {
        int numberOfPayments = 0;
        double interestRate = 0;
        if (isFinanced) {
            if (getVehicleSold().getPrice() >= 10000) {
                numberOfPayments = 48;
                interestRate = 4.25 / 1200;
            } else {
                numberOfPayments = 24;
                interestRate = 5.25 / 1200;
            }

            double monthlyPayment = getTotalPrice() * (interestRate * Math.pow(1 + interestRate, numberOfPayments)) / (Math.pow(1 + interestRate, numberOfPayments) - 1);
            monthlyPayment = Math.round(monthlyPayment * 100);
            monthlyPayment /= 100;
            return monthlyPayment;
        } else {
            return 0.0;
        }
    }
    public double getSalesTaxAmount(){
        return getVehicleSold().getPrice()*0.05;
    }
    public double getProcessingFee(){
        double vehiclePrice = getVehicleSold().getPrice();
        if(vehiclePrice>=10000){
            return 495;
        }
        else return 295;
    }
}
