package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TicketDaoDB {

    @Autowired
    JdbcTemplate jdbc;

    public Ticket findTicketByTicketId(int id) {

        final String FIND_USER_BY_TICKET_ID = "SELECT * FROM ticket WHERE ticket_id = ?;";
        return jdbc.queryForObject(FIND_USER_BY_TICKET_ID, new TicketMapper(), id);
    }

    @Transactional
    public void addTicket(Ticket ticket) {

        final String ADD_TICKET = "INSERT INTO ticket(ticket_id, " +
                "concert_id, price) VALUES(?,?,?);";
        jdbc.query(ADD_TICKET, new TicketMapper(),
                ticket.getId(),
                ticket.getConcertId(),
                ticket.getPrice());
    }

    @Transactional
    public void deleteTicket(int id) {

        final String ADD_TICKET = "DELETE FROM ticket " +
                "WHERE ticket_id = ?;";
        jdbc.query(ADD_TICKET, new TicketMapper(), id);
    }

    public Ticket updateTicket(Ticket ticket) {

        final String UPDATE_TICKET = "UPDATE ticket SET " +
                "concert_id = ?, " +
                "price = ?;";
        jdbc.update(UPDATE_TICKET, ticket.getId());

        return findTicketByTicketId(ticket.getId());
    }

    public static final class TicketMapper implements RowMapper<Ticket> {

        @Override
        public Ticket mapRow(ResultSet rs, int index) throws SQLException {
            Ticket ticket = new Ticket();
            ticket.setId(rs.getInt("ticket_id"));
            ticket.setConcertId(rs.getInt("concert_id"));
            ticket.setPrice(rs.getFloat("price"));

            return ticket;
        }
    }
}
