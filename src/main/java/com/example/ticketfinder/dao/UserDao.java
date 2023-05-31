package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.User;

public interface UserDao {

    User findByEmail(String username);

    void addUser(User user);


}
