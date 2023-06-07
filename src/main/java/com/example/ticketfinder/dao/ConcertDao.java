package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Concert;

import java.util.List;

public interface ConcertDao {

    Concert getConcertById(int iD);

    List<Concert> getAllConcerts();

    List<Concert> findConcertsBySearch(String search);

    void updateConcert(Concert concert, int id);

    void removeTicketsForConcert(int Id, String seatType, int quantity);

    List<Float> seatsLeft(int Id);
}
