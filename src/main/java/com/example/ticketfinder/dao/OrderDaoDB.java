package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class OrderDaoDB {

    @Autowired
    JdbcTemplate jdbc;

    public Order findOrderById(int id) {

        final String FIND_USER_BY_TICKET_ID = "SELECT * FROM ticket WHERE ticket_id = ?;";
        return jdbc.queryForObject(FIND_USER_BY_TICKET_ID, new OrderMapper(), id);
    }

    @Transactional
    public void addOrder(Order order) {

        final String ADD_TICKET = "INSERT INTO orders(concert_id, " +
                "user_id, quantity, price) VALUES(?,?,?,?);";
        jdbc.query(ADD_TICKET, new OrderMapper(),
                order.getId(),
                order.getConcertId(),
                order.getPrice());
    }

    @Transactional
    public void deleteOrder(int id) {

        final String ADD_TICKET = "DELETE FROM ticket " +
                "WHERE ticket_id = ?;";
        jdbc.query(ADD_TICKET, new OrderMapper(), id);
    }

    public Order updateOrder(Order order) {

        final String UPDATE_TICKET = "UPDATE ticket SET " +
                "concert_id = ?, " +
                "price = ?;";
        jdbc.update(UPDATE_TICKET, order.getId());

        return findOrderById(order.getId());
    }

    public static final class OrderMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int index) throws SQLException {
            Order ticket = new Order();
            ticket.setId(rs.getInt("ticket_id"));
            ticket.setConcertId(rs.getInt("concert_id"));
            ticket.setPrice(rs.getFloat("price"));

            return ticket;
        }
    }
}
