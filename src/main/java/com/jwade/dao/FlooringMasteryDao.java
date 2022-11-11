package com.jwade.dao;

import com.jwade.dto.Order;

import java.util.List;

public interface FlooringMasteryDao {

    Order getOrder(String order);

    List<Order> listAllOrders();

    Order addOrder(Order order);

    Order removeOrder(Order order);

    Order editOrder(Order order);


}
