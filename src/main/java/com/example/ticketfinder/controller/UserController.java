package com.example.ticketfinder.controller;

import com.example.ticketfinder.exception.TicketFinderDataPersistenceException;
import com.example.ticketfinder.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    UserDao userDao;

    @Autowired
    ConcertDao concertDao;

    @GetMapping("/create-account")
    public String createAccount(Model model) {

        model.addAttribute("user", new User());

        return "create-account";
    }

    @PostMapping("add-user")
    public String createUser(@Valid User user, BindingResult result, Model model) {

        boolean hasErrors = result.hasErrors();

        if(hasErrors) {
            return "create-account";
        } else {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setUserType("User");

            try{
                userDao.addUser(user);
            }
            catch (TicketFinderDataPersistenceException e) {
                return "redirect:/create-account";
            }
            return "redirect:/create-account";
        }
    }

    @GetMapping("admin")
    public String admin(Model model) {

            List<Concert> concerts = concertDao.getAllConcerts();
            model.addAttribute("concerts", concerts);

            return "data-list-admin";
    }

    @GetMapping("sign-in")
    public String signIn() {

        return "sign-in";
    }

    @GetMapping("sign-out")
    public String signOut() {
        return "sign-in";
    }

    @PostMapping("home")
    public String home() {
        return "redirect:/";
    }


}