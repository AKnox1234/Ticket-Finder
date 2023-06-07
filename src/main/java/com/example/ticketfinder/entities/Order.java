package com.example.ticketfinder.entities;

import lombok.Data;

@Data
public class Order {

    private int id;

    private Concert concert;

    private float price;

    private int ticketQuantity;
}
