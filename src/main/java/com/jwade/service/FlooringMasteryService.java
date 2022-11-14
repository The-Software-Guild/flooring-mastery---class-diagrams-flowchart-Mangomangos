package com.jwade.service;

import com.jwade.dto.Order;
import com.jwade.dto.Product;

import java.math.BigDecimal;
import java.util.List;

public interface FlooringMasteryService {

    Order getOrder(String orderDate, Integer orderNumber);

    List<Order> listAllOrders();

    List<Product> listAllProducts();

    Order removeOrder(String orderDate, Integer orderNumber);

    Integer generateOrderNumber();

    String setAndValidateCustomerName();

    String setAndValidateCustomerState();

    String setCustomerProduct();

    BigDecimal setAndValidateFloorArea();

    BigDecimal generateTaxRate(String stateAbbreviation);

    Order addOrder (Integer orderNumber, String customerName, String state, String product, BigDecimal area, BigDecimal taxRate);





}
