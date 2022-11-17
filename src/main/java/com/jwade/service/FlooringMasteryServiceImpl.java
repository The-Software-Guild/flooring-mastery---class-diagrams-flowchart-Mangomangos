package com.jwade.service;

import com.jwade.dao.*;
import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.dto.Tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FlooringMasteryServiceImpl implements FlooringMasteryService{

    private ProductDao productDao;
    private TaxDao taxDao;
    private FlooringMasteryDao dao;

    private Integer maxOrderNumber;

    public FlooringMasteryServiceImpl (ProductDao productDao, TaxDao taxDao, FlooringMasteryDao dao){
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
        return new ArrayList<>(mapOrdersForDay(orderDate).values());
    }
    @Override
    public Map<Integer, Order> mapOrdersForDay(String oderDate) throws FlooringMasteryPersistenceException{
        return new HashMap<>(dao.mapDayOrders(oderDate));
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
            throw new FlooringMasteryDataValidationException("Invalid date. Date must be in format MMDDYYYYY");
        }
        LocalDate today = LocalDate.now();
        if (today.isBefore(chosenDate)){
            return true;
        }
        throw new FlooringMasteryDataValidationException(
                "Invalid Date. Chosen date must be in the future!"
        );
    }

    @Override
    public Integer generateOrderNumber() {
        return maxOrderNumber = maxOrderNumber + 1;
    }

    @Override
    public Boolean validateCustomerName(String inputName) throws FlooringMasteryDataValidationException{


        List<Character> characters = new ArrayList<>();
        Collections.addAll(characters, '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '_', '=',
                '`', '[', ']', '"', ':', ';', '<', '>', '/', '{', '}', '|', '~', '\'' );

        for (Character c : inputName.toCharArray()){
            for (Character chr : characters){
                if (c.equals(chr)){
                    throw new FlooringMasteryDataValidationException(
                            "Invalid Name: Please only use letters (a-z), numbers (0-9), and characters limited to '.' and ',' ");
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
        throw new FlooringMasteryDataValidationException("State is not within tax file");
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
    public Boolean validateFloorArea(BigDecimal area) throws FlooringMasteryDataValidationException {
        if (area.compareTo(BigDecimal.valueOf(100)) >= 0){
            return true;
        }
        throw new FlooringMasteryDataValidationException(
                "Invalid area. Minimum value must be >=100"
        );
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
        dao.addOrderToFiles(orderDate, order);

    }

    @Override
    public String editOrderName(String newName, Order currentOrder){
        return dao.updateCustomerName(currentOrder, newName);
    }

    @Override
    public String editState(String state, Order currentOrder){
        return dao.updateCustomerState(currentOrder, state);

    }

    @Override
    public Product validateNewProduct(String inputProduct, Order currentOrder) throws FlooringMasteryDataValidationException {

        int productNumber;
        try{
            productNumber = Integer.parseInt(inputProduct);
        } catch (NumberFormatException e){
            throw new FlooringMasteryDataValidationException(
                    "Invalid Input. Please pick a number from the list of products."
            );
        }

        Product requestedProduct;
        try {
            requestedProduct = listAllProducts().get(productNumber-1);
        } catch (IndexOutOfBoundsException e){
            throw new FlooringMasteryDataValidationException(
                    "Invalid Input: Chosen value not a product on list"
            );
        }
        return requestedProduct;
    }

    @Override
    public void editProductType (Product chosenProduct, Order currentOrder){
        dao.updateProductType(currentOrder, chosenProduct.getProductType());
    }

    @Override
    public void exportAllOrders() throws FlooringMasteryPersistenceException {
        dao.exportAllOrders();
    }

    @Override
    public BigDecimal editArea(BigDecimal area, Order currentOrder) {
        return dao.updateArea(currentOrder, area);
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
        return area.multiply(product.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateLaborCost(BigDecimal area, Product product) {
        return area.multiply(product.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
       BigDecimal totalCosts = materialCost.add(laborCost);
       BigDecimal taxPercentage = taxRate.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
       return totalCosts.multiply(taxPercentage).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return materialCost.add(laborCost).add(tax).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void addEditedOrderToFile(Map<Integer,Order> listOfOrders, String orderDate, Order order) throws FlooringMasteryPersistenceException {
        dao.editOrdersInFile(listOfOrders,orderDate, order);
    }

    @Override
    public BigDecimal editAreaInputFromString(String inputArea) throws FlooringMasteryDataValidationException {
        try{
            return new BigDecimal(inputArea);
        } catch (NumberFormatException e){
            throw new FlooringMasteryDataValidationException(
                    "Not a valid number."
            );
        }
    }


    @Override
    public void removeOrderFromFile(Map<Integer, Order> orders, String orderDate, Order removedOrder) throws FlooringMasteryPersistenceException {
        dao.removeOrderFromFiles(orders, orderDate, removedOrder);
    }


}
