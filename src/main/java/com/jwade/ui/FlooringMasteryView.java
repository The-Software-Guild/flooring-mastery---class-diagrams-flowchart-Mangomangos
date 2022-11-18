package com.jwade.ui;

import com.jwade.dto.Order;
import com.jwade.dto.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public void printEditMenu(){
        io.print("1. Edit customer name");
        io.print("2. Edit state");
        io.print("3. Edit product type");
        io.print("4. Edit flooring area");
        io.print("5. Exit");
    }

    public String getMenuSelection(){
        return  io.readString("Please select an operation.");
    }

    public Integer getProductSelection(int min, int max){
        return io.readInt("Please select a Product Type");
    }



    public ArrayList<String> printAllProducts(List<Product> productList){
        allProductsBanner();
        int j=1;
        ArrayList<String> list = new ArrayList<>();
        list.add(0, "null");
        for (Product i: productList){
            io.print(j + ". " + i.toString());
            list.add(j, i.getProductType());
            j++;
        }
        return list;
    }

    public ArrayList<Integer> printAllOrders(List<Order> orderList) {
        allOrdersBanner();
        int j=1;
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0, 0);
        for (Order i: orderList){
            io.print(j + ". " + i.toString() +"\n");
            list.add(j, i.getOrderNumber());
            j++;
        }
        return  list;

    }

    public String confirmationSaveOrder(Order order){
        return  io.readString("Confirm save order Y/N \n" + order.toString());
    }
    public String confirmationRemoveOrder(Order order){
        return  io.readString("Confirm remove order Y/N: \n" + order.toString());
    }

    public Integer getOrderNumber(){
        return io.readInt("Please enter an Order Number");
    }

    public String getOrderDate(){
        return io.readString("Please enter an Order Date");
    }
    public String getOrderName(){
        return io.readString("What is the customer's name?");
    }

    public BigDecimal getOrderArea(){
        return io.readBigDecimal("What is the area of the floor?");
    }

    public String getOrderState (){
        return  io.readString("What is the state for billing?");
    }

    public String editCustomerName(Order editOrder){
        return io.readString("Enter customer name (" + editOrder.getCustomerName() + "):");
    }

    public String editCustomerState (Order editOrder){
        return io.readString("Enter state (" + editOrder.getState() + ")");
    }

    public String editProductType (Order editOrder) {
        return io.readString("What product type would you like? (" + editOrder.getProductType() + ")");
    }

    public String editArea (Order editOrder) {
        return io.readString("Enter an area (" + editOrder.getArea() + ")");
    }

    public void displayErrorMessage(String message){
        io.print(message + '\n');
        io.readString("Please hit enter to continue");
    }

    public void mainMenuBanner(){
        io.print("\n======Main Menu======");
    }

    public void allOrdersBanner(){
        io.print("\n======All Orders======");
    }

    public void allProductsBanner(){io.print("=====All Products=====");}

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
        io.print("Invalid input.");
    }

    public void exportOrdersSuccessful(){
        io.print("Orders Exported!");
    }
}
