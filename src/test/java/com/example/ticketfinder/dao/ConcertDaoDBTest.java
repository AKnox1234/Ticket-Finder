package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Concert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcertDaoDBTest {

    // inject dependencies
    @Autowired
    ConcertDao concertDao;
    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    public void setUp() {

        // make sure concert under test for update function test
        // is reset every time test is run
        Concert concertUnderTest = concertDao.getConcertById(2);
        concertUnderTest.setArtist("Rammstein");
        concertDao.updateConcert(concertUnderTest, 2);
    }
    /**
     * Tests whether a concert can be correctly found just by its ID number
     */
    @Test
    void testGetConcertById() {

        // try to obtain a desired concert through an id
        Concert expectedConcert = concertDao.getConcertById(100);

        // found concert's id should be 100
        assertEquals(100,expectedConcert.getId());

    }

    /**
     * Tests whether appropriate concerts are being returned
     * in respect to the search criteria
     */
    @Test
    void testFindConcertBySearch() {

        // realistic search
        String firstSearch = "Eminem";
        // unrealistic search
        String secondSearch = "faafafafwfdaqf";

        // perform first search
        List<Concert> concerts = concertDao.findConcertsBySearch(firstSearch);
        int validCounter = 0;

        for(Concert concert : concerts) {

            if(concert.getArtist().equalsIgnoreCase(firstSearch)) {
                // if the concert artist matches the search -> increment
                validCounter++;
            }
        }

        // all found concerts should have had eminem as the artist
        // so counter should match the size of list
        assertEquals(validCounter, concerts.size());

        // try unrealistic search
        concerts = concertDao.findConcertsBySearch(secondSearch);

        // no concert data should be returned
        assertEquals(0, concerts.size());
    }

    /**
     * Tests whether update concert function
     * is actually changing the data within the database correctly
     */
    @Test
    void testUpdateConcert() {

        // get the original concert data
        Concert originalConcert = concertDao.getConcertById(2);
        String originalArtist = originalConcert.getArtist();

        // change a data entry
        originalConcert.setArtist("Eminem");

        // update the change
        concertDao.updateConcert(originalConcert, originalConcert.getId());

        // retrieve the same concert as a new instance
        Concert newConcert = concertDao.getConcertById(2);

        // check the artists now do not match
        assertNotEquals(originalArtist, newConcert.getArtist());

        // check the venues still match
        assertEquals(originalConcert.getVenue(), newConcert.getVenue() );
    }


}