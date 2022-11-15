package com.jwade.controller;

import com.jwade.dao.FlooringMasteryDaoImpl;
import com.jwade.dao.FlooringMasteryException;
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

    public void addOrder(){
        Boolean validDate = false;
        String orderDate = "";
        while(!validDate){
            orderDate = view.getOrderDate();
            validDate = service.validateOrderDate(orderDate);
        }

        Boolean validName = false;
        String orderName = "";
        while (!validName){
            orderName = view.getOrderName();
            validName = service.validateCustomerName(orderName);
        }

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

        BigDecimal taxRate = service.generateTaxRate(chosenState);

        view.allProductsBanner();
        List<Product> productList = service.listAllProducts();
        view.printAllProducts(productList);
        int selection = view.getProductSelection(1, productList.size());
        Product chosenProduct = productList.get(selection-1);
        String productType = service.setCustomerProduct(chosenProduct);

        Boolean validArea = false;
        BigDecimal area = null;
        while (!validArea){
            area = view.getOrderArea();
            validArea = service.validateFloorArea(area);
        }

        BigDecimal costPerSquareFoot = service.setCostPerSquareFoot(chosenProduct);
        BigDecimal laborCostPerSquareFoot = service.setLaborCostPerSquareFoot(chosenProduct);
        BigDecimal materialCost = service.calculateMaterialCost(area,chosenProduct);
        BigDecimal laborCost = service.calculateLaborCost(area,chosenProduct);
        BigDecimal tax = service.calculateTax(materialCost, laborCost, taxRate);
        BigDecimal total = service.calculateTotal(materialCost, laborCost, tax);

        service.addOrder(orderDate, orderName, stateAbbreviation, productType, taxRate, area, costPerSquareFoot,
                laborCostPerSquareFoot, materialCost, laborCost, tax, total);

        view.orderAddedSuccessful();
    }

    public void listOrders(){
        view.allOrdersBanner();
    }

    public void editOrder(){
        view.editOrderBanner();
        String orderDate = view.getOrderDate();
        Integer orderNumber = view.getOrderNumber();
        Order currentOrder = service.getOrder(orderDate, orderNumber);
        String newName = view.editCustomerName(currentOrder);
        String newState = view.editCustomerState(currentOrder);
        view.editProductType(currentOrder);
        view.printAllProducts(service.listAllProducts());
        BigDecimal newArea = view.editArea(currentOrder);

    }

    private void editArea(Order currentOrder) {
        BigDecimal area = view.getOrderArea();
        service.validateFloorArea(area);
        view.confirmationSaveOrder(currentOrder);
        view.editOrderSuccessful();
    }

    private void editProductType(Order currentOrder) {
        view.confirmationSaveOrder(currentOrder);
        view.editOrderSuccessful();
    }

    private void editState(Order currentOrder) {
        String newState = view.getOrderState();
        service.validateCustomerState(newState);
        view.confirmationSaveOrder(currentOrder);
        view.editOrderSuccessful();
    }

    private void editCustomerName(Order currentOrder) {
        String newName = view.getOrderName();
        view.confirmationSaveOrder(currentOrder);
        view.editOrderSuccessful();
    }

    public void removeAnOrder(){
        String orderDate = view.getOrderDate();
        Integer orderNumber = view.getOrderNumber();
        Order currentOrder = service.getOrder(orderDate, orderNumber);
        view.confirmationRemoveOrder(currentOrder);
        view.orderRemovedSuccessful();
    }

    public void exportData(){
        view.exportOrdersSuccessful();
    }

}
