package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.UserDao;
import com.example.ticketfinder.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserDao userDao;


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