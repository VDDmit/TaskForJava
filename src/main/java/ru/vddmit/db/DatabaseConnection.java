package ru.vddmit.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://db:5432/coffee_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12341234";
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Database connection established successfully.");
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to establish database connection", e);
            throw e;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
