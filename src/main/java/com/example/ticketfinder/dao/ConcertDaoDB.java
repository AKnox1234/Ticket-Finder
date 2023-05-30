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
public class ConcertDaoDB {

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
                "Join artist a ON c.artist_id = a.artist_id;";
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

}
