package com.jwade.dao;

import com.jwade.dto.Order;
import com.jwade.dto.Product;

import java.util.Map;

public interface ProductDao {

    public Order unmarshallOrder(String line);

    public Map<String, Product> readFile(String file);

}
