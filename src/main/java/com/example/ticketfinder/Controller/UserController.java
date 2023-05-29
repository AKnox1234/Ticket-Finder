package com.example.ticketfinder.Controller;

import com.example.ticketfinder.DAO.TicketDaoDB;
import com.example.ticketfinder.DAO.UserDaoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    UserDaoDB userDaoDB;
}
