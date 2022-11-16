package com.jwade.service;

import com.jwade.dao.FlooringMasteryDaoImpl;
import com.jwade.dao.FlooringMasteryPersistenceException;
import com.jwade.dao.ProductDaoImpl;
import com.jwade.dao.TaxDaoImpl;
import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.dto.Tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class FlooringMasteryServiceImpl implements FlooringMasteryService{

    private ProductDaoImpl productDao;
    private TaxDaoImpl taxDao;
    private FlooringMasteryDaoImpl dao;

    private Integer maxOrderNumber;

    public FlooringMasteryServiceImpl (ProductDaoImpl productDao, TaxDaoImpl taxDao, FlooringMasteryDaoImpl dao){
        this.productDao = productDao;
        this.taxDao = taxDao;
        this.dao = dao;
        this.maxOrderNumber = dao.generateCurrentMaxOrderNumber(dao.listAllOrders());

    }


    @Override
    public Order getOrder(String orderDate, Integer orderNumber) throws FlooringMasteryPersistenceException {
        return dao.getOrder(orderDate, orderNumber);
    }

    @Override
    public List<Order> listAllOrders() {
        return new ArrayList<>(dao.listAllOrders());
    }

    @Override
    public List<Order> listAllOrdersForDay(String orderDate) throws FlooringMasteryPersistenceException {
        return new ArrayList<>(dao.listDayOrders(orderDate));
    }

    @Override
    public List<Product> listAllProducts() {
        return new ArrayList<>(productDao.listOfProducts());
    }

    @Override
    public Boolean validateOrderDate(String orderDate) throws FlooringMasteryDataValidationException {
        String pattern = "MMddyyyy";
        LocalDate chosenDate;
        try {
            chosenDate = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            throw new FlooringMasteryDataValidationException("Invalid date. Date must be in format DDMMYYYYY");
        }
        LocalDate today = LocalDate.now();
        if (today.isBefore(chosenDate)){
            throw new FlooringMasteryDataValidationException(
                    "Invalid Date. Chosen date must be in the future!"
            );
        }
        return true;
    }

    @Override
    public Integer generateOrderNumber() {
        return maxOrderNumber = maxOrderNumber + 1;
    }

    @Override
    public Boolean validateCustomerName(String inputName) throws FlooringMasteryDataValidationException{


        List<Character> characters = new ArrayList<>();
        Collections.addAll(characters, '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '_', '=',
                '`', '[', ']', '"', ':', ';', '<', '>', '/', '{', '}', '|', '~' );

        for (Character c : inputName.toCharArray()){
            for (Character chr : characters){
                if (c.equals(chr)){
                    throw new FlooringMasteryDataValidationException(
                            "Invalid Name: Please only use letters (a-z), numbers (0-9), and charachers limited to '.' and ',' ");
                }
            }
        }
        return true;
    }

    @Override
    public Tax validateCustomerState(String inputState) throws FlooringMasteryDataValidationException {
        List<Tax> listOfTaxRates = taxDao.listOfTaxes();
        for (Tax tax : listOfTaxRates){
            if (tax.getStateAbbreviation().equalsIgnoreCase(inputState) ||
                    tax.getStateName().equalsIgnoreCase(inputState)){
               return tax;
            }
        }
        throw new FlooringMasteryDataValidationException("State is within tax file");
    }

    @Override
    public String setCustomerState(Tax taxObject) {
        return taxObject.getStateAbbreviation();
    }

    @Override
    public BigDecimal generateTaxRate(Tax taxObject) {
        return taxObject.getTaxRate();
    }

    @Override
    public String setCustomerProduct(Product product) {
        return product.getProductType();
    }

    @Override
    public Boolean validateFloorArea(BigDecimal area) {
        return area.compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    @Override
    public Order createOrder (String orderDate, String customerName, String state, String product, BigDecimal taxRate, BigDecimal area,
                          BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost,
                          BigDecimal laborCost, BigDecimal tax, BigDecimal total) {

        Integer orderNumber = generateOrderNumber();
        return new Order(orderNumber, customerName, state, product,
                taxRate, area, costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, total);

    }

    @Override
    public void addOrder(String orderDate, Order order) throws FlooringMasteryPersistenceException {
        dao.addOrder(orderDate, order);

    }

    @Override
    public String editOrderName(String newName, Order currentOrder) throws FlooringMasteryDataValidationException {
        if (!newName.isBlank()){
            if (validateCustomerName(newName)) {
                dao.updateCustomerName(currentOrder, newName);
            }
        }
        return currentOrder.getCustomerName();
    }

    @Override
    public String editState(String state, Order currentOrder) throws FlooringMasteryDataValidationException {
        if (!state.isBlank()){
            Tax tax = validateCustomerState(state);
            if (tax != null){
                dao.updateCustomerState(currentOrder, state);
            }
        }
        return currentOrder.getState();
    }

    @Override
    public Product editProduct(String inputProduct, Order currentOrder) {

        Product requestedProduct = productDao.getProduct(inputProduct);

        if (requestedProduct.getCostPerSquareFoot() !=null){
            dao.updateProductType(currentOrder, requestedProduct.getProductType());
            return requestedProduct;
        }
        return(productDao.getProduct(currentOrder.getProductType()));
    }

    @Override
    public BigDecimal editArea(BigDecimal area, Order currentOrder) {
        if (area != null){
            if (validateFloorArea(area)){
                dao.updateArea(currentOrder, area);
            }
        }
        return currentOrder.getArea();
    }

    @Override
    public Order editOrder(Order currentOrder) {

        //Grab necessary information to set the remaining calculating fields
        Tax orderTaxInfo = taxDao.getTax(currentOrder.getState());
        Product orderProductInfo = productDao.getProduct(currentOrder.getProductType());
        BigDecimal area = currentOrder.getArea();

        ///edit the calculated fields and update it in the map
        BigDecimal newTaxRate = dao.updateTaxRate(currentOrder, orderTaxInfo.getTaxRate());
        dao.updateCostPerSquareFoot(currentOrder, orderProductInfo.getCostPerSquareFoot());
        dao.updateLaborCostPerSquareFoot(currentOrder, orderProductInfo.getLaborCostPerSquareFoot());

        BigDecimal newLaborCost = calculateLaborCost(area, orderProductInfo);
        dao.updateLaborCost(currentOrder, newLaborCost);

        BigDecimal newMaterialCost = calculateMaterialCost(area, orderProductInfo);
        dao.updateMaterialCost(currentOrder, newMaterialCost);

        BigDecimal newTaxCost = calculateTax(newMaterialCost, newLaborCost, newTaxRate);
        dao.updateTax(currentOrder, newTaxCost);

        BigDecimal newTotal = calculateTotal(newMaterialCost, newLaborCost, newTaxCost);
        dao.updateTotal(currentOrder, newTotal);

        return  currentOrder;
    }

    @Override
    public BigDecimal setLaborCostPerSquareFoot(Product product) {
        return product.getLaborCostPerSquareFoot();
    }

    @Override
    public BigDecimal setCostPerSquareFoot(Product product) {
        return product.getCostPerSquareFoot();
    }

    @Override
    public BigDecimal calculateMaterialCost(BigDecimal area, Product product) {
        return area.multiply(product.getCostPerSquareFoot());
    }

    @Override
    public BigDecimal calculateLaborCost(BigDecimal area, Product product) {
        return area.multiply(product.getLaborCostPerSquareFoot());
    }

    @Override
    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
       BigDecimal totalCosts = materialCost.add(laborCost);
       BigDecimal taxPercentage = taxRate.divide(new BigDecimal(100), RoundingMode.HALF_UP);
       return totalCosts.multiply(taxPercentage);
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return materialCost.add(laborCost).add(tax);
    }

    @Override
    public void updateOrdersInFile(String orderDate) throws FlooringMasteryPersistenceException {
        dao.editOrdersInFile(orderDate);
    }


    @Override
    public Order removeOrder(String orderDate, Order removedOrder) {
        return dao.removeOrder(orderDate, removedOrder);
    }


}
