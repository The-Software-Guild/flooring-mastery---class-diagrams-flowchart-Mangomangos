package com.jwade.dao;

import com.jwade.dto.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TaxDaoImpl implements TaxDao{

    private Map<String, Tax> taxes = new HashMap<>();

    private static final String TAX_FILE = "taxes.txt";

    private static final  String DELIMITER = ",";
    @Override
    public Tax unmarshallTaxes(String line) {

        String[] taxTokens = line.split(DELIMITER);
        String taxStateAbbreviation = taxTokens[0];
        String taxStateName = taxTokens[1];
        BigDecimal taxRate = BigDecimal.valueOf(Double.parseDouble(taxTokens[2]));
        
        return new Tax(taxStateAbbreviation, taxStateName, taxRate);
    }

    @Override
    public Map<String, Tax> readFile(String file) {
        Scanner scanner = null;
        
        try{
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)
                    )
            );
            
        } catch (FileNotFoundException e) {
            
        }
        
        String currentLine;
        Tax currentTax;
        
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax = unmarshallTaxes(currentLine);
            taxes.put(currentTax.getStateAbbreviation(), currentTax);
        }
        
        scanner.close();
        return taxes;
    }
}
