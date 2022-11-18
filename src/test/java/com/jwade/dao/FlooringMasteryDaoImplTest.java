package com.jwade.dao;

import com.jwade.dto.Order;
import com.jwade.service.FlooringMasteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FlooringMasteryDaoImplTest {

    private FlooringMasteryDao dao;
    private FileDao filedao;

    private Map<Integer, Order> orderMap = new HashMap<>();

    private List<Order> ordersList1 = new ArrayList<>();

    @BeforeEach
    void setUp() throws FlooringMasteryPersistenceException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContextTest.xml");
        filedao = ctx.getBean("fileDaoMock", FileDao.class);
        dao = ctx.getBean("dao", FlooringMasteryDao.class);

        Order order1 = new Order(1, "Customer A", "California", "Carpet", new BigDecimal(25), new BigDecimal(100),
                new BigDecimal(2.15), new BigDecimal(4.25), new BigDecimal(215), new BigDecimal(425),
                new BigDecimal(160), new BigDecimal(800));

        Order order2 = new Order(2, "Customer B", "Alaska", "Tile", new BigDecimal(10), new BigDecimal(200),
                new BigDecimal(1.50), new BigDecimal(4.00), new BigDecimal(300), new BigDecimal(800),
                new BigDecimal(110), new BigDecimal(1210));

        ordersList1.add(order1);
        ordersList1.add(order2);

        orderMap.put(order1.getOrderNumber(), order1);
        orderMap.put(order2.getOrderNumber(), order2);

    }


    @Test
    void getOrder() throws FlooringMasteryPersistenceException {

        Integer orderNumber = 1;

        String orderDate = "12122022";
        when(filedao.doesFileExist(orderDate)).thenReturn(true);
        when(filedao.generateFilePathName(orderDate)).thenReturn("Yes");
        when(filedao.readFile("Yes")).thenReturn(orderMap);

        assertEquals(orderMap.get(1), dao.getOrder(orderDate, orderNumber));


    }

    @Test
    void listAllOrders() throws FlooringMasteryPersistenceException {

        Order order1 = new Order(1, "Customer A", "California", "Carpet", new BigDecimal(25), new BigDecimal(100),
                new BigDecimal(2.15), new BigDecimal(4.25), new BigDecimal(215), new BigDecimal(425),
                new BigDecimal(160), new BigDecimal(800));

        Order order2 = new Order(2, "Customer B", "Alaska", "Tile", new BigDecimal(10), new BigDecimal(200),
                new BigDecimal(1.50), new BigDecimal(4.00), new BigDecimal(300), new BigDecimal(800),
                new BigDecimal(110), new BigDecimal(1210));

        Map<Integer, Order> orders = new HashMap<>();

        orders.put(1, order1);
        orders.put(2, order2);
        when(filedao.readAllFiles(any())).thenReturn(orders);
        dao.loadFile();
        assertTrue(dao.listAllOrders().contains(order1));
        assertTrue(dao.listAllOrders().contains(order2));

    }

    @Test
    void addOrderToFiles() throws FlooringMasteryPersistenceException {
        String orderDate = "12122022";
        Order order1 = ordersList1.get(0);
        assertDoesNotThrow(()-> dao.addOrderToFiles(orderDate, order1));
        verify(filedao).addNewOrderToFile(orderDate, order1);
    }

    @Test
    void removeOrderFromFiles() throws FlooringMasteryPersistenceException {
        String orderDate = "12122022";
        Order order1 = orderMap.get(1);
        assertDoesNotThrow(()-> dao.removeOrderFromFiles(orderMap, orderDate, order1));
        verify(filedao).updateOrderInFile(new ArrayList<>(orderMap.values()), orderDate);
        assertTrue(orderMap.containsKey(2));
        assertFalse(orderMap.containsKey(1));
    }

    @Test
    void editOrdersInFile() throws FlooringMasteryPersistenceException {
        Map<Integer, Order> orders = orderMap;
        String orderDate = "12122022";
        Order order3 = new Order(3, "Customer C", "Virginia", "Wood", new BigDecimal(15), new BigDecimal(100),
                new BigDecimal(2.00), new BigDecimal(5.00), new BigDecimal(200), new BigDecimal(1000),
                new BigDecimal(180), new BigDecimal(1380));
        assertDoesNotThrow(()-> dao.editOrdersInFile(orders, orderDate, order3));
        verify(filedao).updateOrderInFile(new ArrayList<>(orders.values()), orderDate);
        assertTrue(orderMap.containsKey(3));

    }

    @Test
    void mapDayOrders() throws FlooringMasteryPersistenceException {
        String orderDate = "12122022";
        String testPath = "Orders/order_12122022.txt";
        when(filedao.generateFilePathName(orderDate)).thenReturn(testPath);
        when(filedao.readFile(testPath)).thenReturn(orderMap);
        assertEquals(orderMap, dao.mapDayOrders(orderDate));
    }

    @Test
    void orderList() {
        Map<Integer, Order> orders = orderMap;
        assertEquals(new ArrayList<>(orders.values()), dao.orderList(orders));
    }

    @Test
    void updateCustomerName() {
        Order order1 = ordersList1.get(0);
        String name = "Bob";
        assertEquals(name, dao.updateCustomerName(order1, name));
    }

    @Test
    void updateCustomerState() {
        Order order1 = ordersList1.get(0);
        String state = "OH";
        assertEquals(state, dao.updateCustomerState(order1, state));
    }

    @Test
    void updateTaxRate() {
        Order order1 = ordersList1.get(0);
        BigDecimal taxRate = new BigDecimal(25);
        assertEquals(taxRate, dao.updateTaxRate(order1, taxRate));
    }

    @Test
    void updateProductType() {
        Order order1 = ordersList1.get(0);
        String productType = "Wood";
        assertEquals(productType, dao.updateProductType(order1, productType));
    }

    @Test
    void updateArea() {
        Order order1 = ordersList1.get(0);
        BigDecimal area = new BigDecimal(100);
        assertEquals(area, dao.updateArea(order1, area));
    }

    @Test
    void updateCostPerSquareFoot() {
        Order order1 = ordersList1.get(0);
        BigDecimal cost = new BigDecimal(5.25);
        assertEquals(cost, dao.updateCostPerSquareFoot(order1, cost));
    }

    @Test
    void updateLaborCostPerSquareFoot() {
        Order order1 = ordersList1.get(0);
        BigDecimal labor = new BigDecimal(6);
        assertEquals(labor, dao.updateLaborCostPerSquareFoot(order1, labor));
    }

    @Test
    void updateMaterialCost() {
        Order order1 = ordersList1.get(0);
        BigDecimal materialCost = new BigDecimal(825.35);
        assertEquals(materialCost, dao.updateMaterialCost(order1, materialCost));
    }

    @Test
    void updateLaborCost() {
        Order order1 = ordersList1.get(0);
        BigDecimal laborCost = new BigDecimal(1000.25);
        assertEquals(laborCost, dao.updateLaborCost(order1, laborCost));
    }

    @Test
    void updateTax() {
        Order order1 = ordersList1.get(0);
        BigDecimal tax = new BigDecimal(42.42);
        assertEquals(tax, dao.updateTax(order1, tax));
    }

    @Test
    void updateTotal() {
        Order order1 = ordersList1.get(0);
        BigDecimal total = new BigDecimal(123000.98);
        assertEquals(total, dao.updateTotal(order1, total));
    }

    @Test
    void generateCurrentMaxOrderNumber() throws FlooringMasteryPersistenceException {
        when(filedao.readAllFiles(any())).thenReturn(orderMap);
        dao.loadFile();
        assertEquals(2, dao.generateCurrentMaxOrderNumber());
    }

    @Test
    void exportAllOrders() throws FlooringMasteryPersistenceException {
        assertDoesNotThrow(()->dao.exportAllOrders());
        verify(filedao).exportFiles();
    }
}