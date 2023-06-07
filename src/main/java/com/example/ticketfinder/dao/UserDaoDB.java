package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Concert;
import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.exception.TicketFinderDataPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoDB implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    public List<User> getAllUsers() {

        final String GET_ALL_USERS = "SELECT * FROM tf_user";
        return jdbc.query(GET_ALL_USERS, new UserMapper());

    }

    public User findByEmail(String username) {

        final String FIND_USER_BY_EMAIL = "SELECT * FROM tf_user WHERE email = ?";

        return jdbc.queryForObject(FIND_USER_BY_EMAIL, new UserMapper(), username);
    }

    public void addUser(User user) throws TicketFinderDataPersistenceException {

        User foundUser;

        try {
            foundUser = findByEmail(user.getEmail());
        } catch (EmptyResultDataAccessException e) {
            foundUser = null;
        }

        if(foundUser != null) {
            throw new TicketFinderDataPersistenceException("Email already in use");
        }

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

    /**
     * Mapper for user DTO.
     */
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