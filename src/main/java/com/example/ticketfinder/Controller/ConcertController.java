package com.example.ticketfinder.Controller;

import com.example.ticketfinder.DAO.ConcertDaoDB;
import com.example.ticketfinder.Entities.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ConcertController {

    @Autowired
    ConcertDaoDB concertDaoDB;


    @GetMapping("concerts")
    public String displayConcerts(Model model) {
        List<Concert> concerts = concertDaoDB.getAllConcerts();
        model.addAttribute("concerts", concerts);

        return "concerts";

    }

}
