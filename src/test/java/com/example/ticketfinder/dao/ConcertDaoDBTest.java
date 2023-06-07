package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Concert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcertDaoDBTest {

    @Autowired
    ConcertDao concertDao;
    @Autowired
    JdbcTemplate jdbc;

    public ConcertDaoDBTest() {

    }

    @Test
    public void testGetConcertById() {

        Concert expectedConcert = concertDao.getConcertById(100);
        assertEquals(expectedConcert.getId(),100);

    }

    @Test
    public void testFindConcertBySearch() {

        String firstSearch = "Eminem";
        String secondSearch = "faafafafwfdaqf";
        List<Concert> concerts = concertDao.findConcertsBySearch(firstSearch);
        int validCounter = 0;

        for(Concert concert : concerts) {

            if(concert.getArtist().equalsIgnoreCase(firstSearch)) {
                validCounter++;
            }
        }

        assertEquals(validCounter, concerts.size());

        concerts = concertDao.findConcertsBySearch(secondSearch);

        assertEquals(0, concerts.size());
    }

    @Test
    public void testUpdateConcert() {

        Concert concert = concertDao.getConcertById(2);
        String originalArtist = concert.getArtist();

        concert.setArtist("Eminem");

        concertDao.updateConcert(concert, concert.getId());

        Concert newConcert = concertDao.getConcertById(2);

        assertNotEquals(originalArtist, newConcert.getArtist());
    }


}