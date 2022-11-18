package com.jwade.service;

import com.jwade.dao.FlooringMasteryDao;
import com.jwade.dao.FlooringMasteryPersistenceException;
import com.jwade.dao.ProductDao;
import com.jwade.dao.TaxDao;
import com.jwade.dto.Order;
import com.jwade.dto.Product;
import com.jwade.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;
import org.mockito.Mock;
import org.mockito.internal.matchers.Or;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryServiceImplTest {

    private FlooringMasteryService service;
    private FlooringMasteryDao dao;
    private ProductDao productDao;
    private TaxDao taxDao;
    private List<Order> ordersList1 = new ArrayList<>();
    private List<Order> ordersList2 = new ArrayList<>();
    private Map<Integer, Order> orderMap = new HashMap<>();
    private List<Product> productList = new ArrayList<>();
    private List<Tax> taxList = new ArrayList<>();
    private Map<String, Product> productMap = new HashMap<>();
    private Map<String, Tax> taxMap = new HashMap<>();


    @BeforeEach
    public void setUp(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContextTest.xml");
        service = ctx.getBean("service", FlooringMasteryService.class);
        dao = ctx.getBean("daoMock", FlooringMasteryDao.class);
        productDao = ctx.getBean("productDaoMock", ProductDao.class);
        taxDao = ctx.getBean("taxDaoMock", TaxDao.class);

        Order order1 = new Order(1, "Customer A", "California", "Carpet", new BigDecimal(25), new BigDecimal(100),
                new BigDecimal(2.15), new BigDecimal(4.25), new BigDecimal(215), new BigDecimal(425),
                new BigDecimal(160), new BigDecimal(800));

        Order order2 = new Order(2, "Customer B", "Alaska", "Tile", new BigDecimal(10), new BigDecimal(200),
                new BigDecimal(1.50), new BigDecimal(4.00), new BigDecimal(300), new BigDecimal(800),
                new BigDecimal(110), new BigDecimal(1210));

        Order order3 = new Order(3, "Customer C", "Virginia", "Wood", new BigDecimal(15), new BigDecimal(100),
                new BigDecimal(2.00), new BigDecimal(5.00), new BigDecimal(200), new BigDecimal(1000),
                new BigDecimal(180), new BigDecimal(1380));

        Order order4 = new Order(4, "Customer D", "Montana", "Laminate", new BigDecimal(10), new BigDecimal(100),
                new BigDecimal(1.25), new BigDecimal(3.25), new BigDecimal(125), new BigDecimal(325),
                new BigDecimal(45), new BigDecimal(495));

        ordersList1.add(order1);
        ordersList1.add(order2);

        ordersList2.add(order3);
        ordersList2.add(order4);

        orderMap.put(order1.getOrderNumber(), order1);
        orderMap.put(order2.getOrderNumber(), order2);
        orderMap.put(order3.getOrderNumber(), order3);
        orderMap.put(order4.getOrderNumber(), order4);

        Product product1 = new Product("Carpet", new BigDecimal(2.15), new BigDecimal(4.25));
        Product product2 = new Product("Tile", new BigDecimal(1.50), new BigDecimal(4.00));
        Product product3 = new Product("Wood", new BigDecimal(2.00), new BigDecimal(5.00));
        Product product4 = new Product("Laminate", new BigDecimal(1.25), new BigDecimal(3.25));

        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);

        for (Product product : productList){
            productMap.put(product.getProductType(), product);
        }

        Tax tax1 = new Tax("AK", "Alaska", new BigDecimal(10));
        Tax tax2 = new Tax("CA", "California", new BigDecimal(25));
        Tax tax3 = new Tax("VA", "Virginia", new BigDecimal(15));
        Tax tax4 = new Tax("MT", "Montana", new BigDecimal(10));

        taxList.add(tax1);
        taxList.add(tax2);
        taxList.add(tax3);
        taxList.add(tax4);

        for (Tax tax: taxList){
            taxMap.put(tax.getStateAbbreviation(), tax);
        }




    }



    @Test
    void getOrder() throws FlooringMasteryPersistenceException {

        Order order = new Order("Greg", "California", "Carpet", new BigDecimal(100));

        when(dao.getOrder(anyString(), anyInt())).thenReturn(order);
        assertEquals(order, service.getOrder("12234", 1));
        verify(dao).getOrder("12234", 1);

    }

    @Test
    void listAllOrders() {
        when(dao.listAllOrders()).thenReturn(ordersList1);
        assertEquals(ordersList1, service.listAllOrders());
        verify(dao, atLeast(1)).listAllOrders();
    }

    @Test
    void listAllOrdersForDay() throws FlooringMasteryPersistenceException {
        List<Order> orderList3 = new ArrayList<>(ordersList1);
        orderList3.addAll(ordersList2);
        when(dao.mapDayOrders(anyString())).thenReturn(orderMap);
        assertEquals(orderList3, service.listAllOrdersForDay("12042021"));
        verify(dao).mapDayOrders("12042021");

    }

    @Test
    void mapOrdersForDay() throws FlooringMasteryPersistenceException {
        when(dao.mapDayOrders(anyString())).thenReturn(orderMap);
        assertEquals(orderMap, service.mapOrdersForDay("12042021"));
        verify(dao).mapDayOrders("12042021");

    }

    @Test
    void listAllProducts() {
        when(productDao.listOfProducts()).thenReturn(productList);
        assertEquals(productList, service.listAllProducts());
        verify(productDao).listOfProducts();
    }

    @Test
    void validateValidOrderDate() throws FlooringMasteryDataValidationException {
        String date = "12042025";
        assertTrue(service.validateOrderDate(date));
        assertDoesNotThrow(()-> service.validateOrderDate(date));
    }

    @Test
    @DisplayName("Testing invalid date entry")
    void validateInvalidOrderDate(){
        String date = "123asdfe";
        assertThrows(FlooringMasteryDataValidationException.class, ()-> service.validateOrderDate(date));
    }

    @Test
    void generateOrderNumber() {
        service.generateOrderNumber();
        service.generateOrderNumber();
        service.generateOrderNumber();
        assertEquals(4, service.generateOrderNumber());
    }

    @Test
    void validateValidCustomerName() throws FlooringMasteryDataValidationException {
        String name = "HELLO, World.";
        assertTrue(service.validateCustomerName(name));
        assertDoesNotThrow(()-> service.validateCustomerName(name));
    }

    @Test
    void validateInvalidCustomerName(){
        String name = "Hello$%@";
        assertThrows(FlooringMasteryDataValidationException.class, ()-> service.validateCustomerState(name));
    }

    @Test
    void validateCustomerStateValidFullStateName() throws FlooringMasteryDataValidationException {
        String state = "Virginia";
        when(taxDao.listOfTaxes()).thenReturn(taxList);
        assertEquals(taxMap.get("VA"), service.validateCustomerState(state));
        assertDoesNotThrow(()-> service.validateCustomerState(state));
        verify(taxDao, atLeast(2)).listOfTaxes();
    }

    @Test
    void validateCustomerStateValidAbbreviation() throws FlooringMasteryDataValidationException {
        String state = "VA";
        when(taxDao.listOfTaxes()).thenReturn(taxList);
        assertEquals(taxMap.get("VA"), service.validateCustomerState(state));
        assertDoesNotThrow(()-> service.validateCustomerState(state));
        verify(taxDao, atLeast(2)).listOfTaxes();
    }

    @Test
    void validateCustomerStateInvalid(){
        String state = "Washington";
        when(taxDao.listOfTaxes()).thenReturn(taxList);
        assertThrows(FlooringMasteryDataValidationException.class, ()-> service.validateCustomerState(state));
        verify(taxDao).listOfTaxes();
    }

    @Test
    void validateValidThresholdFloorArea() throws FlooringMasteryDataValidationException {
        BigDecimal area = new BigDecimal(100);
        assertTrue(service.validateFloorArea(area));
        assertDoesNotThrow(()-> service.validateFloorArea(area));
    }

    @Test
    void validateInvalidFloorAreaTooSmall(){
        BigDecimal area = new BigDecimal(99);
        assertThrows(FlooringMasteryDataValidationException.class, ()-> service.validateFloorArea(area));
    }

    @Test
    void validateValidFloorAreaAboveThreshold () throws FlooringMasteryDataValidationException {
        BigDecimal area = new BigDecimal(500);
        assertTrue(service.validateFloorArea(area));
        assertDoesNotThrow(()-> service.validateFloorArea(area));
    }

    @Test
    void addOrder() throws FlooringMasteryPersistenceException {
        assertDoesNotThrow(()->service.addOrder("1234", ordersList1.get(0)));
        verify(dao).addOrderToFiles("1234", ordersList1.get(0));
    }

    @Test
    void editOrderName() {
        Order order1 = ordersList1.get(0);
        String name = "Bob";
        when(dao.updateCustomerName(order1, name)).thenReturn(name);
        assertEquals(name, service.editOrderName( name, order1));
        verify(dao).updateCustomerName(order1, name);
    }

    @Test
    void editState() {
        Order order1 = ordersList1.get(0);
        String state = "MT";
        when(dao.updateCustomerState(order1, state)).thenReturn(state);
        assertEquals(state, service.editState(state, order1));
        verify(dao).updateCustomerState(order1, state);
    }

    @Test
    void validateNewProductInvalidInput() {
        Order order1 = ordersList1.get(0);
        String product = "asdfwe";
        when(productDao.listOfProducts()).thenReturn(productList);
        assertThrows(FlooringMasteryDataValidationException.class, ()-> service.validateNewProduct(product, order1));
        verify(productDao, atMost(0)).listOfProducts();
    }

    @Test
    void validateNewProductValidInput(){
        Order order1 = ordersList1.get(0);
        String product = "1";
        when(productDao.listOfProducts()).thenReturn(productList);
        assertDoesNotThrow(()-> service.validateNewProduct(product, order1));
        verify(productDao).listOfProducts();
    }

    @Test
    void ValidateNewProductInvalidNumberNegative(){
        Order order1 = ordersList1.get(0);
        String product = "-1";
        when(productDao.listOfProducts()).thenReturn(productList);
        assertThrows(FlooringMasteryDataValidationException.class, ()-> service.validateNewProduct(product, order1));
        verify(productDao).listOfProducts();
    }

    @Test
    void validateNewProductInvalidNumberTooLarge(){
        Order order1 = ordersList1.get(0);
        String product = "15";
        when(productDao.listOfProducts()).thenReturn(productList);
        assertThrows(FlooringMasteryDataValidationException.class, ()-> service.validateNewProduct(product, order1));
        verify(productDao).listOfProducts();
    }

    @Test
    void editProductType() {
        Product product = productList.get(0);
        Order order = ordersList1.get(0);
        assertDoesNotThrow(()-> service.editProductType(product, order));
        verify(dao).updateProductType(order, product.getProductType());
    }

    @Test
    void exportAllOrders() throws FlooringMasteryPersistenceException {
        assertDoesNotThrow(()-> service.exportAllOrders());
        verify(dao).exportAllOrders();
    }

    @Test
    void editArea() {
        BigDecimal area = new BigDecimal(100);
        Order order = ordersList1.get(0);
        assertDoesNotThrow(()-> service.editArea(area, order));
        verify(dao).updateArea(order, area);
    }

    @Test
    void editOrder() {
        Order order = ordersList1.get(0);
        Product product = productList.get(0);
        Tax tax = taxList.get(0);

        when(taxDao.getTax(order.getState())).thenReturn(taxList.get(0));
        when(productDao.getProduct(order.getProductType())).thenReturn(productList.get(0));

        when(dao.updateTaxRate(order,tax.getTaxRate())).thenReturn(order.getTaxRate());
        when(dao.updateCostPerSquareFoot(order, product.getCostPerSquareFoot())).thenReturn(order.getCostPerSquareFoot());
        when(dao.updateLaborCostPerSquareFoot(order, product.getLaborCostPerSquareFoot())).thenReturn(order.getLaborCostPerSquareFoot());

        when(dao.updateLaborCost(order, order.getLaborCost())).thenReturn(order.getLaborCost());
        when(dao.updateMaterialCost(order, order.getMaterialCost())).thenReturn(order.getMaterialCost());
        when(dao.updateTax(order, order.getTax())).thenReturn(order.getTax());
        when(dao.updateTotal(order, order.getTotal())).thenReturn(order.getTotal());


        assertEquals(order, service.editOrder(order));


    }


    @Test
    void calculateMaterialCost() {
        BigDecimal area = new BigDecimal(100);
        Product product = productList.get(1);

        assertEquals(new BigDecimal(150.00).setScale(2, RoundingMode.HALF_UP), service.calculateMaterialCost(area, product));
    }

    @Test
    void calculateLaborCost() {
        BigDecimal area = new BigDecimal(100);
        Product product = productList.get(1);

        assertEquals(new BigDecimal(400.00).setScale(2, RoundingMode.HALF_UP), service.calculateLaborCost(area, product));
    }

    @Test
    void calculateTax() {
        BigDecimal materialCost = new BigDecimal(100);
        BigDecimal laborCost = new BigDecimal(100);
        BigDecimal taxRate = new BigDecimal(9);

        assertEquals(new BigDecimal(18.00).setScale(2), service.calculateTax(materialCost, laborCost, taxRate));
    }

    @Test
    void calculateTotal() {
        BigDecimal materialCost = new BigDecimal(100);
        BigDecimal laborCost = new BigDecimal(100);
        BigDecimal tax = new BigDecimal(50);

        assertEquals(new BigDecimal(250.00).setScale(2, RoundingMode.HALF_UP), service.calculateTotal(materialCost, laborCost, tax));
    }

    @Test
    void addEditedOrderToFile() throws FlooringMasteryPersistenceException {
        assertDoesNotThrow(()-> service.addEditedOrderToFile(orderMap, "1234", ordersList1.get(1)));
        verify(dao).editOrdersInFile(orderMap, "1234", ordersList1.get(1));
    }

    @Test
    void editAreaInputFromStringInValidInput() {
        assertThrows(FlooringMasteryDataValidationException.class, ()-> service.editAreaInputFromString("qwe12"));
    }

    @Test
    void editAreaInputFromStringValidInput() throws FlooringMasteryDataValidationException {
        assertEquals(new BigDecimal(100), service.editAreaInputFromString("100"));
        assertDoesNotThrow(()->service.editAreaInputFromString("100"));
    }

    @Test
    void removeOrderFromFile() throws FlooringMasteryPersistenceException {
        assertDoesNotThrow(()-> service.removeOrderFromFile(orderMap, "1234", ordersList1.get(1)));
        verify(dao).removeOrderFromFiles(orderMap, "1234", ordersList1.get(1));
    }
}