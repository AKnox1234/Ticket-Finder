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

    public List<Concert> getAllConcerts() {

        final String GET_ALL_CONCERTS = "SELECT c.concert_id, a.artist_name, v.venue_name, c.concert_date, a.image, " +
                "v.city FROM concert c\n" +
                "JOIN venue v ON c.venue_id = v.venue_id\n" +
                "Join artist a ON c.artist_id = a.artist_id\n" +
                "ORDER BY c.concert_id;";
        return jdbc.query(GET_ALL_CONCERTS, new ConcertMapper());

    }

    public List<Concert> findConcertsBySearch(String search) {

        final String GET_ALL_CONCERTS_BY_SEARCH = "SELECT c.concert_id, a.artist_name, v.venue_name, c.concert_date, a.image, \n" +
                "v.city FROM concert c                \n" +
                "JOIN venue v ON c.venue_id = v.venue_id\n" +
                "JOIN artist a ON c.artist_id = a.artist_id\n" +
                "WHERE a.genre LIKE '%" + search + "%' " +
                "OR a.artist_name LIKE '%" + search + "%' " +
                "OR v.venue_name LIKE '%" + search + "%' " +
                "OR v.country LIKE '%" + search + "%' " +
                "OR v.city LIKE '%" + search + "%';";


        return jdbc.query(GET_ALL_CONCERTS_BY_SEARCH, new ConcertMapper());

    }

    public void addConcert(Concert concert) {

        final String ADD_CONCERT = "INSERT INTO concert(concert_id, artist_id, venue_id, concert_date) \n" +
                "VALUES(?,?,?,?);";
        jdbc.update(ADD_CONCERT,
                concert.getId(),
                concert.getArtist(),
                concert.getVenue(),
                concert.getConcertDate());
    }

    public void deleteConcert(int Id) {

        final String DELETE_CONCERT = "DELETE FROM concert\n" +
                " WHERE concert_id = ?;";

        jdbc.update(DELETE_CONCERT, Id);
    }

    public void updateConcert(Concert concert, int id) {

        final String GET_ARTIST_ID = "SELECT artist_id FROM artist a WHERE a.artist_name LIKE '?';";
        List<Integer> artistId = jdbc.query(GET_ARTIST_ID,
                new ConcertDaoDB.ArtistIdMapper(), concert.getArtist());

        final String GET_VENUE_ID = "SELECT venue_id FROM venue v WHERE v.venue_name LIKE '?';";
        List<Integer> venueId = jdbc.query(GET_VENUE_ID,
                new ConcertDaoDB.VenueIdMapper(), concert.getVenue());

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

    public void removeTicketsForConcert(int Id, String seatType, int quantity) {

        final String REMOVE_TICKETS = "UPDATE concert_seat cs\n" +
                "JOIN concert c ON c.concert_id = cs.concert_id\n" +
                "JOIN seat s ON s.seat_id = cs.seat_id\n" +
                "SET quantity = quantity - ?\n" +
                "WHERE s.seat_type LIKE '?' AND c.concert_id = ?;";
        jdbc.update(REMOVE_TICKETS, quantity, seatType, Id);
    }

    public List<Float> seatsLeft(int Id) {

        final String TICKETS_LEFT = "SELECT quantity FROM concert_seat cs\n" +
                "JOIN concert c ON c.concert_id = cs.concert_id\n" +
                "JOIN seat s ON s.seat_id = cs.seat_id\n" +
                "WHERE c.concert_id = ?;";
        return jdbc.queryForList(TICKETS_LEFT, Float.class, Id);
    }

    public boolean validSeatNoCheck(int Id) {

        List<Float> seatNos = seatsLeft(Id);
        for (float number : seatNos) {
            if (number < 0) {return false;}
        }

        return true;
    }

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

    public static final class ArtistIdMapper implements RowMapper<Integer> {

        public Integer mapRow(ResultSet rs, int index) throws SQLException {

            return rs.getInt("artist_id");
        }
    }

    public static final class VenueIdMapper implements RowMapper<Integer> {

        public Integer mapRow(ResultSet rs, int index) throws SQLException {

            return rs.getInt("venue_id");
        }
    }

}