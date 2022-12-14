package com.jwade.dto;

import java.math.BigDecimal;

public class Tax {

    private String stateAbbreviation;

    private String stateName;

    private BigDecimal taxRate;

    public Tax(){}

    public Tax (String abbreviation, String name, BigDecimal taxRate){
        this.stateAbbreviation = abbreviation;
        this.stateName = name;
        this.taxRate = taxRate;
    }

    @Override
    public String toString() {
        return stateAbbreviation + ", " + stateName + ", " + taxRate;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}
