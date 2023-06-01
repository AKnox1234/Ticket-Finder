package com.example.ticketfinder.controller;

import com.example.ticketfinder.dao.*;
import com.example.ticketfinder.entities.Order;
import com.example.ticketfinder.entities.User;
import com.example.ticketfinder.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @GetMapping("orderList")
    public String orderList(Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = ((CustomUserDetails)principal).getUsername();
        User user = userDao.findByEmail(userEmail);
        List<Order> usersOrders = orderDao.getAllUsersOrders(user);

        model.addAttribute("usersOrders", usersOrders);

        return "/orderList";
    }

    @PostMapping("addOrder")
    public String addOrder(HttpServletRequest request) {

        int concertId = Integer.parseInt(request.getParameter("concertId"));
        int ticketQuantity = Integer.parseInt(request.getParameter("quantity"));
//        int price = Integer.parseInt(request.getParameter("price"));

        Order order = new Order();
        order.setConcert(concertDao.getConcertById(concertId));
        order.setTicketQuantity(ticketQuantity);
        order.setPrice(40);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = ((CustomUserDetails)principal).getUsername();
        User user = userDao.findByEmail(userEmail);

        orderDao.addOrder(order, user.getId());

        return "redirect:/";

    }

    @GetMapping("deleteOrder")
    public String deleteOrder(HttpServletRequest request) {

        int orderId = Integer.parseInt(request.getParameter("id"));
        orderDao.deleteOrder(orderId);

        return "redirect:orderList";

    }
}