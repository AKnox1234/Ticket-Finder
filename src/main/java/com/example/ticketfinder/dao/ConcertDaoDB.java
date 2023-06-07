package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ConcertDaoDB implements ConcertDao {

    @Autowired
    JdbcTemplate jdbc;

    public Concert getConcertById(int iD) {

        final String GET_CONCERT_BY_ID = "SELECT c.concert_id, a.artist_name, v.venue_name, c.concert_date, a.image, " +
                "v.city FROM concert c\n" +
                "JOIN venue v ON c.venue_id = v.venue_id\n" +
                "Join artist a ON c.artist_id = a.artist_id\n" +
                "WHERE c.concert_id = ?;";
        Concert concert = jdbc.queryForObject(GET_CONCERT_BY_ID, new ConcertMapper(), iD);
        return concert;
    }

    /**
     *
     * @return returns a list of concert object loaded from database
     * Loads all concert data from database into a List<Concert>
     */
    public List<Concert> getAllConcerts() {

        // join concert table with artist and venue based on ID-s from concert table
        final String GET_ALL_CONCERTS = "SELECT c.concert_id, a.artist_name, v.venue_name, c.concert_date, a.image, " +
                "v.city FROM concert c\n" +
                "JOIN venue v ON c.venue_id = v.venue_id\n" +
                "Join artist a ON c.artist_id = a.artist_id\n" +
                "ORDER BY c.concert_id;";
        // map concert data rows to Concert object instances
        return jdbc.query(GET_ALL_CONCERTS, new ConcertMapper());

    }

    /**
     *
     * @param search
     * @return a list of concerts that are closest to search criteria based on available keywords
     */
    public List<Concert> findConcertsBySearch(String search) {

        /* join concert table with artist and venue based on ID-s from concert table and
           implement searching with WHERE, LIKE, OR SQL keywords*/
        final String GET_ALL_CONCERTS_BY_SEARCH = "SELECT c.concert_id, a.artist_name, v.venue_name, c.concert_date, a.image, \n" +
                "v.city FROM concert c                \n" +
                "JOIN venue v ON c.venue_id = v.venue_id\n" +
                "JOIN artist a ON c.artist_id = a.artist_id\n" +
                "WHERE a.genre LIKE '%" + search + "%' " +
                "OR a.artist_name LIKE '%" + search + "%' " +
                "OR v.venue_name LIKE '%" + search + "%' " +
                "OR v.country LIKE '%" + search + "%' " +
                "OR v.city LIKE '%" + search + "%';";

        // return the data mapped to Concert object instances
        return jdbc.query(GET_ALL_CONCERTS_BY_SEARCH, new ConcertMapper());

    }

    /**
     *
     * @param concert
     * Adds data from concert object to database. Basic CRUD Create method.
     */
    public void addConcert(Concert concert) {

        final String ADD_CONCERT = "INSERT INTO concert(concert_id, artist_id, venue_id, concert_date) \n" +
                "VALUES(?,?,?,?);";
        jdbc.update(ADD_CONCERT,
                concert.getId(),
                concert.getArtist(),
                concert.getVenue(),
                concert.getConcertDate());
    }

    /**
     *
     * @param Id
     * Deletes concert data corresponding to an ID from database. Basic CRUD Delete method.
     */
    public void deleteConcert(int Id) {

        final String DELETE_CONCERT = "DELETE FROM concert\n" +
                " WHERE concert_id = ?;";

        jdbc.update(DELETE_CONCERT, Id);
    }

    /**
     *
     * @param concert
     * @param id
     * Admin feature.
     * Allows for editing of concert data corresponding to an ID in database. Basic CRUD Update method.
     */
    public void updateConcert(Concert concert, int id) {

        // get artist ID from artist name (editing is done with names, not ID-s on the admin page)
        final String GET_ARTIST_ID = "SELECT artist_id FROM artist a WHERE a.artist_name = ?;";
        List<Integer> artistId = jdbc.query(GET_ARTIST_ID,
                new ConcertDaoDB.ArtistIdMapper(), concert.getArtist());

        // get venue ID from venue name (editing is done with names, not ID-s on the admin page)
        final String GET_VENUE_ID = "SELECT venue_id FROM venue v WHERE v.venue_name = ?;";
        List<Integer> venueId = jdbc.query(GET_VENUE_ID,
                new ConcertDaoDB.VenueIdMapper(), concert.getVenue());

        // query for update operation using ID-s (since concert data is stored through ID-s for artist and venue)
        final String UPDATE_CONCERT = "UPDATE concert SET\n" +
                " artist_id = ?,\n" +
                " venue_id = ?,\n" +
                " concert_date = ?\n" +
                "WHERE concert_id = ?;";
        jdbc.update(UPDATE_CONCERT,
                artistId.get(0),
                venueId.get(0),
                concert.getConcertDate(), id);
    }

    /**
     *
     * @param Id
     * @param seatType
     * @param quantity
     * Once an order is placed, this method removes the number of tickets corresponding
     * to the order's ticket quantity and seat type
     */
    public void removeTicketsForConcert(int Id, String seatType, int quantity) {

        // query searches for seat type and concert ID, and modifies quantity where previous two match
        final String REMOVE_TICKETS = "UPDATE concert_seat cs\n" +
                "JOIN concert c ON c.concert_id = cs.concert_id\n" +
                "JOIN seat s ON s.seat_id = cs.seat_id\n" +
                "SET quantity = quantity - ?\n" +
                "WHERE s.seat_type LIKE ? AND c.concert_id = ?;";
        jdbc.update(REMOVE_TICKETS, quantity, seatType.toLowerCase(), Id);
    }

    /**
     *
     * @param Id
     * @return a list of floats that correspond to the number of seats
     * left for a given concert for every seat type (float type was used in data storage
     * since we were just dividing venue capacity with an integer, that could have resulted in a float)
     * Used to calculate the number of seats left for a given concert for every seat type
     */
    public List<Float> seatsLeft(int Id) {

        // query searches for concert in concert-seat joined table and select available number of seats for seat types
        final String SEATS_LEFT = "SELECT quantity FROM concert_seat cs\n" +
                "JOIN concert c ON c.concert_id = cs.concert_id\n" +
                "JOIN seat s ON s.seat_id = cs.seat_id\n" +
                "WHERE c.concert_id = ?;";
        return jdbc.queryForList(SEATS_LEFT, Float.class, Id);
    }

    /**
     *
     * @param Id
     * @return boolean
     * Checks whether a concert has negative available seats after a placed order ->
     * checks if order could be placed.
     * Ended up not using it due to time limit.
     */
    public boolean validSeatNoCheck(int Id) {

        List<Float> seatNos = seatsLeft(Id);
        for (float number : seatNos) {
            if (number < 0) {return false;}
        }

        return true;
    }

    /**
     *
     * @param Id
     * @return boolean
     * Checks if number of seats left is zero -> display sold out on frontend.
     * Ended up not using it due to time limit.
     */
    public boolean zeroSeatsLeftCheck(int Id) {

        List<Float> seatNos = seatsLeft(Id);
        for (float number : seatNos) {
            if (number != 0) {return false;}
        }

        return true;
    }

    public static final class ConcertMapper implements RowMapper<Concert> {

        @Override
        public Concert mapRow(ResultSet rs, int index) throws SQLException {
            Concert concert = new Concert();
            concert.setId(rs.getInt("concert_id"));
            concert.setArtist(rs.getString("artist_name"));
            concert.setArtistImage(rs.getString("image"));
            concert.setVenue(rs.getString("venue_name"));
            concert.setCity(rs.getString("city"));
            concert.setConcertDate(rs.getDate("concert_date"));

            return concert;
        }
    }

    /**
     * Mapper for artist ID attribute
     */
    public static final class ArtistIdMapper implements RowMapper<Integer> {

        public Integer mapRow(ResultSet rs, int index) throws SQLException {

            return rs.getInt("artist_id");
        }
    }

    /**
     * Mapper for venue ID attribute
     */
    public static final class VenueIdMapper implements RowMapper<Integer> {

        public Integer mapRow(ResultSet rs, int index) throws SQLException {

            return rs.getInt("venue_id");
        }
    }

}