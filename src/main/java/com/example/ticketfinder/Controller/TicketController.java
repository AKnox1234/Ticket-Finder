package com.example.ticketfinder.Controller;

import com.example.ticketfinder.DAO.TicketDAO;
import com.example.ticketfinder.DAO.TicketDaoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TicketController {

    @Autowired
    TicketDaoDB ticketDaoDB;
}
