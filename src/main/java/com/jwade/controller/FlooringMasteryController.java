package com.jwade.controller;

import com.jwade.dao.FlooringMasteryDaoImpl;
import com.jwade.dao.FlooringMasteryException;
import com.jwade.dao.FlooringMasteryPersistenceException;
import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.dto.Tax;
import com.jwade.service.FlooringMasteryService;
import com.jwade.service.FlooringMasteryServiceImpl;
import com.jwade.ui.FlooringMasteryView;

import java.math.BigDecimal;
import java.util.List;

public class FlooringMasteryController {

    private FlooringMasteryView view;

    private FlooringMasteryServiceImpl service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceImpl service) {
        this.view = view;
        this.service = service;
    }

    public void run (){
        boolean start = true;
        while (start){
            view.printMenu();
            String operation = view.getMenuSelection();
            switch (operation) {
                case "1": // display order
                    listOrders();
                    break;
                case "2": //add an order
                    addOrder();
                    break;
                case "3": //edit an order
                    editOrder();
                    break;
                case "4": // remove an order
                    removeAnOrder();
                    break;
                case "5": // export all data
                    exportData();
                    break;
                case "6": // quit program
                    view.displayExitMessage();
                    start = false;
                    break;
                default:
                    view.displayUnknownCommand();


            }
        }
    }

    public void listOrders(){
        view.allOrdersBanner();
        String orderDate = view.getOrderDate();
        List<Order> orders = service.listAllOrdersForDay(orderDate);
        view.printAllOrders(orders);
    }

    public void addOrder(){

        // check if date is valid then save as order date field
        Boolean validDate = false;
        String orderDate = "";
        while(!validDate){
            orderDate = view.getOrderDate();
            validDate = service.validateOrderDate(orderDate);
        }
        // check if name is valid then save as name field
        Boolean validName = false;
        String orderName = "";
        while (!validName){
            orderName = view.getOrderName();
            validName = service.validateCustomerName(orderName);
        }

        //check if state is a valid state in taxFile then save as state abbreviation field
        Boolean validState = false;
        String stateAbbreviation = "";
        Tax chosenState = null;
        while (!validState) {
            String inputState = view.getOrderState();
            chosenState = service.validateCustomerState(inputState);
            if (chosenState != null) {
                validState = true;
                stateAbbreviation = service.setCustomerState(chosenState);
            }
        }

        // use previous selected state to generate the tax rate
        BigDecimal taxRate = service.generateTaxRate(chosenState);

        //Show all the products available and save selected as chosen product type
        view.allProductsBanner();
        List<Product> productList = service.listAllProducts();
        view.printAllProducts(productList);
        int selection = view.getProductSelection(1, productList.size());
        Product chosenProduct = productList.get(selection-1);
        String productType = service.setCustomerProduct(chosenProduct);

        // check if area is >= 100 and set as area field
        Boolean validArea = false;
        BigDecimal area = null;
        while (!validArea){
            area = view.getOrderArea();
            validArea = service.validateFloorArea(area);
        }

        // finished with validating -- setting the other calculated fields of the order
        BigDecimal costPerSquareFoot = service.setCostPerSquareFoot(chosenProduct);
        BigDecimal laborCostPerSquareFoot = service.setLaborCostPerSquareFoot(chosenProduct);
        BigDecimal materialCost = service.calculateMaterialCost(area,chosenProduct);
        BigDecimal laborCost = service.calculateLaborCost(area,chosenProduct);
        BigDecimal tax = service.calculateTax(materialCost, laborCost, taxRate);
        BigDecimal total = service.calculateTotal(materialCost, laborCost, tax);

        // Holding the current order without saving to show to user before saving the order
        Order currentOrder = service.createOrder(orderDate, orderName, stateAbbreviation, productType, taxRate, area, costPerSquareFoot,
                laborCostPerSquareFoot, materialCost, laborCost, tax, total);

        //Asking user if they want to save and only saving to file if user hits y
        String toSave = view.confirmationSaveOrder(currentOrder);
        if (toSave.equalsIgnoreCase("y")){
            service.addOrder(orderDate, currentOrder);
            view.orderAddedSuccessful();
        }

    }

    public void editOrder() throws FlooringMasteryPersistenceException {
        //display edit order banner
        view.editOrderBanner();

        //user enter order information to grab order if exists
        String orderDate = view.getOrderDate();
        Integer orderNumber = view.getOrderNumber();
        Order currentOrder = service.getOrder(orderDate, orderNumber);

        //User prompted to change name first
        String newName = view.editCustomerName(currentOrder);
        service.editOrderName(newName, currentOrder);

        //User prompted to change name
        String newState = view.editCustomerState(currentOrder);
        service.editState(newState, currentOrder);

        //user prompted to change product type
        String newProductType = view.editProductType(currentOrder);
        view.printAllProducts(service.listAllProducts());
        service.editProduct(newProductType, currentOrder);

        //user prompted to change area of floor
        BigDecimal newArea = view.editArea(currentOrder);
        service.editArea(newArea, currentOrder);

        currentOrder = service.editOrder(currentOrder);

        String toEdit = view.confirmationSaveOrder(currentOrder);
        if (toEdit.equalsIgnoreCase("y")){
            service.updateOrdersInFile(orderDate);
            view.orderAddedSuccessful();
        }

    }


    public void removeAnOrder() throws FlooringMasteryPersistenceException {

        String orderDate = view.getOrderDate();
        Integer orderNumber = view.getOrderNumber();
        Order currentOrder = service.getOrder(orderDate, orderNumber);

        String toRemove = view.confirmationRemoveOrder(currentOrder);
        if (toRemove.equalsIgnoreCase("y")){
            service.removeOrder(orderDate, currentOrder);
            service.updateOrdersInFile(orderDate);
            view.orderRemovedSuccessful();
        }

    }

    public void exportData(){
        view.exportOrdersSuccessful();
    }

}
