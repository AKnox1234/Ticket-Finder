package com.example.ticketfinder.security;

import com.example.ticketfinder.dao.UserDaoDB;
import com.example.ticketfinder.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDaoDB userDaoDB;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDaoDB.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return new CustomUserDetails(user);
    }
}
