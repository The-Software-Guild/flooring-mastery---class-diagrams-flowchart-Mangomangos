package com.jwade.dao;

import com.jwade.dto.Order;

import java.util.ArrayList;
import java.util.Map;

public interface FileDao {

    public Order unmarshallOrder(String line);

    public String marshallOrder(Order order);

    void writeFile(ArrayList<Order> orders);

    public Map<String, Order> readFile(String file);

}
