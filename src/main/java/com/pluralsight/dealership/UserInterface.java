package com.pluralsight.dealership;

import com.pluralsight.contract.Contract;
import com.pluralsight.contract.ContractFileManager;
import com.pluralsight.contract.LeaseContract;
import com.pluralsight.contract.SalesContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserInterface {

    private Dealership dealership;
    private Scanner scanner;

    public UserInterface() {
        scanner = new Scanner(System.in);
    }

    public void display() {
        init();
        boolean quit = false;
        while (!quit) {
            System.out.println("---------- Menu ----------");
            System.out.println("1. Get vehicles by price");
            System.out.println("2. Get vehicles by make and model");
            System.out.println("3. Get vehicles by year");
            System.out.println("4. Get vehicles by color");
            System.out.println("5. Get vehicles by mileage");
            System.out.println("6. Get vehicles by type");
            System.out.println("7. Get all vehicles");
            System.out.println("8. Add vehicle");
            System.out.println("9. Remove vehicle");
            System.out.println("10. Sell/Lease contracts");
            System.out.println("99. Quit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    processGetByPriceRequest();
                    break;
                case "2":
                    processGetByMakeModelRequest();
                    break;
                case "3":
                    processGetByYearRequest();
                    break;
                case "4":
                    processGetByColorRequest();
                    break;
                case "5":
                    processGetByMileageRequest();
                    break;
                case "6":
                    processGetByVehicleTypeRequest();
                    break;
                case "7":
                    processGetAllVehiclesRequest();
                    break;
                case "8":
                    processAddVehicleRequest();
                    break;
                case "9":
                    processRemoveVehicleRequest();
                    break;
                case "10":

                    break;
                case "99":
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void processGoToLeaseSellContractMenu(){
        System.out.println("Going to lease/sell contracts");
        ContractFileManager contractFileManager = new ContractFileManager();
        ArrayList<Contract> contracts = contractFileManager.getContracts();
        boolean quit = false;
        while (!quit) {
            System.out.println("---------- Menu ----------");
            System.out.println("1. View all contracts");
            System.out.println("2. Create a new contract");
            System.out.println("3. Delete a contract");
            System.out.println("4. Quit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    viewAllContracts(contracts);
                    break;
                case "2":
                    Contract contract = createContract();
                    if(contract!=null) {
                        contracts.add(contract);
                    }else {
                        System.out.println("Please try again.");
                    }
                    break;
                case "3":
                    contracts = processDeletionContract(contracts);
                    break;
                case "4":
                    quit = true;
                    break;
            }
        }
    }
    public ArrayList<Contract> processDeletionContract(ArrayList<Contract> contracts){
        System.out.println("What is the vin of vehicle of contract?");
        int vin = scanner.nextInt();
        scanner.nextLine();
        return contracts.stream().filter(contract -> contract.getVehicleSold().getVin() == vin).collect(Collectors.toCollection(ArrayList::new));
    }
    public Contract createContract() {
        Contract contract = null;
        Vehicle vehicle = processGetByVin();
        if (vehicle != null) {
            System.out.println("What is the type of contract? (lease/sell)");
            String choice = scanner.nextLine().trim();

            //Get common parameters for all contracts
            System.out.println("Enter Contract Date (YYYY-MM-DD):");
            String date = scanner.nextLine();
            System.out.println("Enter Customer Name:");
            String customerName = scanner.nextLine();
            System.out.println("Enter Customer Email:");
            String customerEmail = scanner.nextLine();
            System.out.println("Enter Total Price (Capitalized Cost or Sale Price):");
            double totalPrice = Double.parseDouble(scanner.nextLine());
            System.out.println("Enter Monthly Payment:");
            double monthlyPayment = Double.parseDouble(scanner.nextLine());

            if (choice.equalsIgnoreCase("lease")) {
                System.out.println("Enter Expected Ending Value (Residual Value):");
                double expectedEndingValue = Double.parseDouble(scanner.nextLine());
                System.out.println("Enter Lease Fee:");
                double leaseFee = Double.parseDouble(scanner.nextLine());

                contract = new LeaseContract(
                        date, customerName, customerEmail, vehicle, totalPrice, monthlyPayment,
                        expectedEndingValue, leaseFee
                );
                System.out.println("\nLease Contract successfully created!");

            } else if (choice.equalsIgnoreCase("sell")) {

                // Get parameters specific to SalesContract
                System.out.println("Enter Sales Tax Amount:");
                double salesTaxAmount = Double.parseDouble(scanner.nextLine());
                System.out.println("Enter Recording Fee:");
                double recordingFee = Double.parseDouble(scanner.nextLine());
                System.out.println("Enter Processing Fee:");
                double processingFee = Double.parseDouble(scanner.nextLine());
                System.out.println("Is the sale financed? (true/false):");
                boolean isFinanced = Boolean.parseBoolean(scanner.nextLine());

                // Instantiate the SalesContract object
                contract = new SalesContract(
                        date, customerName, customerEmail, vehicle, totalPrice, salesTaxAmount, recordingFee, processingFee, isFinanced, monthlyPayment);
                System.out.println("\nSales Contract successfully created!");
            } else {
                System.out.println("Invalid input");
            }
        }else {
            System.out.println("Fail! There is no this car");
            return null;
        }
        return contract;
    }

    public void viewAllContracts(ArrayList<Contract> contracts){
        contracts.forEach(System.out::println);
    }
    public Vehicle processGetByVin(){
        System.out.println("What is VIN of car?");
        int vin = scanner.nextInt();
        scanner.nextLine();
        return dealership.getVehicleByVin(vin);
    }
    public void processGetByPriceRequest() {
        System.out.print("Enter minimum price: ");
        double min = scanner.nextDouble();
        System.out.print("Enter maximum price: ");
        double max = scanner.nextDouble();
        List<Vehicle> vehicles = dealership.getVehiclesByPrice(min, max);
        displayVehicles(vehicles);
    }

    public void processGetByMakeModelRequest() {
        System.out.print("Enter make: ");
        String make = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByMakeModel(make, model);
        displayVehicles(vehicles);
    }

    public void processGetByYearRequest() {
        System.out.print("Enter minimum year: ");
        int min = scanner.nextInt();
        System.out.print("Enter maximum year: ");
        int max = scanner.nextInt();
        List<Vehicle> vehicles = dealership.getVehiclesByYear(min, max);
        displayVehicles(vehicles);
    }

    public void processGetByColorRequest() {
        System.out.print("Enter color: ");
        String color = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByColor(color);
        displayVehicles(vehicles);
    }

    public void processGetByMileageRequest() {
        System.out.print("Enter minimum mileage: ");
        int min = scanner.nextInt();
        System.out.print("Enter maximum mileage: ");
        int max = scanner.nextInt();
        List<Vehicle> vehicles = dealership.getVehiclesByMileage(min, max);
        displayVehicles(vehicles);
    }

    public void processGetByVehicleTypeRequest() {
        System.out.print("Enter vehicle type: ");
        String vehicleType = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByType(vehicleType);
        displayVehicles(vehicles);
    }

    public void processGetAllVehiclesRequest() {
        List<Vehicle> vehicles = dealership.getAllVehicles();
        displayVehicles(vehicles);
    }

    public void processAddVehicleRequest() {
        System.out.print("Enter vehicle vin: ");
        int vin = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter vehicle make: ");
        String make = scanner.nextLine();

        System.out.print("Enter vehicle model: ");
        String model = scanner.nextLine();

        System.out.print("Enter vehicle year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter vehicle price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter vehicle color: ");
        String color = scanner.nextLine();

        System.out.print("Enter vehicle mileage: ");
        int mileage = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter vehicle type (Car, Truck, SUV, Motorcycle): ");
        String type = scanner.nextLine();

        Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price);

        dealership.addVehicle(vehicle);
        System.out.println("Vehicle added successfully!");
        DealershipFileManager manager = new DealershipFileManager();
        manager.saveDealership(dealership);
    }

    public void processRemoveVehicleRequest() {
        System.out.print("Enter the VIN of the vehicle you wish to remove: ");
        int vin = scanner.nextInt();

        boolean vehicleRemoved = false;
        for (Vehicle vehicle : dealership.getAllVehicles()) {
            if (vehicle.getVin() == vin) {
                dealership.removeVehicle(vehicle);
                System.out.println("Vehicle removed successfully!");
                vehicleRemoved = true;
                break;
            }
        }

        if (!vehicleRemoved) {
            System.out.println("Vehicle not found. Please try again.");
            return;
        }

        DealershipFileManager manager = new DealershipFileManager();
        manager.saveDealership(dealership);
    }

    private void init() {
        DealershipFileManager manager = new DealershipFileManager();
        dealership = manager.getDealership();
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.toString());
        }
    }

}
