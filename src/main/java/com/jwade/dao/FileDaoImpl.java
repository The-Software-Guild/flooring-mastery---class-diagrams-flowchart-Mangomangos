package com.jwade.dao;

import com.jwade.dto.Order;
import com.sun.source.tree.TryTree;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileDaoImpl implements FileDao{

    private String pathName = "Orders";

    private static final String DELIMITER = ",";

    private  Map<Integer, Order> orders = new HashMap<>();

    @Override
    public Order unmarshallOrder(String line) {
        String [] orderTokens = line.split(DELIMITER);
        int orderNumber = Integer.parseInt(orderTokens[0]);
        String customerName = orderTokens[1];
        String state = orderTokens[2];
        BigDecimal taxRate = BigDecimal.valueOf(Double.parseDouble(orderTokens[3]));
        String productType = orderTokens[4];
        BigDecimal area = BigDecimal.valueOf(Double.parseDouble(orderTokens[5]));
        BigDecimal costPerSquareFoot = BigDecimal.valueOf(Double.parseDouble(orderTokens[6]));
        BigDecimal laborCostPerSquareFoot = BigDecimal.valueOf(Double.parseDouble(orderTokens[7]));
        BigDecimal materialCost = BigDecimal.valueOf(Double.parseDouble(orderTokens[8]));
        BigDecimal laborCost = BigDecimal.valueOf((Double.parseDouble(orderTokens[9])));
        BigDecimal tax = BigDecimal.valueOf(Double.parseDouble(orderTokens[10]));
        BigDecimal total = BigDecimal.valueOf(Double.parseDouble(orderTokens[11]));

        return new Order(orderNumber, customerName, state, taxRate, productType, area,
                costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, total);


    }

    @Override
    public String marshallOrder(Order order) {
        return order.getOrderNumber() + DELIMITER + order.getCustomerName() + DELIMITER + order.getState() +
                DELIMITER + order.getTaxRate() + DELIMITER + order.getProductType() + DELIMITER + order.getArea() +
                DELIMITER + order.getCostPerSquareFoot() + DELIMITER + order.getLaborCostPerSquareFoot() +
                DELIMITER + order.getMaterialCost() + DELIMITER + order.getLaborCost() + DELIMITER + order.getTax() +
                DELIMITER + order.getTotal();
    }

    @Override
    public void addNewOrderToFile(String orderDate, Order order) {

        String fileName = "orders_" + orderDate + ".txt";

        PrintWriter out = null;

        File orderFile = new File("Orders/" + fileName);

        try {
            orderFile.createNewFile();
            out = new PrintWriter( new FileWriter(fileName));
        } catch (IOException e) {
        }

        String oderAsText = marshallOrder(order);
        out.println(oderAsText);
        out.flush();
        out.close();

    }

    @Override
    public void updateOrderInFile(Order newOrder, Order oldOrder, String orderDate) {

        PrintWriter out = null;

        String fileName = "Orders/order_" + orderDate + ".txt";

        Map<Integer, Order> dayOrders = readFiles(fileName);

        dayOrders.replace(oldOrder.getOrderNumber(), oldOrder, newOrder);

        File orderFile = new File(fileName);

        orderFile.delete();

        try {
            orderFile.createNewFile();
            out = new PrintWriter( new FileWriter(fileName));
        } catch (IOException e){

        }

        String orderAsText;
        for (Order currentOrder : new ArrayList<>(dayOrders.values())){
            orderAsText = marshallOrder(currentOrder);
            out.println(orderAsText);
            out.flush();
        }
        out.close();

    }

    @Override
    public void updateRemovedOrderInFile(String orderDate, Order removedOrder) {

        PrintWriter out = null;

        String fileName = "Orders/order_" + orderDate + ".txt";

        Map<Integer, Order> dayOrders = readFiles(fileName);

        dayOrders.remove(removedOrder.getOrderNumber());

        File orderFile = new File(fileName);

        orderFile.delete();

        try {
            orderFile.createNewFile();
            out = new PrintWriter( new FileWriter(fileName));
        } catch (IOException e){

        }

        String orderAsText;
        for (Order currentOrder : new ArrayList<>(dayOrders.values())){
            orderAsText = marshallOrder(currentOrder);
            out.println(orderAsText);
            out.flush();
        }
        out.close();

    }



    @Override
    public Map<Integer, Order> readFile(String file) {

        Map<Integer, Order> dayOrders = new HashMap<>();

        Scanner scanner = null;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(file)
                    )
            );
        } catch (FileNotFoundException e) {
        }

        String currentLine;
        Order currentOrder;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            dayOrders.put(currentOrder.getOrderNumber(), currentOrder);
        }

        scanner.close();
        return  dayOrders;
    }


    @Override
    public List<File> listFiles(String path) {

        List<File> filesInFolder = new ArrayList<>();

        try {
            filesInFolder = Files.walk(Paths.get(pathName))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
        }

        return filesInFolder;
    }

    @Override
    public Map<Integer, Order> readFiles(String path) {
        for (File file : listFiles(pathName)) {
            orders.putAll(readFile(file.getName()));
        }
        return orders;
    }



}
