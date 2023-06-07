package com.example.ticketfinder.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class User {

    private int id;
    @NotBlank (message = "Must have a first name")
    private String firstName;
    @NotBlank (message = "Must have a last name")
    private String lastName;
    @NotBlank (message = "Must have a valid email address")
    @Email
    private String email;
    @NotBlank (message = "Must have a password")
    private String password;
    private String userType; //could also be boolean
    private List<Order> orders;

}
