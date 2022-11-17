package com.jwade.dao;

import com.jwade.dto.Order;

import java.math.BigDecimal;
import java.util.*;

public class FlooringMasteryDaoImpl implements FlooringMasteryDao {

    private Map<Integer, Order> orderMap = new HashMap<>();

    private FileDao fileDao = new FileDaoImpl();

    public FlooringMasteryDaoImpl() throws FlooringMasteryPersistenceException {
        String FILE_PATH = "src/main/Orders";
        orderMap = fileDao.readAllFiles(FILE_PATH);

    }

    @Override
    public Order getOrder(String orderDate, Integer orderNumber) throws FlooringMasteryPersistenceException {
        String filePath = "src/main/Orders/orders_" + orderDate + ".txt";
        if (fileDao.doesFileExist(orderDate)){
            Map<Integer, Order> orders = fileDao.readFile(filePath);
            if (orders.get(orderNumber) != null){
                return orders.get(orderNumber);
            }
        }
        throw new FlooringMasteryPersistenceException("Order for given date does not exist");


    }

    @Override
    public List<Order> listAllOrders() {
        return new ArrayList<>(orderMap.values());
    }


    @Override
    public void addOrderToFiles(String orderDate, Order order) throws FlooringMasteryPersistenceException {
        fileDao.addNewOrderToFile(orderDate, order);
    }

    @Override
    public void removeOrderFromFiles(Map<Integer, Order> orders, String orderDate, Order order) throws FlooringMasteryPersistenceException {
        orders.remove(order.getOrderNumber());
        String filepath = "src/main/Orders/orders_" + orderDate + ".txt";
        fileDao.updateOrderInFile(new ArrayList<>(orders.values()), filepath);
    }

    @Override
    public void editOrdersInFile(Map<Integer, Order> orders, String orderDate, Order order) throws FlooringMasteryPersistenceException {
        orders.put(order.getOrderNumber(), order);
        String filepath = "src/main/Orders/orders_" + orderDate + ".txt";
        fileDao.updateOrderInFile(new ArrayList<>(orders.values()), filepath);
    }

    @Override
    public Map<Integer, Order> mapDayOrders(String orderDate) throws FlooringMasteryPersistenceException {
        String filepath = ("src/main/Orders/orders_" + orderDate + ".txt");
        return fileDao.readFile(filepath);
    }

    public List<Order> orderList (Map<Integer, Order> orders){
        return new ArrayList<>(orders.values());
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

