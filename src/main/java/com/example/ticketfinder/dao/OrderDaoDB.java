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
public class OrderDaoDB implements OrderDao {

    @Autowired
    JdbcTemplate jdbc;

    public List<Order> getAllUsersOrders(User user) {

        final String GET_ALL_USERS_ORDERS =
                "SELECT o.order_id, a.artist_name, v.venue_name, c.concert_date,c.concert_id, o.quantity\n" +

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
                order.getTicketQuantity(),
                order.getPrice());
    }

    public void deleteOrder(int orderID) {

        final String DELETE_ORDER = "DELETE FROM orders\n" +
                " WHERE order_id = ?;";

        jdbc.update(DELETE_ORDER, orderID);

    }

    public float calcConcertPrice(int id, String seatType) {

        final String BASE_PRICE = "SELECT a.base_price, v. FROM artist a" +
                "JOIN concert c ON c.artist_id = a.artist_id" +
                "WHERE c.concert_id = ?;";
        float basePrice = jdbc.queryForObject(BASE_PRICE, new FloatMapper(), id);

        final String SEAT_PRICE = "SELECT DISTINCT s.seat_price from seat s " +
                "JOIN venue_seat vs ON vs.seat_id = s.seat_id " +
                "JOIN venue v ON vs.venue_id = v.venue_id " +
                "WHERE s.seat_type LIKE '%"+seatType+"%';";
        float seat_price = jdbc.queryForObject(SEAT_PRICE, new FloatMapper(), seatType);

        return (basePrice + seat_price);
    }

    public static final class OrderMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int index) throws SQLException {
            Order order = new Order();
            Concert concert = new Concert();
            concert.setId(rs.getInt("concert_id"));
            concert.setArtist(rs.getString("artist_name"));
            concert.setVenue(rs.getString("venue_name"));
            concert.setConcertDate(rs.getDate("concert_date"));
            order.setConcert(concert);
            order.setId(rs.getInt("order_id"));
            order.setTicketQuantity(rs.getInt("quantity"));

            return order;
        }
    }

    public static final class FloatMapper implements RowMapper<Float> {

        public Float mapRow(ResultSet rs, int index) throws SQLException {

            return rs.getFloat("base_price");
        }
    }
}