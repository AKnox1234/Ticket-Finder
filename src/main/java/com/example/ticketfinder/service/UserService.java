package com.example.ticketfinder.service;

import com.example.ticketfinder.dao.UserDao;
import com.example.ticketfinder.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    /**
     *
     * @param email
     * @param password
     * @return - returns TRUE if email an password corresponds to existing AND valid user, otherwise returns false
     */
    public boolean validUserCheck(String email, String password) {

        try {

            User user = userDao.findByEmail(email);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            return bCryptPasswordEncoder.matches(password, user.getPassword());

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAdminCheck(String email) {

        User user = userDao.findByEmail(email);
        if (user.getUserType().equalsIgnoreCase("admin")) {return true;}
        return false;
    }
}
