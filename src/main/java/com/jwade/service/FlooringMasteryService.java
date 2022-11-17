package com.jwade.service;

import com.jwade.dao.FlooringMasteryPersistenceException;
import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.dto.Tax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FlooringMasteryService {

    Order getOrder(String orderDate, Integer orderNumber) throws FlooringMasteryPersistenceException;

    List<Order> listAllOrders();

    List<Order> listAllOrdersForDay(String orderDate) throws FlooringMasteryPersistenceException;

    List<Product> listAllProducts();

    void removeOrderFromFile(Map<Integer, Order> orders, String orderDate, Order removedOrder) throws FlooringMasteryPersistenceException;

    Integer generateOrderNumber();

    Boolean validateCustomerName(String inputName) throws FlooringMasteryDataValidationException;

    Tax validateCustomerState(String inputState) throws FlooringMasteryDataValidationException;

    String setCustomerState (Tax taxObject);

    String setCustomerProduct(Product product);

    Boolean validateFloorArea(BigDecimal area) throws FlooringMasteryDataValidationException;

    BigDecimal generateTaxRate(Tax taxObject);

    Boolean validateOrderDate(String orderDate) throws FlooringMasteryDataValidationException;

    Order createOrder (String orderDate, String customerName, String state, String product, BigDecimal taxRate, BigDecimal area,
                    BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost,
                    BigDecimal laborCost, BigDecimal tax, BigDecimal total);
    void addOrder(String orderDate, Order order) throws FlooringMasteryPersistenceException;

    String editOrderName (String newName, Order currentOrder);

    String editState (String state, Order currentOrder);

    Product validateNewProduct(String inputProduct, Order currentOrder) throws FlooringMasteryDataValidationException;

    BigDecimal editArea (BigDecimal area, Order currentOrder);

    Order editOrder (Order currentOrder);

    BigDecimal setLaborCostPerSquareFoot (Product product);

    BigDecimal setCostPerSquareFoot (Product product);

    BigDecimal calculateMaterialCost(BigDecimal area, Product product);


    BigDecimal calculateLaborCost (BigDecimal area, Product product);

    BigDecimal calculateTax (BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);

    void addEditedOrderToFile(Map<Integer, Order> orders, String orderDate, Order order) throws FlooringMasteryPersistenceException;

    BigDecimal editAreaInputFromString(String area) throws FlooringMasteryDataValidationException;

    void editProductType (Product chosenProduct, Order currentOrder);

    public void exportAllOrders() throws FlooringMasteryPersistenceException;
    public Map<Integer, Order> mapOrdersForDay(String oderDate) throws FlooringMasteryPersistenceException;





}
