package com.jwade.dao;

import com.jwade.dto.Order;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileDao {

    public Order unmarshallOrder(String line);

    public String marshallOrder(Order order);

    void addNewOrderToFile(String orderDate, Order order);

    public Map<Integer, Order> readFiles(String file);

    public List<File> listFiles(String path);

    public Map<Integer, Order> readFile(String file);

    void updateRemovedOrderInFile (String orderDate, Order removedOrder);

    void updateOrderInFile(Order newOrder, String orderDate);

}
