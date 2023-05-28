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

        final String GET_ALL_CONCERTS = "SELECT * FROM concert c " +
                "JOIN venue v ON v.venue_id";
        return jdbc.query(GET_ALL_CONCERTS, new ConcertMapper());

    }

    public static final class ConcertMapper implements RowMapper<Concert> {

        @Override
        public Concert mapRow(ResultSet rs, int index) throws SQLException {
            Concert concert = new Concert();
            concert.setId(rs.getInt("concert_id"));
//            concert.setArtist(rs.getString("artist_name"));
            concert.setVenue(rs.getString("venue_name"));
            concert.setCity(rs.getString("city"));
            concert.setConcertDate(rs.getDate("concert_date"));

            return concert;
        }
    }

}
