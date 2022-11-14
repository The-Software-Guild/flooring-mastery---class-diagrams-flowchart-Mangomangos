package com.jwade.service;

import com.jwade.dto.Order;
import com.jwade.dto.Product;

import java.math.BigDecimal;
import java.util.List;

public class FlooringMasteryServiceImpl implements FlooringMasteryService{


    @Override
    public Order getOrder(String orderDate, Integer orderNumber) {
        return null;
    }

    @Override
    public List<Order> listAllOrders() {
        return null;
    }

    @Override
    public List<Product> listAllProducts() {
        return null;
    }

    @Override
    public Order removeOrder(String orderDate, Integer orderNumber) {
        return null;
    }

    @Override
    public Integer generateOrderNumber() {
        return null;
    }

    @Override
    public String setAndValidateCustomerName() {
        return null;
    }

    @Override
    public String setAndValidateCustomerState() {
        return null;
    }

    @Override
    public String setCustomerProduct() {
        return null;
    }

    @Override
    public BigDecimal setAndValidateFloorArea() {
        return null;
    }

    @Override
    public BigDecimal generateTaxRate(String stateAbbreviation) {
        return null;
    }

    @Override
    public Order addOrder(Integer orderNumber, String customerName, String state, String product, BigDecimal area, BigDecimal taxRate) {
        return null;
    }
}
