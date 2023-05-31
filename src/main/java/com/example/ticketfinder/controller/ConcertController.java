package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.ConcertDao;
import com.example.ticketfinder.entities.Concert;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ConcertController {

    @Autowired
    ConcertDao concertDao;

    @GetMapping("concerts")
    public String displayConcerts(Model model) {
        List<Concert> concerts = concertDao.getAllConcerts();
        model.addAttribute("concerts", concerts);

        return "concerts";

    }

    @GetMapping("concertsBySearch")
    public String displayConcertsBySearch(Model model, HttpServletRequest request) {
        String search = request.getParameter("search");
        List<Concert> concerts = concertDao.findConcertsBySearch(search);
        model.addAttribute("concerts", concerts);
        model.addAttribute("search", search);

        return "concerts";

    }

    @GetMapping("viewConcert")
    public String viewConcert(HttpServletRequest request, Model model) {
        int iD = Integer.parseInt(request.getParameter("id"));
        Concert concert = concertDao.getConcertById(iD);
        model.addAttribute("concert", concert);

        return "viewConcert";

    }

}
