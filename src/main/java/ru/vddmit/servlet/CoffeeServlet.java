package ru.vddmit.servlet;

import com.google.gson.Gson;
import ru.vddmit.model.Coffee;
import ru.vddmit.service.CoffeeService;
import ru.vddmit.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/coffee")
public class CoffeeServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CoffeeService coffeeService = ServletUtils.getServiceFromContext(getServletContext(), "coffeeService", CoffeeService.class);

        resp.setContentType("application/json");
        String idParam = req.getParameter("id");

        if (idParam != null) {
            long id = Long.parseLong(idParam);
            Coffee coffee = coffeeService.getCoffeeById(id);
            if (coffee != null) {
                String jsonResponse = gson.toJson(coffee);
                resp.getWriter().write(jsonResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"message\":\"Coffee not found\"}");
            }
        } else {
            List<Coffee> coffees = coffeeService.getAllCoffees();
            String jsonResponse = gson.toJson(coffees);
            resp.getWriter().write(jsonResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CoffeeService coffeeService = ServletUtils.getServiceFromContext(getServletContext(), "coffeeService", CoffeeService.class);

        resp.setContentType("application/json");
        BufferedReader reader = req.getReader();
        Coffee coffee = gson.fromJson(reader, Coffee.class);

        if (coffee != null && coffee.getName() != null && !coffee.getName().trim().isEmpty() && coffee.getPrice() > 0) {
            coffeeService.addCoffee(coffee);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Coffee added successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Invalid coffee data\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CoffeeService coffeeService = ServletUtils.getServiceFromContext(getServletContext(), "coffeeService", CoffeeService.class);

        resp.setContentType("application/json");
        BufferedReader reader = req.getReader();
        Coffee coffee = gson.fromJson(reader, Coffee.class);

        if (coffee != null && coffee.getId() != null && coffee.getId() > 0 && coffee.getName() != null && !coffee.getName().trim().isEmpty() && coffee.getPrice() > 0) {
            coffeeService.updateCoffee(coffee);
            resp.getWriter().write("{\"message\":\"Coffee updated successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Invalid coffee data\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CoffeeService coffeeService = ServletUtils.getServiceFromContext(getServletContext(), "coffeeService", CoffeeService.class);

        String idParam = req.getParameter("id");

        if (idParam != null) {
            long id = Long.parseLong(idParam);
            coffeeService.deleteCoffee(id);
            resp.getWriter().write("{\"message\":\"Coffee deleted successfully\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\":\"Invalid ID\"}");
        }
    }
}
