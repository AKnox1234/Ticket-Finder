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

    // inject dependencies
    @Autowired
    UserDao userDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserDaoDBTest() {

    }

    @BeforeEach
    public void setUp() {

        // make sure the order and user table is reset everytime before test is ran
        jdbcTemplate.update("delete from orders");
        jdbcTemplate.update("delete from tf_user");

    }

    /**
     * Tests whether both add user and find by email works
     */
    @Test
    void testAddUserAndFindByEmail() throws TicketFinderDataPersistenceException {

        // create the user
        User testUser = new User();
        testUser.setEmail("GeorgeClooney@hotmail.com");
        testUser.setPassword("hollywood");
        testUser.setFirstName("George");
        testUser.setLastName("Clooney");
        testUser.setUserType("User");

        // add the user to the database
        userDao.addUser(testUser);

        // see if the correct user can be found through their email
        User foundUser = userDao.findByEmail(testUser.getEmail());

        // if emails match, correct user was found
        assertEquals(testUser.getEmail(), foundUser.getEmail());

    }

    /**
     * Tests whether a duplicate email will throw the appropriate exception
     */
    @Test
    void testDuplicateEmailThrowsPersistenceException() {

        // create the duplicate users
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
                    // try to add both the users -> should throw the error
                    userDao.addUser(testUser1);
                    userDao.addUser(testUser2);
                });

        // only one user should have been added
        assertEquals(1, userDao.getAllUsers().size());
    }
}

