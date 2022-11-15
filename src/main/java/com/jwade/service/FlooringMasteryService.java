package com.jwade.service;

import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.dto.Tax;

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

    Tax validateCustomerState(String inputState);

    String setCustomerState (Tax taxObject);

    String setCustomerProduct();

    Boolean validateFloorArea(BigDecimal area);

    BigDecimal generateTaxRate(Tax taxObject);

    Boolean validateOrderDate(String orderDate);

    void addOrder (String orderDate, String customerName, String state, String product, BigDecimal taxRate, BigDecimal area,
                    BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost,
                    BigDecimal laborCost, BigDecimal tax, BigDecimal total);

    String editOrderName (String newName, Order currentOrder);

    String editState (String state, Order currentOrder);

    String editProduct (String product, Order currentOrder);

    BigDecimal editArea (BigDecimal area, Order currentOrder);

    Order editOrder (String orderDate, Order currentOrder);

    BigDecimal setLaborCostPerSquareFoot (Product product);

    BigDecimal setCostPerSquareFoot (Product product);

    BigDecimal calculateMaterialCost(BigDecimal area, Product product);


    BigDecimal calculateLaborCost (BigDecimal area, Product product);

    BigDecimal calculateTax (BigDecimal area, BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);




}
