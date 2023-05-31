package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.UserDaoDB;
import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {

    @Autowired
    UserDaoDB userDaoDB;


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

        userDaoDB.addUser(user);

        return "redirect:/createAccount";
    }

    public String findUserById(int iD, Model model ) {

        User user = userDaoDB.findUserById(iD);
        model.addAttribute("user", user);

        return "/home";

    }



    @GetMapping("signIn")
    public String signIn() {
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
