package com.example.ticketfinder.dao;

import com.example.ticketfinder.entities.Order;
import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.security.WebSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class OrderDaoDBTest {

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

        jdbcTemplate.update("delete from orders");
        jdbcTemplate.update("delete from tf_user");
    }


    @Test
    void testAddOrderAndGetUsersOrders() {

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

        userDao.addUser(user1);
        userDao.addUser(user2);

        user1 = userDao.findByEmail(user1.getEmail());
        user2 = userDao.findByEmail(user2.getEmail());

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

        orderDao.addOrder(order1, user1.getId());
        orderDao.addOrder(order2, user1.getId());
        orderDao.addOrder(order3, user2.getId());

        assertEquals(2, orderDao.getAllUsersOrders(user1).size());
        assertEquals(1, orderDao.getAllUsersOrders(user2).size());


    }

}