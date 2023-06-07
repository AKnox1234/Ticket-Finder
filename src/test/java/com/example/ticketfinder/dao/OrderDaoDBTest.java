package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Order;
import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.exception.TicketFinderDataPersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class OrderDaoDBTest {

    // inject dependencies
    @Autowired
    OrderDao orderDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ConcertDao concertDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public OrderDaoDBTest() {

    }

    @BeforeEach
    public void setUp() {

        // make sure the order and user table is reset everytime before test is ran
        jdbcTemplate.update("delete from orders");
        jdbcTemplate.update("delete from tf_user");
    }


    /**
     * Tests multiple crud operations -> add, delete order and get a specific users orders
     */
    @Test
    void testAddDeleteOrderAndAlsoGetUsersOrders() throws TicketFinderDataPersistenceException {

        // create 2 different users
        User user1 = new User();
        user1.setEmail("GeorgeClooney@hotmail.com");
        user1.setPassword("hollywood");
        user1.setFirstName("George");
        user1.setLastName("Clooney");
        user1.setUserType("User");

        User user2 = new User();
        user2.setEmail("BradPitt@outlook.com");
        user2.setPassword("Troy");
        user2.setFirstName("Brad");
        user2.setLastName("Pitt");
        user2.setUserType("User");

        // add the users to the test database
        userDao.addUser(user1);
        userDao.addUser(user2);

        // reassign user objects to the database version so their ids are not 0
        user1 = userDao.findByEmail(user1.getEmail());
        user2 = userDao.findByEmail(user2.getEmail());

        // create the orders
        Order order1 = new Order();
        order1.setConcert(concertDao.getConcertById(5));
        order1.setTicketQuantity(2);
        order1.setPrice(80);

        Order order2 = new Order();
        order2.setConcert(concertDao.getConcertById(20));
        order2.setTicketQuantity(4);
        order2.setPrice(160);

        Order order3 = new Order();
        order3.setConcert(concertDao.getConcertById(34));
        order3.setTicketQuantity(1);
        order3.setPrice(35);

        // add 2 orders in user 1s name
        orderDao.addOrder(order1, user1.getId());
        orderDao.addOrder(order2, user1.getId());

        // add 1 order in user 2s name
        orderDao.addOrder(order3, user2.getId());

        // user 1s order list should be of size 2, and user 2s list should be of size 1
        assertEquals(2, orderDao.getAllUsersOrders(user1).size());
        assertEquals(1, orderDao.getAllUsersOrders(user2).size());

        // try deleting one of user 1s orders
        List<Order> user1Orders = orderDao.getAllUsersOrders(user1);
        orderDao.deleteOrder(user1Orders.get(1).getId());

        // user 1s order list size should now be 1
        assertEquals(1, orderDao.getAllUsersOrders(user1).size());

    }
}