package com.example.ticketfinder.entities;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Data
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userType; //could also be boolean
    private List<Order> orders;

}
