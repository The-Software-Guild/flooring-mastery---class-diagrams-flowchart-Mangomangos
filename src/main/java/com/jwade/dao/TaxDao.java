package com.jwade.dao;

import com.jwade.dto.Tax;

import java.util.Map;

public interface TaxDao {

    public Tax unmarshallTaxes(String line);

    public Map<String, Tax> readFile(String file);
}

