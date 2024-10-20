package ru.vddmit.servlet;

import com.google.gson.Gson;
import ru.vddmit.dto.CustomerDTO;
import ru.vddmit.model.Customer;
import ru.vddmit.service.CustomerService;
import ru.vddmit.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomerService customerService = ServletUtils.getServiceFromContext(
                getServletContext(), "customerService", CustomerService.class);

        String idParam = req.getParameter("id");
        if (idParam != null) {
            Long id = Long.parseLong(idParam);
            CustomerDTO customerDTO = customerService.getCustomerWithOrders(id);
            if (customerDTO != null) {
                String json = gson.toJson(customerDTO);
                resp.getWriter().write(json);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"message\":\"Customer not found\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Invalid customer ID\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomerService customerService = ServletUtils.getServiceFromContext(getServletContext(), "customerService", CustomerService.class);

        resp.setContentType("application/json");
        BufferedReader reader = req.getReader();
        Customer customer = gson.fromJson(reader, Customer.class);

        if (customer != null && customer.getName() != null && !customer.getName().trim().isEmpty() && customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
            customerService.addCustomer(customer);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Customer added successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Invalid customer data\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomerService customerService = ServletUtils.getServiceFromContext(getServletContext(), "customerService", CustomerService.class);

        String email = req.getParameter("email");

        if (email != null && !email.trim().isEmpty()) {
            customerService.deleteCustomerByEmail(email);
            resp.getWriter().write("{\"message\":\"Customer deleted successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Invalid email\"}");
        }
    }
}
