package com.jwade.service;

import com.jwade.dto.Order;
import com.jwade.dto.Product;

import java.math.BigDecimal;
import java.util.List;

public interface FlooringMasteryService {

    Order getOrder(String orderDate, Integer orderNumber);

    List<Order> listAllOrders();

    List<Order> listAllOrdersForDay(String orderDate);

    List<Product> listAllProducts();

    Order removeOrder(String orderDate, Integer orderNumber);

    Integer generateOrderNumber();

    Boolean validateCustomerName(String inputName);

    Boolean validateCustomerState(String inputState);

    String setCustomerProduct();

    Boolean validateFloorArea(BigDecimal area);

    BigDecimal generateTaxRate(String state);

    Boolean validateOrderDate(String orderDate);

    Order addOrder (Integer orderNumber, String customerName, String state, String product, BigDecimal area, BigDecimal taxRate);

    Order editOrder (Order currentOrder);

    Order removeOrder (Order currentOrder);






}
