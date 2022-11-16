package com.jwade;

import com.jwade.controller.FlooringMasteryController;
import com.jwade.dao.*;
import com.jwade.service.FlooringMasteryDataValidationException;
import com.jwade.service.FlooringMasteryServiceImpl;
import com.jwade.ui.FlooringMasteryView;
import com.jwade.ui.UserIO;
import com.jwade.ui.UserIOImpl;

public class Main {
    public static void main(String[] args) throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException {
        UserIO myIO = new UserIOImpl();
        ProductDaoImpl productDao = new ProductDaoImpl();
        TaxDaoImpl taxDao = new TaxDaoImpl();
        FlooringMasteryDaoImpl myDao = new FlooringMasteryDaoImpl();
        FlooringMasteryView myView = new FlooringMasteryView(myIO);
        FlooringMasteryServiceImpl myService = new FlooringMasteryServiceImpl(productDao, taxDao, myDao);
        FlooringMasteryController controller = new FlooringMasteryController(myView, myService);
        controller.run();
    }
}