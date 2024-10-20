package ru.vddmit.repository;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.OrderItem;
import ru.vddmit.model.Coffee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {
    private final Connection connection;
    private final CoffeeRepository coffeeRepository;

    @Override
    public void addOrderItem(OrderItem orderItem, long orderId) {
        String sql = "INSERT INTO order_item (order_id, coffee_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.setLong(2, orderItem.getCoffee().getId());
            preparedStatement.setInt(3, orderItem.getQuantity());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add order item", e);
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(long orderId) {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Coffee coffee = coffeeRepository.findById(resultSet.getLong("coffee_id"));
                items.add(new OrderItem(coffee, resultSet.getInt("quantity")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error when retrieving order items for order_id = " + orderId, e);
        }
        return items;
    }

    @Override
    public void deleteOrderItemsByOrderId(long orderId) {
        String sql = "DELETE FROM order_item WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error when deleting order items for order_id = " + orderId, e);
        }
    }
}
