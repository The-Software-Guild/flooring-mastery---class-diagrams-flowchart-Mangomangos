package com.jwade.dao;

import com.jwade.dto.Order;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FileDao {

    public Order unmarshallOrder(String line);

    public String marshallOrder(Order order);

    void writeFile(String orderDate, Order order);

    public Map<Integer, Order> readFile(String file);

    public List<File> listFiles(String path);

}
