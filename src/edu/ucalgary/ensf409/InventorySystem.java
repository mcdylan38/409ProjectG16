package edu.ucalgary.ensf409;

/**
 * @version 1.0
 * @since 1.0
 */

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Main Inventory System class.
 * Handles UI
 */
public class InventorySystem {
    private InventoryManager inventoryManager;
    private Scanner scan;
    public String category;
    public String type;
    public int quantity;

    /**
     * Constructor for Inventory System. Creates Scanner object to pass through
     * InventoryManager to DatabaseDriver for MySQL credentials request
     */
    public InventorySystem() {
        scan = new Scanner(System.in);
        inventoryManager = new InventoryManager(scan);
    }

    /**
     * Run starts UI and calls necessary methods that will request user input
     * until correct inputs are received. Closes Scanner object and InventoryManager
     * to close MySQL connection
     */
    public void run() {
        // Loops to check categories, types and quantities
        do {
            this.category = setCategory();
        } while (this.category == null);

        do {
            this.type = setType();
        } while (this.type == null);

        do {
            this.quantity = setQuantity();
        } while (this.quantity == -1);

        if(!inventoryManager.pieceFurniture(category, type, quantity)) {
            // If furniture cannot be created, print error message
            printErrorMessage(category);
        }

        // Release MySQL resources
        this.scan.close();
        this.inventoryManager.close();
    }

    /**
     * Helper method to print error message of furniture cannot be sourced
     *
     * @param category category of requested furniture
     */
    private void printErrorMessage(String category) {
        ArrayList<String> suggestedManufacturers = inventoryManager.getSuggestedManufacturers(category);
        String message = "Order cannot be fulfilled based on current inventory. Suggested manufacturers are ";
        message += suggestedManufacturers.get(0);
		
        if(suggestedManufacturers.size() == 1) {
            // If there is at only one manufacturer, print the name(s)
            System.out.println(message);
            return;
        }

        // Loop to get other manufacturers (excluding the first one retirved previously)
        for(int i = 1; i < suggestedManufacturers.size(); i++) {
            message += ", " + suggestedManufacturers.get(i);
        }
        System.out.println(message + ".");
    }

    /**
     * Helper method to get category from user
     * 
     * @return requested category if category is found, else null
     */
    private String setCategory() {
        try {
            System.out.println("Enter furniture category:");
            String category = scan.nextLine();
            if (inventoryManager.checkCategory(category)) {
                // If category exists, return it
                return category;
            }
            else {
                // If category doesn't exit, return null
                System.out.println("Category not found, please retry.");
                return null;
            }
        } 
	catch (Exception e) {
            // If an error occurs when reading inputs, print error message and stack trace
            System.out.println("Invalid Furniture Category Request");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to get type from user
     * 
     * @return requested type within a validated category if type is found in
     *         database else, null
     */
    private String setType() {
        try {
            System.out.println("Enter furniture type:");
            String type = scan.nextLine();
            if (inventoryManager.checkType(this.category, type)) {
                // If type exists, return it
                return type;
            }
            else {
                // If type doesn't exist, return null
                System.out.println("Type not found, please retry.");
                return null;
            }
        } 
	catch (Exception e) {
            // If an error occurs when reading inputs, print error message and stack trace
            System.out.println("Invalid" + category + "Type Request");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to get quantity from user
     * 
     * @return requested quantity if a real number is provided, else returns -1
     */
    private int setQuantity() {
        try {
            System.out.println("Enter the quantity:");
            int q = scan.nextInt();

            if (q >= 0) {
                // If valid quantity is entered, return it
                return q;
            }
            else {
                // If invalid quantity is entered, return -1
                System.out.println("Integer must be >= 0, please try again.");
                return -1;
            }
        } 
	catch (Exception e) {
            // If an error occurs when reading inputs, output error message and stack trace
            System.out.println("Invalid Furniture Quantity Request");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Main method. Creates a new instance of inventorySystem and calls the run method to
     * initialize the order request and subsequent methods to determine outcome of
     * the request.
     * 
     * @param args command line arguments that are unused for our program
     */
    public static void main(String [] args) {
        InventorySystem inventorySystem = new InventorySystem();
        inventorySystem.run();
    }
}
