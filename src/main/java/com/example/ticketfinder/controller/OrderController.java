package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.*;
import com.example.ticketfinder.entities.Concert;
import com.example.ticketfinder.entities.Order;
import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class OrderController {

    // inject dependencies
    @Autowired
    OrderDao orderDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ConcertDao concertDao;

    /**
     * @param model
     * @return Returns a list of orders from the database that are associated
     * to the specific users ID
     */
    @GetMapping("order-list")
    public String orderList(Model model) {

        // get the logged-in user
        User user = getLoggedInUser();

        // consult the orders table with the newly acquired user objects ID
        // and return the appropriate orders
        List<Order> usersOrders = orderDao.getAllUsersOrders(user);

        model.addAttribute("usersOrders", usersOrders);

        return "order-list";
    }

    /**
     * @param request
     * @return Adds an order to the database in the users name
     */
    @PostMapping("add-order")
    public String addOrder(HttpServletRequest request) {

        // retrieve parameters from the request
        int concertId = Integer.parseInt(request.getParameter("concertId"));
        int ticketQuantity = Integer.parseInt(request.getParameter("quantity"));
        String seatType = request.getParameter("seatType");

        // use concert id from request to find the concert in question
        Concert concert = concertDao.getConcertById(concertId);

        // calculate price for order
        float price = orderDao.calcConcertPrice(concertId, seatType);
        price *= ticketQuantity;

        // removing entries from remaining tickets pool according to order's ticket quantity
        concertDao.removeTicketsForConcert(concertId, seatType, ticketQuantity);

        // create the order object using the obtained data
        Order order = new Order();
        order.setConcert(concert);
        order.setTicketQuantity(ticketQuantity);
        order.setPrice(price);

        // get the logged-in user
        User user = getLoggedInUser();

        // add the order in the users name
        orderDao.addOrder(order, user.getId());

        return "redirect:/";

    }

    /**
     * @param request
     * @return Deletes the desired order from the users order list and database
     */
    @GetMapping("delete-order")
    public String deleteOrder(HttpServletRequest request) {

        // retrieve the order id from the request
        int orderId = Integer.parseInt(request.getParameter("id"));

        // perform the crud operation
        orderDao.deleteOrder(orderId);

        return "redirect:order-list";

    }

    /**
     * @return Finds the current logged-in user objects data
     * using spring security features
     */
    private User getLoggedInUser() {

        // use spring security to find the logged-in users email address(username)
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = ((CustomUserDetails) principal).getUsername();

        // use this email address to obtain the rest of the users data
        return userDao.findByEmail(userEmail);

    }
}