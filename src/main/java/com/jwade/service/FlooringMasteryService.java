package com.jwade.service;

import com.jwade.dao.FlooringMasteryPersistenceException;
import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.dto.Tax;

import java.math.BigDecimal;
import java.util.List;

public interface FlooringMasteryService {

    Order getOrder(String orderDate, Integer orderNumber) throws FlooringMasteryPersistenceException;

    List<Order> listAllOrders();

    List<Order> listAllOrdersForDay(String orderDate) throws FlooringMasteryPersistenceException;

    List<Product> listAllProducts();

    Order removeOrder(String orderDate, Order removedOrder);

    Integer generateOrderNumber();

    Boolean validateCustomerName(String inputName) throws FlooringMasteryDataValidationException;

    Tax validateCustomerState(String inputState) throws FlooringMasteryDataValidationException;

    String setCustomerState (Tax taxObject);

    String setCustomerProduct(Product product);

    Boolean validateFloorArea(BigDecimal area);

    BigDecimal generateTaxRate(Tax taxObject);

    Boolean validateOrderDate(String orderDate) throws FlooringMasteryDataValidationException;

    Order createOrder (String orderDate, String customerName, String state, String product, BigDecimal taxRate, BigDecimal area,
                    BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost,
                    BigDecimal laborCost, BigDecimal tax, BigDecimal total);
    void addOrder(String orderDate, Order order) throws FlooringMasteryPersistenceException;

    String editOrderName (String newName, Order currentOrder) throws FlooringMasteryDataValidationException;

    String editState (String state, Order currentOrder) throws FlooringMasteryDataValidationException;

    Product editProduct (String inputProduct, Order currentOrder);

    BigDecimal editArea (BigDecimal area, Order currentOrder);

    Order editOrder (Order currentOrder);

    BigDecimal setLaborCostPerSquareFoot (Product product);

    BigDecimal setCostPerSquareFoot (Product product);

    BigDecimal calculateMaterialCost(BigDecimal area, Product product);


    BigDecimal calculateLaborCost (BigDecimal area, Product product);

    BigDecimal calculateTax (BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);

    void updateOrdersInFile(String orderDate) throws FlooringMasteryPersistenceException;




}
