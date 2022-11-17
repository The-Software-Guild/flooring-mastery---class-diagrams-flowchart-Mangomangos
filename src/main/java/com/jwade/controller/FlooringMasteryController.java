package com.jwade.controller;

import com.jwade.dao.FlooringMasteryPersistenceException;
import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.dto.Tax;
import com.jwade.service.FlooringMasteryDataValidationException;
import com.jwade.service.FlooringMasteryServiceImpl;
import com.jwade.ui.FlooringMasteryView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class FlooringMasteryController {

    private FlooringMasteryView view;

    private FlooringMasteryServiceImpl service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceImpl service) {
        this.view = view;
        this.service = service;
    }

    public void run () {
        boolean start = true;
        while (start){
            view.printMenu();
            String operation = view.getMenuSelection();
            switch (operation) {
                case "1": // display order
                    try{
                        listOrders();
                    } catch (FlooringMasteryPersistenceException e){
                        view.displayErrorMessage(e.getMessage());
                    }
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

    public void listOrders() throws FlooringMasteryPersistenceException {
        String orderDate = view.getOrderDate();
        List<Order> orders = service.listAllOrdersForDay(orderDate);
        view.printAllOrders(orders);
    }

    public void addOrder() {

        // check if date is valid then save as order date field
        boolean validDate = false;
        String orderDate = "";
        while(!validDate){
            orderDate = view.getOrderDate();
            try {
                validDate = service.validateOrderDate(orderDate);
            } catch (FlooringMasteryDataValidationException e){
                view.displayErrorMessage(e.getMessage());
            }
        }
        // check if name is valid then save as name field
        boolean validName = false;
        String orderName = "";
        while (!validName){
            orderName = view.getOrderName();
            try{
                validName = service.validateCustomerName(orderName);
            } catch (FlooringMasteryDataValidationException e){
                view.displayErrorMessage(e.getMessage());
            }
        }

        //check if state is a valid state in taxFile then save as state abbreviation field
        boolean validState = false;
        String stateAbbreviation = "";
        Tax chosenState = null;
        while (!validState) {
            String inputState = view.getOrderState();
            try{
                chosenState = service.validateCustomerState(inputState);
            } catch (FlooringMasteryDataValidationException e){
                view.displayErrorMessage(e.getMessage());
            }
            validState = true;
            assert chosenState != null;
            stateAbbreviation = service.setCustomerState(chosenState);
        }

        // use previous selected state to generate the tax rate
        BigDecimal taxRate = service.generateTaxRate(chosenState);

        //Show all the products available and save selected as chosen product type
        List<Product> productList = service.listAllProducts();
        view.printAllProducts(productList);
        int selection = view.getProductSelection(1, productList.size());
        Product chosenProduct = productList.get(selection-1);
        String productType = service.setCustomerProduct(chosenProduct);

        // check if area is >= 100 and set as area field
        boolean validArea = false;
        BigDecimal area = BigDecimal.valueOf(0);
        while (!validArea){
            area = view.getOrderArea();
            try {
                validArea = service.validateFloorArea(area);
            }catch (FlooringMasteryDataValidationException e) {
                view.displayErrorMessage(e.getMessage());
            }
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
            try{
                service.addOrder(orderDate, currentOrder);
            } catch (FlooringMasteryPersistenceException e){
                view.displayErrorMessage(e.getMessage());
            }
            view.orderAddedSuccessful();
        }

    }


    public void editOrder() {
        //display edit order banner
        view.editOrderBanner();

        //user enter order information to grab order if exists
        String orderDate = view.getOrderDate();
        Integer orderNumber = view.getOrderNumber();

        Order currentOrder = null;
        Map<Integer, Order> orders = null;


        try{
            currentOrder = service.getOrder(orderDate, orderNumber);
            orders = service.mapOrdersForDay(orderDate);
        } catch (FlooringMasteryPersistenceException e){
            view.displayErrorMessage(e.getMessage());
            return;
        }
        

        //User prompted to change name first
        assert currentOrder != null;
        String newName = view.editCustomerName(currentOrder);
        if (!newName.isEmpty()){
            boolean validName = false;
            while(!validName){
                try{
                    validName = service.validateCustomerName(newName);
                } catch (FlooringMasteryDataValidationException e){
                    view.displayErrorMessage(e.getMessage());
                    newName = view.editCustomerName(currentOrder);
                }
            }
            service.editOrderName(newName, currentOrder);
        }


        //User prompted to change State Name
        String newState = view.editCustomerState(currentOrder);
        if (!newState.isEmpty()){
            Tax chosenState = null;
            boolean validState = false;
            while(!validState){
                try{
                    chosenState = service.validateCustomerState(newState);
                    validState = true;
                } catch (FlooringMasteryDataValidationException e){
                    view.displayErrorMessage(e.getMessage());
                    newState = view.editCustomerState(currentOrder);
                }
            }
            service.editState(chosenState.getStateAbbreviation(), currentOrder);
        }


        //user prompted to change product type
        view.printAllProducts(service.listAllProducts());
        String newProductType = view.editProductType(currentOrder);
        if (!newProductType.isEmpty()){
            Product chosenProduct = null;
            boolean validProduct = false;
            while(!validProduct){
                try{
                    chosenProduct = service.validateNewProduct(newProductType, currentOrder);
                    validProduct =true;
                }catch (FlooringMasteryDataValidationException e){
                    view.displayErrorMessage(e.getMessage());
                    view.printAllProducts(service.listAllProducts());
                    newProductType = view.editProductType(currentOrder);
                }
            }
            service.editProductType(chosenProduct, currentOrder);
        }

        //user prompted to change area of floor
        String newArea = view.editArea(currentOrder);
        if (!newArea.isEmpty()){
            BigDecimal area = null;
            boolean validArea = false;
            while (!validArea){
                try {
                    area = service.editAreaInputFromString(newArea);
                    service.validateFloorArea(area);
                    validArea = true;
                } catch (FlooringMasteryDataValidationException e){
                    view.displayErrorMessage(e.getMessage());
                    newArea = view.editArea(currentOrder);
                }
            }
            service.editArea(area, currentOrder);

        }

        //update the rest of the calculated fields
        currentOrder = service.editOrder(currentOrder);

        //ask user if they would like to save this updated order -- otherwise changes will disappear
        String toEdit = view.confirmationSaveOrder(currentOrder);
        if (toEdit.equalsIgnoreCase("y")){
            if (orders != null) {
                try {
                    service.addEditedOrderToFile(orders, orderDate, currentOrder);
                    view.orderAddedSuccessful();
                } catch (FlooringMasteryPersistenceException e){
                    view.displayErrorMessage(e.getMessage());
                }
            }
        }
    }


    public void removeAnOrder() {

        String orderDate = view.getOrderDate();
        Integer orderNumber = view.getOrderNumber();

        Order removedOrder = null;
        Map<Integer, Order> orders = null;

        try{
            removedOrder = service.getOrder(orderDate, orderNumber);
            orders = service.mapOrdersForDay(orderDate);
        } catch (FlooringMasteryPersistenceException e){
            view.displayErrorMessage(e.getMessage());
            return;
        }

        assert removedOrder != null;
        String toRemove = view.confirmationRemoveOrder(removedOrder);
        if (toRemove.equalsIgnoreCase("y")){
            try{
                service.removeOrderFromFile(orders, orderDate, removedOrder);
                view.orderRemovedSuccessful();
            } catch (FlooringMasteryPersistenceException e){
                view.displayErrorMessage(e.getMessage());
            }
        }
    }

    public void exportData(){
        try{
            service.exportAllOrders();
            view.exportOrdersSuccessful();
        } catch (FlooringMasteryPersistenceException e){
            view.displayErrorMessage(e.getMessage());
        }

    }

}
