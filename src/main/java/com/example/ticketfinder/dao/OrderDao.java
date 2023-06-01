package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Order;
import com.example.ticketfinder.entities.User;

import java.util.List;

public interface OrderDao {

    List<Order> getAllUsersOrders(User user);

    void addOrder(Order order, int userId);

    void deleteOrder(int orderID);
}
