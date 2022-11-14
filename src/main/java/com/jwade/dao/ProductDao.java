package com.jwade.dao;

import com.jwade.dto.Product;

import java.util.Map;

public interface ProductDao {

    public Product unmarshallProduct(String line);

    public Map<String, Product> readFile(String file);

}
