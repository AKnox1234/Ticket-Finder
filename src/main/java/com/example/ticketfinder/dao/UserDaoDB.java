package com.example.ticketfinder.dao;

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

    // inject dependencies
    @Autowired
    JdbcTemplate jdbc;


    /**
     * @return float
     * Returns all users from the database
     */
    public List<User> getAllUsers() {

        // SQL required to perform the get all users feature
        final String GET_ALL_USERS = "SELECT * FROM tf_user";

        // use JDBCTemplate and SQL to perform the function
        return jdbc.query(GET_ALL_USERS, new UserMapper());

    }

    /**
     * @param username
     * @return float
     * Returns the users, if any, who match the given email in the database
     */
    public User findByEmail(String username) {

        // SQL required to perform the find by email feature
        final String FIND_USER_BY_EMAIL = "SELECT * FROM tf_user WHERE email = ?";

        // use JDBCTemplate and SQL to perform the function
        return jdbc.queryForObject(FIND_USER_BY_EMAIL, new UserMapper(), username);
    }

    /**
     * @param user
     * @throws TicketFinderDataPersistenceException
     * Returns the users, if any, who match the given email in the database
     */
    public void addUser(User user) throws TicketFinderDataPersistenceException {

        // declare user object
        User foundUser;

        // in this instance, the findByEmail method can throw an exception
        // this is because the addUser method is the only time findByEmail is performed
        // when a guaranteed user is not signed in
        try {
            foundUser = findByEmail(user.getEmail());
        } catch (EmptyResultDataAccessException e) {
            // exception was thrown, meaning the given email is not yet in use
            // so lets assign foundUser to null
            foundUser = null;
        }

        // if user is null, we can proceed to add the user to the database
        // if not, throw the custom exception
        if (foundUser != null) {
            throw new TicketFinderDataPersistenceException("Email already in use");
        }

        // SQL required to perform the add user feature
        final String ADD_USER = "INSERT INTO tf_user(first_name, last_name, " +
                "email, user_password, user_type)" +
                " VALUES(?,?,?,?,?);";

        // use JDBCTemplate and SQL to perform the function
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