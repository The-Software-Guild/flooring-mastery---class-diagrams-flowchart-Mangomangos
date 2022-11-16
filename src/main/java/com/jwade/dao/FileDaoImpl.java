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

    private String HEADER_TEXT = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";

    @Override
    public Order unmarshallOrder(String line) {

        String [] orderTokens = line.split(DELIMITER);
        int orderNumber = Integer.parseInt(orderTokens[0]);
        String customerName = orderTokens[1];
        String state = orderTokens[2];
        BigDecimal taxRate = new BigDecimal((orderTokens[3]));
        String productType = orderTokens[4];
        BigDecimal area = new BigDecimal(orderTokens[5]);
        BigDecimal costPerSquareFoot = new BigDecimal(orderTokens[6]);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(orderTokens[7]);
        BigDecimal materialCost = new BigDecimal(orderTokens[8]);
        BigDecimal laborCost = new BigDecimal(orderTokens[9]);
        BigDecimal tax = new BigDecimal(orderTokens[10]);
        BigDecimal total = new BigDecimal(orderTokens[11]);

        return new Order(orderNumber, customerName, state, productType, taxRate, area,
                costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, total);


    }

    @Override
    public String marshallOrder(Order order) {
        return order.getOrderNumber() + DELIMITER + order.getCustomerName() + DELIMITER + order.getState() +
                DELIMITER + order.getTaxRate().toString() + DELIMITER + order.getProductType() + DELIMITER +
                order.getArea().toString() + DELIMITER + order.getCostPerSquareFoot().toString() + DELIMITER +
                order.getLaborCostPerSquareFoot().toString() + DELIMITER + order.getMaterialCost().toString() +
                DELIMITER + order.getLaborCost().toString() + DELIMITER + order.getTax().toString() +
                DELIMITER + order.getTotal().toString();
    }

    @Override
    public void addNewOrderToFile(String orderDate, Order order) {

        PrintWriter out = null;

        File orderFile = new File("Orders/orders_" + orderDate + ".txt");

        if (orderFile.isFile()){
            try {
                out = new PrintWriter( new FileWriter(orderFile));
            } catch (IOException e){
            }

        } else {
            try {
                orderFile.createNewFile();
                out = new PrintWriter(new FileWriter(orderFile));
            } catch (IOException e){

            }
            out.println(HEADER_TEXT);

        }

        String oderAsText = marshallOrder(order);
        out.println(oderAsText);
        out.flush();
        out.close();

    }

    @Override
    public void updateOrderInFile(ArrayList<Order> orders, String orderDate) {

        PrintWriter out = null;

        String fileName = "Orders/orders_" + orderDate + ".txt";

        try {
            out = new PrintWriter( new FileWriter(fileName));
        } catch (IOException e) {

        }

        String orderAsText;
        for (Order currentOrder: orders){
            orderAsText = marshallOrder(currentOrder);
            out.println(orderAsText);
            out.flush();
        }
        out.close();

    }

    @Override
    public Boolean doesFileExist(String orderDate) {
        File f = new File("Orders/orders_" + orderDate + ".txt");
        return f.isFile();
    }


    @Override
    public Map<Integer, Order> readFile(String orderDate) {

        Map<Integer, Order> dayOrders = new HashMap<>();

        Scanner scanner = null;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader("Orders/orders_" + orderDate + ".txt")
                    )
            );
        } catch (FileNotFoundException e) {
        }

        String currentLine;
        Order currentOrder;

        //skip header text
        if(scanner.hasNextLine()){
            scanner.nextLine();
        }

        //read rest of file
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
