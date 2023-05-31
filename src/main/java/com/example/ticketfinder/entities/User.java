package com.example.ticketfinder.entities;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userType; //could also be boolean
    private List<Order> tickets;

    public int getId() {
        return id;
    }

    public void setId(int userId) {
        this.id = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(userType, user.userType) && Objects.equals(tickets, user.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, userType, tickets);
    }

    public List<Order> getTickets() {
        return tickets;
    }

    public void setTickets(List<Order> tickets) {
        this.tickets = tickets;
    }
}
