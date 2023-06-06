package com.example.ticketfinder.controller;

//import ch.qos.logback.core.model.Model;
import com.example.ticketfinder.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.example.ticketfinder.dao.ConcertDao;
import com.example.ticketfinder.dao.UserDao;
import com.example.ticketfinder.entities.Concert;
import com.example.ticketfinder.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    ConcertDao concertDao;

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("addUser")
    public String createUser(String firstName, String lastName, String email, String password) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setUserType("User");

        userDao.addUser(user);

        return "redirect:/createAccount";
    }

    @GetMapping("isAdminCHeck")
    public String isAdminCheck(Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = ((CustomUserDetails)principal).getUsername();
        User user = userDao.findByEmail(userEmail);

        if (user.getUserType().equalsIgnoreCase("admin")) {
            List<Concert> concerts = concertDao.getAllConcerts();
            model.addAttribute("concerts", concerts);

            return "dataListAdmin";
        } else {return "redirect:/";}
    }

    @GetMapping("signIn")
    public String signIn() {

        return "signIn";
    }

    @GetMapping("signOut")
    public String signOut() {
        return "signIn";
    }

    @GetMapping("createAccount")
    public String createAccount() {
        return "createAccount";
    }

    @PostMapping("home")
    public String home() {
        return "redirect:/";
    }


}