package com.jwade.dao;

import com.jwade.dto.Order;

import java.math.BigDecimal;
import java.util.*;

public class FlooringMasteryDaoImpl implements FlooringMasteryDao {

    private Map<Integer, Order> orderMap = new HashMap<>();

    private FileDao fileDao = new FileDaoImpl();
    private String FILE_PATH;

    public FlooringMasteryDaoImpl() {
        String FILE_PATH = "Orders/";
        orderMap = fileDao.readFiles(FILE_PATH);

    }

    @Override
    public Order getOrder(String orderDate, Integer orderNumber) {
        if (fileDao.doesFileExist(orderDate)){
            Map<Integer, Order> orders = fileDao.readFile(orderDate);
            if (orders.get(orderNumber) != null){
                return orders.get(orderNumber);
            }
        }
        return null;


    }

    @Override
    public List<Order> listAllOrders() {
        return new ArrayList<>(orderMap.values());
    }


    @Override
    public Order addOrder(String orderDate, Order order) {
        Order newOrder = orderMap.put(order.getOrderNumber(), order);
        fileDao.addNewOrderToFile(orderDate, newOrder);
        return newOrder;
    }

    @Override
    public Order removeOrder(String orderDate, Order order) {
        return null;
    }

    @Override
    public Order editOrder(Order order, String orderDate) {
        Order updatedOrder = orderMap.put(order.getOrderNumber(), order);
        fileDao.updateOrderInFile(updatedOrder, orderDate);
        return updatedOrder;
    }

    @Override
    public List<Order> listDayOrders(String orderDate) {
        Map<Integer, Order> orderDayMap;
        orderDayMap = fileDao.readFile("Orders/orders_" + orderDate + "txt");
        return new ArrayList<>(orderDayMap.values());
    }

    @Override
    public String updateCustomerName(Order order, String newName) {
        order.setCustomerName(newName);
        return newName;
    }

    @Override
    public String updateCustomerState(Order order, String newState) {
        order.setState(newState);
        return newState;
    }

    @Override
    public BigDecimal updateTaxRate(Order order, BigDecimal newTaxRate) {
        order.setTax(newTaxRate);
        return newTaxRate;
    }

    @Override
    public String updateProductType(Order order, String newProduct) {
        order.setProductType(newProduct);
        return newProduct;
    }

    @Override
    public BigDecimal updateArea(Order order, BigDecimal newArea) {
        order.setArea(newArea);
        return newArea;
    }

    @Override
    public BigDecimal updateCostPerSquareFoot(Order order, BigDecimal newCost) {
        order.setCostPerSquareFoot(newCost);
        return newCost;
    }

    @Override
    public BigDecimal updateLaborCostPerSquareFoot(Order order, BigDecimal newLaborCost) {
        order.setLaborCostPerSquareFoot(newLaborCost);
        return newLaborCost;
    }

    @Override
    public BigDecimal updateMaterialCost(Order order, BigDecimal newMaterialCost) {
        order.setMaterialCost(newMaterialCost);
        return newMaterialCost;
    }

    @Override
    public BigDecimal updateLaborCost(Order order, BigDecimal newLaborCost) {
        order.setLaborCost(newLaborCost);
        return newLaborCost;
    }

    @Override
    public BigDecimal updateTax(Order order, BigDecimal newTax) {
        order.setTax(newTax);
        return newTax;
    }

    @Override
    public BigDecimal updateTotal(Order order, BigDecimal newTotal) {
        order.setTotal(newTotal);
        return newTotal;
    }

    @Override
    public Integer generateCurrentMaxOrderNumber(List<Order> listOfOrders) {
        List<Integer> listOfOrderNumbers = new ArrayList<>();
        listOfOrders = listAllOrders();
        for (Order order: listOfOrders){
            listOfOrderNumbers.add(order.getOrderNumber());
        }

        return Collections.max(listOfOrderNumbers);

    }


}

