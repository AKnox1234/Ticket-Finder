package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.OrderDaoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    @Autowired
    OrderDaoDB ticketDaoDB;
}
