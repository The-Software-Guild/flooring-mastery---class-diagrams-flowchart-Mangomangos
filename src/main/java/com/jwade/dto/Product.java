package com.jwade.dto;

import java.math.BigDecimal;

public class Product {

    private String productType;

    private BigDecimal costPerSquareFoot;

    private BigDecimal laborCostPerSquareFoot;

    public Product (){}

    public Product (String type, BigDecimal cost, BigDecimal labor){
        this.productType = type;
        this.costPerSquareFoot = cost;
        this.laborCostPerSquareFoot = labor;
    }

    @Override
    public String toString() {
        return productType + ", " + "$" + costPerSquareFoot + ", " + "$" + laborCostPerSquareFoot;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }
}
