package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Order;
import com.example.ticketfinder.entities.User;

import java.util.List;

public interface OrderDao {

    public List<Order> getAllUsersOrders(User user);
    public void addOrder(Order order, int userId);
}
