package com.jwade.dao;

import com.jwade.dto.Tax;

import java.util.List;
import java.util.Map;

public interface TaxDao {

    public Tax unmarshallTaxes(String line);

    public Map<String, Tax> readFile(String file);

    public List<Tax> listOfTaxes();

    public Tax getTax(String stateAbbreviation);
}

