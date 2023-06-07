package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.exception.TicketFinderDataPersistenceException;

import java.util.List;

public interface UserDao {

    User findByEmail(String username);
  
    void addUser(User user) throws TicketFinderDataPersistenceException;

    List<User> getAllUsers();


}
