package com.jwade.ui;

public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(){
        io = new UserIOImpl();
    }

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    //print main Menu
    public void printMenu(){
        mainMenuBanner();
        io.print("1. Display orders");
        io.print("2. Add an order");
        io.print("3. Edit an order");
        io.print("4. Remove an order");
        io.print("5. Export all data");
        io.print("6. Exit");

    }

    public void displayErrorMessage(String message){
        io.print(message + '\n');
        io.readString("Please hit enter to continue");
    }

    public void mainMenuBanner(){
        io.print("======Main Menu======");
    }

    public void allOrdersBanner(){
        io.print("======All Orders======");
    }

    public void editOrderBanner(){
        io.print("======Edit Order======");
    }

    public void editOrderSuccessful(){
        io.print("Order Revised!");
    }

    public void orderAddedSuccessful(){
        io.print("Order Added!");
    }

    public void orderRemovedSuccessful(){
        io.print("Order Removed!");
    }

    public void displayExitMessage(){
        io.print("\nGoodbye");
    }

    public void displayUnknownCommand(){
        io.print("Invalid input. Please input number 1-6.");
    }
}
