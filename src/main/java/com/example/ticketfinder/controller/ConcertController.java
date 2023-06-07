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
import java.util.HashMap;
import java.util.List;

@Controller
public class ConcertController {

    // inject dependencies
    @Autowired
    ConcertDao concertDao;

    /**
     *
     * @param model
     * @return
     * Returns all concerts within the database
     */
    @GetMapping("concerts")
    public String displayConcerts(Model model) {

        List<Concert> concerts = concertDao.getAllConcerts();
        model.addAttribute("concerts", concerts);

        return "concerts";

    }

    /**
     *
     * @param request
     * @param model
     * @return
     * Allows user to input search parameters, then
     * appropriate concert objects are returned
     */
    @GetMapping("concerts-by-search")
    public String displayConcertsBySearch(Model model, HttpServletRequest request) {

        // retrieve user's search request
        String search = request.getParameter("search");
        // populate the list of concerts filtered by user search
        List<Concert> concerts = concertDao.findConcertsBySearch(search);

        // populate model for frontend
        model.addAttribute("concerts", concerts);
        model.addAttribute("search", search);

        /* map the number of seats left for each concert to the respective concert instance
           to be able to show on the frontend */
        // initialise map
        HashMap<Concert, Integer> seatNoMap = new HashMap<Concert, Integer>();
        // iterate trough each concert
        for (Concert concert : concerts) {
            // get and save number of seats for each concert
            List<Float> seatsNo = concertDao.seatsLeft(concert.getId());
            float sum = 0;
            // number of seats left us the sum of seats left in for every seat type
            for (Float entry : seatsNo) {
                sum += entry;
            }
            int sumInt = (int) sum;
            seatNoMap.put(concert, sumInt);
        }
        // pass mapping to model
        model.addAttribute("seatNoMap", seatNoMap);

        return "concerts";

    }

    /**
     *
     * @param request
     * @param model
     * @return
     * Allows user to view an individual concert
     */
    @GetMapping("view-concert")
    public String viewConcert(HttpServletRequest request, Model model) {

        // retrieve the id of the concert to perform the correct read
        // from database
        int iD = Integer.parseInt(request.getParameter("id"));

        // find the concert data
        Concert concert = concertDao.getConcertById(iD);

        // add the concert to the model
        model.addAttribute("concert", concert);

        return "view-concert";
    }


    @GetMapping("edit-concert")
    public String toEditPage(HttpServletRequest request, Model model) {

        int iD = Integer.parseInt(request.getParameter("id"));
        Concert concert = concertDao.getConcertById(iD);
        model.addAttribute("concertEditAdmin", concert);
        model.addAttribute("editConcId", iD);

        return "edit-concert";
    }


    /**
     *
     * @param request
     * @param model
     * @return
     * @throws ParseException
     * Method used for admin CRUD feature: editing the details of a concert
     */
    @Transactional
    @PostMapping("edit-concert")
    public String editConcert(HttpServletRequest request, Model model) throws ParseException {

        // initialise variables from front-end request
        int iD = Integer.parseInt(request.getParameter("id"));
        String artist = request.getParameter("artist");
        String venue = request.getParameter("venue");
        Date date = new SimpleDateFormat("yyy-MM-dd").parse(request.getParameter("date"));

        // initialise new concert instance & store variables in it
        Concert newConcert = new Concert();
        newConcert.setArtist(artist);
        newConcert.setVenue(venue);
        newConcert.setConcertDate(date);

        // perform CRUD editing operation via DAO
        concertDao.updateConcert(newConcert, iD);

        // re-load concert data with updated entry
        List<Concert> concerts = concertDao.getAllConcerts();
        model.addAttribute("concerts", concerts);

        // re-direct to admin concert list page
        return "data-list-admin";
    }

}