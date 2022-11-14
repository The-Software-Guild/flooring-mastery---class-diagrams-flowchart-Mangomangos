package com.jwade.dao;

import com.jwade.dto.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProductDaoImpl implements ProductDao{

    private Map<String, Product> products = new HashMap<>();

    private static final String DELIMITER = ",";

    private static final String PRODUCT_FILE = "Products.txt";
    @Override
    public Product unmarshallProduct(String line) {
        String[] productTokens = line.split(DELIMITER);
        String productType = productTokens[0];
        BigDecimal productCostPerSquareFoot = BigDecimal.valueOf(Double.parseDouble(productTokens[1]));
        BigDecimal productLaborCostPerSquareFoot = BigDecimal.valueOf(Double.parseDouble(productTokens[2]));

        return new Product(productType, productCostPerSquareFoot, productLaborCostPerSquareFoot);

    }

    @Override
    public Map<String, Product> readFile(String file) {
        
        Scanner scanner = null;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)
                    )
            );
        } catch (FileNotFoundException e){
        }

        String currentLine;
        Product currentProduct;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(), currentProduct);
        }

        scanner.close();
        return  products;
    }

}
