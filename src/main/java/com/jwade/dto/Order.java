package com.jwade.dto;

import java.math.BigDecimal;

public class Order {

    private int orderNumber;
    private String customerName;
    private String state;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;

    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;


    public Order(String name, String state, String productType, BigDecimal area){
        this.customerName = name;
        this.state = state;
        this.productType = productType;
        this.area = area;

    }

    public Order(int orderNumber, String name, String state, String productType,
                 BigDecimal taxRate, BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot,
                 BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total){
        this.orderNumber = orderNumber;
        this.customerName = name;
        this.state = state;
        this.productType = productType;
        this.taxRate = taxRate;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order Number: " + orderNumber + "\nName: " + customerName + "\nState: " + state + ", Tax Rate:" + taxRate +
                "\nProduct Type: " + productType  + ", Area: " + area +
                "/sqFt \nMaterial Cost: $" + costPerSquareFoot + "/sqFt, Total Material Cost: $" + materialCost +
                "\nLabor Cost: $" + laborCostPerSquareFoot + "/sqFt, Total Labor Cost: $" + laborCost +
                "\nTotal Tax: $" + tax + ", Grand Total: $" + total;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
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

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
