package ru.vddmit.repository;


import lombok.RequiredArgsConstructor;
import ru.vddmit.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final Connection connection;


    @Override
    public void save(Customer customer) {
        String sql = "INSERT INTO customer (name, email) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Customer not saved", e);
        }
    }

    @Override
    public void deleteByEmail(String email) {
        String sql = "DELETE FROM customer WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
        } catch (SQLException e) {
            throw new RuntimeException("Customer not deleted with email = " + email, e);
        }
    }

    @Override
    public Customer findById(long id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Customer(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching customer with id = " + id, e);
        }
        return null;
    }

    @Override
    public Customer findByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Customer(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching customer with email = " + email, e);
        }
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all customers", e);
        }
        return customers;
    }
}