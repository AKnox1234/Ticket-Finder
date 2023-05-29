package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.TicketDaoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TicketController {

    @Autowired
    TicketDaoDB ticketDaoDB;
}
