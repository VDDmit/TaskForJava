package ru.vddmit.repository;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.Coffee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CoffeeRepositoryImpl implements CoffeeRepository {
    private final Connection connection;

    @Override
    public void save(Coffee coffee) {
        String sql = "INSERT INTO coffee (name, price) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, coffee.getName());
            preparedStatement.setDouble(2, coffee.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Coffee not added", e);
        }
    }

    @Override
    public void update(Coffee coffee) {
        String sql = "UPDATE coffee SET name = ?, price = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, coffee.getName());
            preparedStatement.setDouble(2, coffee.getPrice());
            preparedStatement.setLong(3, coffee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update failed", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM coffee WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete coffee with id = " + id + " failed", e);
        }
    }


    @Override
    public Coffee findById(Long id) {
        String sql = "SELECT * FROM coffee WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Coffee(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getDouble("price"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching coffee by ID " + id, e);
        }
        return null;
    }

    @Override
    public List<Coffee> findAll() {
        List<Coffee> coffees = new ArrayList<>();
        String sql = "SELECT * FROM coffee";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                coffees.add(new Coffee(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getDouble("price")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all coffees", e);
        }
        return coffees;
    }


}
