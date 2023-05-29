package com.example.ticketfinder.Controller;

import com.example.ticketfinder.DAO.ConcertDaoDB;
import com.example.ticketfinder.Entities.Concert;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("concertsBySearch")
    public String displayConcertsBySearch(Model model, HttpServletRequest request) {
        String search = request.getParameter("search");
        List<Concert> concerts = concertDaoDB.findConcertsBySearch(search);
        model.addAttribute("concerts", concerts);

        return "concerts";

    }

    @GetMapping("viewConcert")
    public String viewConcert(Integer iD, Model model) {
        Concert concert = concertDaoDB.getConcertById(iD);
        model.addAttribute("concert", concert);

        return "viewConcert";

    }

}
