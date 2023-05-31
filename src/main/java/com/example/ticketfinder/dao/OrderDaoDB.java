package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Concert;
import com.example.ticketfinder.entities.Order;
import com.example.ticketfinder.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderDaoDB implements OrderDao{

    @Autowired
    JdbcTemplate jdbc;

    public List<Order> getAllUsersOrders(User user) {

        final String GET_ALL_USERS_ORDERS = "SELECT o.order_id, a.artist_name, v.venue_name, c.concert_date, o.quantity\n" +
                "FROM orders o               \n" +
                "JOIN concert c ON o.concert_id = c.concert_id\n" +
                "JOIN artist a ON c.artist_id = a.artist_id\n" +
                "JOIN venue v ON c.venue_id = v.venue_id\n" +
                "Where user_id = ?;";
        return jdbc.query(GET_ALL_USERS_ORDERS, new OrderMapper(), user.getId());


    }

    public void addOrder(Order order, int userId) {

        final String ADD_ORDER = "INSERT INTO orders(concert_id, user_id, quantity, price) \n" +
                "VALUES(?,?,?,?);";
        jdbc.update(ADD_ORDER,
                order.getConcert().getId(),
                userId,
                order.getQuantity(),
                order.getPrice());
    }

    public static final class OrderMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int index) throws SQLException {
            Order order = new Order();
            Concert concert = new Concert();
            concert.setArtist(rs.getString("artist_name"));
            concert.setVenue(rs.getString("venue_name"));
            concert.setConcertDate(rs.getDate("concert_date"));
            order.setConcert(concert);
            order.setId(rs.getInt("order_id"));
            order.setQuantity(rs.getInt("quantity"));

            return order;
        }
    }
}
