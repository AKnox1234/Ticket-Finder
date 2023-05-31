package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.User;

public interface UserDao {

    public User findByEmail(String username);

    public void addUser(User user);


}
