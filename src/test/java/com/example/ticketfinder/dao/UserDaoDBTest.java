package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoDBTest {

    @Autowired
    UserDao userDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserDaoDBTest() {

    }

    @BeforeEach
    public void setUp() {

        jdbcTemplate.update("delete from orders");
        jdbcTemplate.update("delete from tf_user");

    }

    @Test
    void testAddUserAndFindByEmail() {

        User testUser = new User();
        testUser.setEmail("GeorgeClooney@hotmail.com");
        testUser.setPassword("hollywood");
        testUser.setFirstName("George");
        testUser.setLastName("Clooney");
        testUser.setUserType("User");

        userDao.addUser(testUser);
        User foundUser = userDao.findByEmail(testUser.getEmail());
        assertEquals(testUser.getEmail(), foundUser.getEmail());

    }
}