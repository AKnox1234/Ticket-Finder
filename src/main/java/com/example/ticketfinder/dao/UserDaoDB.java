package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Order;
import com.example.ticketfinder.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoDB {

    @Autowired
    JdbcTemplate jdbc;

    // used this string multiple times so this is just a shortcut
    final String USER_TICKET_JOIN = "ticket t JOIN user_ticket ut ON ut.ticket_id = t.ticket_id";

    public User findUserById(int id) {

        final String FIND_USER_BY_ID = "SELECT * FROM tf_user WHERE user_id = ?;";
        return jdbc.queryForObject(FIND_USER_BY_ID, new UserMapper(), id);
    }

    public User findByUsername(String username) {

        final String FIND_USER_BY_EMAIL = "SELECT * FROM tf_user WHERE email = ?";
        return jdbc.queryForObject(FIND_USER_BY_EMAIL, new UserMapper(), username);
    }

    public void addUser(User user) {

        final String ADD_USER = "INSERT INTO tf_user(first_name, last_name, " +
                "email, user_password, user_type)" +
                " VALUES(?,?,?,?,?);";
        jdbc.update(ADD_USER,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType());
    }

    @Transactional
    public void deleteUser(int id) {

        // first delete the tickets associated with the user
        final String DELETE_TICKETS_OF_USER = "DELETE FROM " +
                USER_TICKET_JOIN+" WHERE ut.user_id = ?;";
        jdbc.update(DELETE_TICKETS_OF_USER, id);

        final String DELETE_USER = "DELETE FROM tf_user WHERE user_id = ?;";
        jdbc.update(DELETE_USER, id);
    }

    public User updateUser(User user) {

        final String UPDATE_USER = "UPDATE tf_user SET " +
                "first_name = ?," +
                "last_name = ?," +
                "email = ?," +
                "user_password = ?," +
                "user_type = ?," +
                "WHERE user_id = ?;";
        jdbc.update(UPDATE_USER,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType(),
                user.getId());

        return findUserById(user.getId());
    }

    public List<Order> getTicketsOfUser(User user) {

        // get a list of the tickets associated with user instance
        final String GET_TICKETS = "SELECT * FROM " +
                USER_TICKET_JOIN+" WHERE user_id = ?;";
        return jdbc.query(GET_TICKETS, new OrderDaoDB.OrderMapper(), user.getId());
    }

    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int index) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("user_password"));
            user.setUserType(rs.getString("user_type"));

            return user;
        }
    }
}
