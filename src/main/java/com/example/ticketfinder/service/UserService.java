package com.example.ticketfinder.service;

import com.example.ticketfinder.dao.UserDao;
import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
     * @return - returns TRUE if email AND password corresponds to existing AND valid user, otherwise returns false
     */
    public boolean validUserCheck(String email, String password) {

        User user = userDao.findByEmail(email);

        if(user == null) {
            return false;
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    public boolean isAdminCheck(String email) {

        User user = userDao.findByEmail(email);
        return user.getUserType().equalsIgnoreCase("admin");
    }
}
