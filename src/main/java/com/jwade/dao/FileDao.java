package com.jwade.dao;

import com.jwade.dto.Order;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FileDao {

    public Order unmarshallOrder(String line);

    public String marshallOrder(Order order);

    void addNewOrderToFile(String orderDate, Order order) throws FlooringMasteryPersistenceException;

    public Map<Integer, Order> readFiles(String file) throws FlooringMasteryPersistenceException;

    public List<File> listFiles(String path);

    public Map<Integer, Order> readFile(String file) throws FlooringMasteryPersistenceException;

    void updateOrderInFile(ArrayList<Order> orders, String orderDate) throws FlooringMasteryPersistenceException;

    Boolean doesFileExist(String orderDate);

}
