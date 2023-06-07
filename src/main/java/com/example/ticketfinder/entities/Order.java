package com.example.ticketfinder.entities;

import lombok.Data;

@Data
public class Order {

    // data annotation implements getters, setters hashcode etc for us
    private int id;
    private Concert concert;
    private float price;
    private int ticketQuantity;
}
