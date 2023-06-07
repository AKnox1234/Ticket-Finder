package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Concert;

import java.util.List;

public interface ConcertDao {

    Concert getConcertById(int iD);

    List<Concert> getAllConcerts();

    List<Concert> findConcertsBySearch(String search);

    public void updateConcert(Concert concert, int id);
}
