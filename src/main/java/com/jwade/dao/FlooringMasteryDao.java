package com.jwade.dao;

import com.jwade.dto.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FlooringMasteryDao {

    Order getOrder(String orderDate, Integer orderNumber) throws FlooringMasteryPersistenceException;

    List<Order> listAllOrders();

    void addOrderToFiles(String orderDate, Order order) throws FlooringMasteryPersistenceException;

    void removeOrderFromFiles(Map<Integer, Order> orders, String orderDate, Order order) throws FlooringMasteryPersistenceException;

    void editOrdersInFile(Map<Integer, Order> orders, String orderDate, Order order) throws FlooringMasteryPersistenceException;

    Map<Integer, Order> mapDayOrders(String orderDate) throws FlooringMasteryPersistenceException;

    String updateCustomerName(Order order, String newName);

    String updateCustomerState(Order order, String newState);

    BigDecimal updateTaxRate (Order order, BigDecimal newTaxRate);

    String updateProductType(Order order, String newProduct);

    BigDecimal updateArea(Order order, BigDecimal newArea);

    BigDecimal updateCostPerSquareFoot(Order order, BigDecimal newCost);

    BigDecimal updateLaborCostPerSquareFoot(Order order, BigDecimal newLaborCost);

    BigDecimal updateMaterialCost(Order order, BigDecimal newMaterialCost);

    BigDecimal updateLaborCost(Order order, BigDecimal newLaborCost);

    BigDecimal updateTax(Order order, BigDecimal newTax);

    BigDecimal updateTotal(Order order, BigDecimal newTotal);

    Integer generateCurrentMaxOrderNumber(List<Order> listOfOrders);
    List<Order> orderList (Map<Integer, Order> orders);


}
