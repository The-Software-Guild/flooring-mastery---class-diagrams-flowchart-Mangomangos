package com.jwade.dao;

import com.jwade.dto.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoImpl implements ProductDao{

    private Map<String, Product> products = new HashMap<>();

    private static final String DELIMITER = ",";

    private static final String PRODUCT_FILE = "src/main/Data/Products.txt";
    @Override
    public Product unmarshallProduct(String line) {
        String[] productTokens = line.split(DELIMITER);
        String productType = productTokens[0];
        BigDecimal productCostPerSquareFoot = BigDecimal.valueOf(Double.parseDouble(productTokens[1]));
        BigDecimal productLaborCostPerSquareFoot = BigDecimal.valueOf(Double.parseDouble(productTokens[2]));

        return new Product(productType, productCostPerSquareFoot, productLaborCostPerSquareFoot);

    }

    @Override
    public Map<String, Product> readFile(String file) throws FlooringMasteryPersistenceException {
        
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)
                    )
            );
        } catch (FileNotFoundException e){ throw new FlooringMasteryPersistenceException(
                "Could not load product information from file."
        );
        }

        String currentLine;
        Product currentProduct;

        if(scanner.hasNextLine()){
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(), currentProduct);
        }

        scanner.close();
        return  products;
    }

    @Override
    public List<Product> listOfProducts() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Product getProduct(String productType) {
        return products.get(productType);
    }

}
