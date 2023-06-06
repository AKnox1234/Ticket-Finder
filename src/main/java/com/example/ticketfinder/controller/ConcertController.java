package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.ConcertDao;
import com.example.ticketfinder.entities.Concert;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        //model.addAttribute("currConcId", iD);

        return "viewConcert";
    }

    @GetMapping("editConcertPage")
    public String toEditPage(HttpServletRequest request, Model model) {

        int Id = Integer.parseInt(request.getParameter("id"));
        Concert concert = concertDao.getConcertById(Id);
        model.addAttribute("concertEditAdmin", concert);
        model.addAttribute("editConcId", Id);

        return "editConcertPage";
    }

    @Transactional
    @PostMapping("editConcert")
    public String editConcert(HttpServletRequest request, Model model) throws ParseException {

        int iD = Integer.parseInt(request.getParameter("id"));
        String artist = request.getParameter("artist");
        String venue = request.getParameter("venue");
        Date date = new SimpleDateFormat("yyy-MM-dd").parse(request.getParameter("date"));

        Concert newConcert = new Concert();

        newConcert.setArtist(artist);
        newConcert.setVenue(venue);
        newConcert.setConcertDate(date);

        concertDao.updateConcert(newConcert, iD);

        List<Concert> concerts = concertDao.getAllConcerts();
        model.addAttribute("concerts", concerts);

        return "dataListAdmin";
    }

    @GetMapping("seatsLeft")
    public String ticketNumber(HttpServletRequest request, Model model) {
        int iD = Integer.parseInt(request.getParameter("id"));
        List<Float> seatsLeft = concertDao.seatsLeft(iD);
        int sLeft = (int) (seatsLeft.get(0) + seatsLeft.get(1));
        model.addAttribute("seatsLeft", sLeft);

        return "seatsLeft";
    }

    /*
    @GetMapping("viewConcertWithPrice")
    public String viewConcertWithPrice(HttpServletRequest request, Model model) {
        int iD = model.getAttribute("currConcId");
        Concert concert = concertDao.getConcertById(iD);
        model.addAttribute("concert", concert);

        return "viewConcert";
    }*/
}