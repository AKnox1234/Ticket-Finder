package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Concert;

import java.util.List;

public interface ConcertDao {

    public Concert getConcertById(int iD);
    public List<Concert> getAllConcerts();
    public List<Concert> findConcertsBySearch(String search);


}
