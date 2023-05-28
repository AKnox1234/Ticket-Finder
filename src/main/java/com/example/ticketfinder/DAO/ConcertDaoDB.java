package com.example.ticketfinder.DAO;

import com.example.ticketfinder.Entities.Concert;
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

    public List<Concert> getAllConcerts() {

        final String GET_ALL_CONCERTS = "SELECT c.concert_id, a.artist_name, v.venue_name, c.concert_date, a.image, " +
                "v.city FROM concert c\n" +
                "JOIN venue v ON c.venue_id = v.venue_id\n" +
                "Join artist a ON c.artist_id = a.artist_id;";
        return jdbc.query(GET_ALL_CONCERTS, new ConcertMapper());

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
