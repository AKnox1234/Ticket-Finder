package com.example.ticketfinder.controller;

import com.example.ticketfinder.exception.TicketFinderDataPersistenceException;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.example.ticketfinder.dao.ConcertDao;
import com.example.ticketfinder.dao.UserDao;
import com.example.ticketfinder.entities.Concert;
import com.example.ticketfinder.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class UserController {

    // inject dependencies
    @Autowired
    UserDao userDao;
    @Autowired
    ConcertDao concertDao;

    /**
     *
     * @param model
     * @return
     * Directs user to the create account page
     */
    @GetMapping("/create-account")
    public String createAccount(Model model) {

        model.addAttribute("user", new User());

        return "create-account";
    }

    /**
     *
     * @param user
     * @param result
     * @param model
     * @return
     * Creates user account in the database if no errors are thrown
     */
    @PostMapping("add-user")
    public String createUser(@Valid User user, BindingResult result, Model model) {

        // declare boolean to catch errors
        boolean hasErrors = result.hasErrors();


        if(hasErrors) {
            // inputs had errors -> redirect to same page -> do NOT add user
            return "create-account";
        } else {

            // no errors found from thymeleaf on the frontend -> perform encryption on password and set
            // appropriate user type
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setUserType("User");

            // addUser can throw a unique persistence error if the email already exists
            // catch this and redirect to same page if thrown -> User not added
            try{
                userDao.addUser(user);
            }
            catch (TicketFinderDataPersistenceException e) {
                return "redirect:/create-account";
            }

            // exception was not thrown -> user was added -> go to sign-in page
            return "redirect:/sign-in";
        }
    }

    /**

     * @param model
     * @return
     * Returns the main admin page which shows all concerts
     */
    @GetMapping("admin")
    public String admin(Model model) {

        // load all concert data for admin page
        List<Concert> concerts = concertDao.getAllConcerts();
        // add concert data to model for use in frontend
        model.addAttribute("concerts", concerts);

        return "data-list-admin";
    }

    /**
     * @return
     * Directs user to sign in page
     */
    @GetMapping("sign-in")
    public String signIn() {

        return "sign-in";
    }

    /**
     * @return
     * Signs the user out
     */
    @GetMapping("sign-out")
    public String signOut() {
        return "sign-in";
    }

    /**
     * @return
     * Directs user to landing page
     */
    @PostMapping("home")
    public String home() {
        return "redirect:/";
    }


}