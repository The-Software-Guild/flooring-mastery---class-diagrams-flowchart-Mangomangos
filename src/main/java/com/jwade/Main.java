package com.jwade;

import com.jwade.controller.FlooringMasteryController;
import com.jwade.dao.*;
import com.jwade.service.FlooringMasteryDataValidationException;
import com.jwade.service.FlooringMasteryServiceImpl;
import com.jwade.ui.FlooringMasteryView;
import com.jwade.ui.UserIO;
import com.jwade.ui.UserIOImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) throws FlooringMasteryPersistenceException {
//        UserIO myIO = new UserIOImpl();
//        ProductDaoImpl productDao = new ProductDaoImpl();
//        TaxDaoImpl taxDao = new TaxDaoImpl();
//        FlooringMasteryDaoImpl myDao = new FlooringMasteryDaoImpl();
//        FlooringMasteryView myView = new FlooringMasteryView(myIO);
//        FlooringMasteryServiceImpl myService = new FlooringMasteryServiceImpl(productDao, taxDao, myDao);
//        FlooringMasteryController controller = new FlooringMasteryController(myView, myService);
//        controller.run();

        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext,xml");
        FlooringMasteryController controller = ctx.getBean("controller", FlooringMasteryController.class);
        controller.run();
    }
}