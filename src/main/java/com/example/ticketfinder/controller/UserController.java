package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.UserDaoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    UserDaoDB userDaoDB;
}
