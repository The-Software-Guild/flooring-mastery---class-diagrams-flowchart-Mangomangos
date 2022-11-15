package com.jwade.service;

import com.jwade.dao.FlooringMasteryDaoImpl;
import com.jwade.dao.ProductDaoImpl;
import com.jwade.dao.TaxDaoImpl;
import com.jwade.dto.Order;
import com.jwade.dto.Product;

import java.math.BigDecimal;
import java.util.List;

public class FlooringMasteryServiceImpl implements FlooringMasteryService{

    private ProductDaoImpl productDao;
    private TaxDaoImpl taxDao;
    private FlooringMasteryDaoImpl dao;

    public FlooringMasteryServiceImpl (ProductDaoImpl productDao, TaxDaoImpl taxDao, FlooringMasteryDaoImpl dao){
        this.productDao = productDao;
        this.taxDao = taxDao;
        this.dao = dao;

    }


    @Override
    public Order getOrder(String orderDate, Integer orderNumber) {
        return null;
    }

    @Override
    public List<Order> listAllOrders() {
        return null;
    }

    @Override
    public List<Order> listAllOrdersForDay(String orderDate) {
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
    public Boolean validateCustomerName(String inputName) {
        return null;
    }

    @Override
    public Boolean validateCustomerState(String inputState) {
        return null;
    }

    @Override
    public String setCustomerProduct() {
        return null;
    }

    @Override
    public Boolean validateFloorArea(BigDecimal area) {
        return null;
    }

    @Override
    public BigDecimal generateTaxRate(String state) {
        return null;
    }

    @Override
    public Boolean validateOrderDate(String orderDate) {
        return null;
    }

    @Override
    public Order addOrder(Integer orderNumber, String customerName, String state, String product, BigDecimal area, BigDecimal taxRate) {
        return null;
    }

    @Override
    public Order editOrder(Order currentOrder) {
        return null;
    }

    @Override
    public Order removeOrder(Order currentOrder) {
        return null;
    }
}
