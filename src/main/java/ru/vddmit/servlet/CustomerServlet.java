package ru.vddmit.servlet;

import com.google.gson.Gson;
import ru.vddmit.model.Customer;
import ru.vddmit.service.CustomerService;
import ru.vddmit.repository.CustomerRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private CustomerService customerService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        CustomerRepository customerRepository = (CustomerRepository) getServletContext().getAttribute("customerRepository");

        if (customerRepository == null) {
            throw new ServletException("CustomerRepository not found in ServletContext");
        }
        customerService = new CustomerService(customerRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String idParam = req.getParameter("id");

        if (idParam != null) {
            long id = Long.parseLong(idParam);
            Customer customer = customerService.getCustomerById(id);
            if (customer != null) {
                String jsonResponse = gson.toJson(customer);
                resp.getWriter().write(jsonResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"message\":\"Customer not found\"}");
            }
        } else {
            List<Customer> customers = customerService.getAllCustomers();
            String jsonResponse = gson.toJson(customers);
            resp.getWriter().write(jsonResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
