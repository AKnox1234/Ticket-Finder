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

    @Autowired
    OrderDao orderDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ConcertDao concertDao;

    @GetMapping("order-list")
    public String orderList(Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = ((CustomUserDetails)principal).getUsername();
        User user = userDao.findByEmail(userEmail);
        List<Order> usersOrders = orderDao.getAllUsersOrders(user);

        model.addAttribute("usersOrders", usersOrders);

        return "order-list";
    }

    @PostMapping("add-order")
    public String addOrder(HttpServletRequest request) {

        int concertId = Integer.parseInt(request.getParameter("concertId"));
        int ticketQuantity = Integer.parseInt(request.getParameter("quantity"));
        String seatType = request.getParameter("seatType");

        Concert concert = concertDao.getConcertById(concertId);

        // calculate price for order
        float price = orderDao.calcConcertPrice(concertId, seatType);
        price *= ticketQuantity;

        // removing entries from remaining tickets pool according to order's ticket quantity
        concertDao.removeTicketsForConcert(concertId, seatType, ticketQuantity);

        Order order = new Order();
        order.setConcert(concert);
        order.setTicketQuantity(ticketQuantity);
        order.setPrice(price);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = ((CustomUserDetails)principal).getUsername();
        User user = userDao.findByEmail(userEmail);

        orderDao.addOrder(order, user.getId());

        return "redirect:/";

    }

    @GetMapping("delete-order")
    public String deleteOrder(HttpServletRequest request) {

        int orderId = Integer.parseInt(request.getParameter("id"));
        orderDao.deleteOrder(orderId);

        return "redirect:order-list";

    }
}