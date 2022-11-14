package com.jwade.controller;

import com.jwade.dao.FlooringMasteryDaoImpl;
import com.jwade.dao.FlooringMasteryException;
import com.jwade.dto.Product;
import com.jwade.ui.FlooringMasteryView;

import java.math.BigDecimal;

public class FlooringMasteryController {

    private FlooringMasteryView view;

    private FlooringMasteryDaoImpl dao;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryDaoImpl dao) {
        this.view = view;
        this.dao = dao;
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
                    removeOrder();
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
        String orderDate = view.getOrderDate();
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
    }

    public void removeOrder(){
        view.orderRemovedSuccessful();
    }

    public void exportData(){
        view.exportOrdersSuccessful();
    }

}
