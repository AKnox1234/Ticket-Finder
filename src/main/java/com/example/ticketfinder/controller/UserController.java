package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.UserDaoDB;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserDaoDB userDaoDB;

    @GetMapping("createAccount")
    public String createAccount() {
        return "createAccount";
    }

    @GetMapping("signIn")
    public String signIn() {
        return "signIn";
    }

    /*@PostMapping("createAccount")
    public String createAccount(HttpServletRequest request) {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setSpecialty(specialty);

        teacherDao.addTeacher(teacher);

        return "redirect:/teachers";
    }*/

}
