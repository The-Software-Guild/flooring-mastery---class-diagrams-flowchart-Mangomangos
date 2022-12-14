package com.jwade.dao;

import com.jwade.dto.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class TaxDaoImpl implements TaxDao{

    private Map<String, Tax> taxes = new HashMap<>();

    private static final String TAX_FILE = "src/main/Data/Taxes.txt";

    private static final  String DELIMITER = ",";
    public TaxDaoImpl () throws FlooringMasteryPersistenceException {
        this.taxes = readFile(TAX_FILE);

    }
    @Override
    public Tax unmarshallTaxes(String line) {

        String[] taxTokens = line.split(DELIMITER);
        String taxStateAbbreviation = taxTokens[0];
        String taxStateName = taxTokens[1];
        BigDecimal taxRate = BigDecimal.valueOf(Double.parseDouble(taxTokens[2]));
        
        return new Tax(taxStateAbbreviation, taxStateName, taxRate);
    }

    @Override
    public Map<String, Tax> readFile(String file) throws FlooringMasteryPersistenceException {
        Scanner scanner;
        
        try{
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)
                    )
            );
            
        } catch (FileNotFoundException e) { throw new FlooringMasteryPersistenceException(
                "Could not load tax information from file.");
            
        }
        
        String currentLine;
        Tax currentTax;

        if(scanner.hasNextLine()){
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax = unmarshallTaxes(currentLine);
            taxes.put(currentTax.getStateAbbreviation(), currentTax);
        }
        
        scanner.close();
        return taxes;
    }

    @Override
    public List<Tax> listOfTaxes() {
        return new ArrayList<>(taxes.values());
    }

    @Override
    public Tax getTax(String stateAbbreviation) {
        return taxes.get(stateAbbreviation.toUpperCase());
    }


}
