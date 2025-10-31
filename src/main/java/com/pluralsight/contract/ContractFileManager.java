package com.pluralsight.contract;

import com.pluralsight.dealership.Vehicle;

import java.io.*;
import java.util.ArrayList;

public class ContractFileManager {
    public ArrayList<Contract> getContracts(){
        ArrayList<Contract> contracts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("contracts.csv"))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\\|");
                String date = fields[1];
                String name = fields[2];
                String email = fields[3];
                Vehicle vehicle = new Vehicle(Integer.parseInt(fields[4]),
                        Integer.parseInt(fields[5]),
                        fields[6],
                        fields[7],
                        fields[8],
                        fields[9],
                        Integer.parseInt(fields[10]),
                        Double.parseDouble(fields[11]));
                if (fields[0].equalsIgnoreCase("SALE")){

                    double totalPrice = Double.parseDouble(fields[11]);
                    double salesTax = Double.parseDouble(fields[12]);
                    double recordingFee = Double.parseDouble(fields[13]);
                    double processingFee = Double.parseDouble(fields[14]);
                    boolean financeOption = !fields[16].equalsIgnoreCase("no");
                    double monthlyPay = Double.parseDouble(fields[17]);
                    contracts.add(new SalesContract(date,name,email,vehicle,totalPrice, salesTax,recordingFee,processingFee,financeOption,monthlyPay));

                } else if (fields[0].equalsIgnoreCase("LEASE")) {

                    double expectedEndingValue = Double.parseDouble(fields[12]);
                    double leaseFee = Double.parseDouble(fields[13]);
                    double totalPrice = Double.parseDouble(fields[14]);
                    double monthlyPay = Double.parseDouble(fields[15]);
                    contracts.add(new LeaseContract(date,name,email,vehicle,totalPrice,monthlyPay,expectedEndingValue,leaseFee));

                }else {
                    System.out.println("Line " + (lineNumber+1)+ " is in wrong format");
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        return contracts;
    }
    public void saveContracts(ArrayList<Contract> contracts){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("contracts.csv"))) {
            for(Contract contract:contracts) {
                if (contract instanceof SalesContract){
                    bw.write("SALE|"+contract.getDate()
                            +"|"+contract.getCustomerName()
                            +"|"+contract.getCustomerEmail()
                            +"|"+contract.getVehicleSold()
                            +"|"+((SalesContract) contract).getSalesTaxAmount()
                            +"|"+((SalesContract) contract).getRecordingFee()
                            +"|"+((SalesContract) contract).getProcessingFee()
                            +"|"+contract.getTotalPrice()
                            +"|"+((SalesContract) contract).getFinanceOption()
                            +"|"+contract.getMonthlyPayment());
                } else if (contract instanceof LeaseContract) {
                    bw.write("LEASE|"+contract.getDate()
                            +"|"+contract.getCustomerName()
                            +"|"+contract.getCustomerEmail()
                            +"|"+contract.getVehicleSold()
                            +"|"+((LeaseContract) contract).getExpectedEndingValue()
                            +"|"+((LeaseContract) contract).getLeaseFee()
                            +"|"+contract.getTotalPrice()
                            +"|"+contract.getMonthlyPayment());
                }
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
