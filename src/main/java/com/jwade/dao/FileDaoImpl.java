package com.jwade.dao;

import com.jwade.dto.Order;

import java.util.ArrayList;
import java.util.Map;

public class FileDaoImpl implements FileDao{
    @Override
    public Order unmarshallOrder(String line) {
        return null;
    }

    @Override
    public String marshallOrder(Order order) {
        return null;
    }

    @Override
    public void writeFile(ArrayList<Order> orders) {

    }

    @Override
    public Map<String, Order> readFile(String file) {
        return null;
    }
}
