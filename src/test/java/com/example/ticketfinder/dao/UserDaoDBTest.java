package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.exception.TicketFinderDataPersistenceException;
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
    void testAddUserAndFindByEmail() throws TicketFinderDataPersistenceException {

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

    @Test
    void testDuplicateEmailThrowsPersistenceException() throws TicketFinderDataPersistenceException {

        User testUser1 = new User();
        testUser1.setEmail("GeorgeClooney@hotmail.com");
        testUser1.setPassword("hollywood");
        testUser1.setFirstName("George");
        testUser1.setLastName("Clooney");
        testUser1.setUserType("User");

        User testUser2 = new User();
        testUser2.setEmail("GeorgeClooney@hotmail.com");
        testUser2.setPassword("hollywood");
        testUser2.setFirstName("George");
        testUser2.setLastName("Clooney");
        testUser2.setUserType("User");

        assertThrows(TicketFinderDataPersistenceException.class,
                () ->{
                    userDao.addUser(testUser1);
                    userDao.addUser(testUser2);
                });

        assertEquals(1, userDao.getAllUsers().size());
    }

}

