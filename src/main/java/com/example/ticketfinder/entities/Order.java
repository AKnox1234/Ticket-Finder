package com.example.ticketfinder.entities;

import lombok.Data;

import java.text.DecimalFormat;

@Data
public class Order {

    private int id;

    private Concert concert;

    private float price;

    private int ticketQuantity;

}
