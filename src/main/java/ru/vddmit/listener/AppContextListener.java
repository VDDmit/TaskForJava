package ru.vddmit.listener;

import ru.vddmit.db.DatabaseConnection;
import ru.vddmit.repository.*;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vddmit.service.CoffeeService;
import ru.vddmit.service.CustomerService;
import ru.vddmit.service.OrderService;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);
    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            connection = DatabaseConnection.getConnection();

            CoffeeRepository coffeeRepository = new CoffeeRepositoryImpl(connection);
            CustomerRepository customerRepository = new CustomerRepositoryImpl(connection);
            OrderItemRepository orderItemRepository = new OrderItemRepositoryImpl(connection, coffeeRepository);
            OrderRepository orderRepository = new OrderRepositoryImpl(connection, orderItemRepository, customerRepository);

            // Инициализация сервисов
            OrderService orderService = new OrderService(orderRepository);
            CustomerService customerService = new CustomerService(customerRepository, orderRepository);
            CoffeeService coffeeService = new CoffeeService(coffeeRepository);

            // Сохранение репозиториев и сервисов в контексте
            sce.getServletContext().setAttribute("coffeeRepository", coffeeRepository);
            sce.getServletContext().setAttribute("customerRepository", customerRepository);
            sce.getServletContext().setAttribute("orderItemRepository", orderItemRepository);
            sce.getServletContext().setAttribute("orderRepository", orderRepository);

            sce.getServletContext().setAttribute("orderService", orderService);
            sce.getServletContext().setAttribute("customerService", customerService);
            sce.getServletContext().setAttribute("coffeeService", coffeeService);

            logger.info("All repositories and services initialized and stored in the context.");
        } catch (SQLException e) {
            logger.error("Failed to initialize database connection", e);
            sce.getServletContext().setAttribute("dbError", e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.error("Failed to close database connection", e);
        }
    }
}
