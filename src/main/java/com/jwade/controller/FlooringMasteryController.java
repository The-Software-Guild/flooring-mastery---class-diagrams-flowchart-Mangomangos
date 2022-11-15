package com.jwade.controller;

import com.jwade.dao.FlooringMasteryDaoImpl;
import com.jwade.dao.FlooringMasteryException;
import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.service.FlooringMasteryService;
import com.jwade.service.FlooringMasteryServiceImpl;
import com.jwade.ui.FlooringMasteryView;

import java.math.BigDecimal;

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
        while(!service.validateOrderDate(view.getOrderDate())){
            String orderDate = view.getOrderDate();
            service.validateOrderDate(orderDate);
        }
        String orderName = view.getOrderName();
        String orderState = view.getOrderState();
        BigDecimal orderArea = view.getOrderArea();
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
        boolean keepGoing = true;
        while (keepGoing) {
            String operation = view.getMenuSelection();
            switch (operation) {
                case "1": // edit customer name
                    editCustomerName(currentOrder);
                    break;
                case "2": // edit state
                    editState(currentOrder);
                    break;
                case "3": // edit product type
                    editProductType(currentOrder);
                    break;
                case "4": // edit area
                    editArea(currentOrder);
                case "5": // quit
                    keepGoing = false;
                    break;
                default:
                    view.displayUnknownCommand();
            }
        }
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
