package com.jwade.dao;

import com.jwade.dto.Order;

import java.math.BigDecimal;
import java.util.List;

public interface FlooringMasteryDao {

    Order getOrder(String orderDate, Integer orderNumber);

    List<Order> listAllOrders();

    Order addOrder(String orderDate, Order order);

    Order removeOrder(String orderDate, Order order);

    void editOrdersInFile(String orderDate);
    List<Order> listDayOrders(String orderDate);

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


}
