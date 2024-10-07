package ru.vddmit.servlet;

import com.google.gson.Gson;
import ru.vddmit.model.Order;
import ru.vddmit.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private OrderService orderService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        orderService = (OrderService) getServletContext().getAttribute("orderService");

        if (orderService == null) {
            throw new ServletException("OrderService not found in ServletContext");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String idParam = req.getParameter("id");

        if (idParam != null) {
            long id = Long.parseLong(idParam);
            Order order = orderService.getOrderById(id);
            if (order != null) {
                String jsonResponse = gson.toJson(order);
                resp.getWriter().write(jsonResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"message\":\"Order not found\"}");
            }
        } else {
            List<Order> orders = orderService.getAllOrders();
            String jsonResponse = gson.toJson(orders);
            resp.getWriter().write(jsonResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        BufferedReader reader = req.getReader();
        Order order = gson.fromJson(reader, Order.class);

        if (order != null && order.getCustomer() != null && !order.getItems().isEmpty()) {
            orderService.createOrder(order);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Order added successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Invalid order data\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam != null) {
            long id = Long.parseLong(idParam);
            orderService.deleteOrder(id);
            resp.getWriter().write("{\"message\":\"Order deleted successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Invalid ID\"}");
        }
    }
}
