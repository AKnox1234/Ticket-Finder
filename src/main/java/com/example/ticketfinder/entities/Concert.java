package com.example.ticketfinder.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Concert {

    private int id;
    private String artist;
    private String venue;
    private String city;
    private Date concertDate;
    private String artistImage;
    private int ticketsRemaining;

}
