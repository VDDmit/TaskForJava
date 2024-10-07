package ru.vddmit.repository;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.Order;
import ru.vddmit.model.Customer;
import ru.vddmit.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final Connection connection;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void addOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, order.getCustomer().getId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long orderId = generatedKeys.getLong(1);
                for (OrderItem item : order.getItems()) {
                    orderItemRepository.addOrderItem(item, orderId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add order", e);
        }
    }

    @Override
    public Order getOrderById(long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Customer customer = customerRepository.findById(resultSet.getLong("customer_id"));
                List<OrderItem> items = orderItemRepository.getOrderItemsByOrderId(id);
                return new Order(id, customer, items);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error when receiving order by ID " + id, e);
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                long orderId = resultSet.getLong("id");
                Customer customer = customerRepository.findById(resultSet.getLong("customer_id"));
                List<OrderItem> items = orderItemRepository.getOrderItemsByOrderId(orderId);
                orders.add(new Order(orderId, customer, items));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error receiving all orders", e);
        }
        return orders;
    }

    @Override
    public void deleteOrder(long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            orderItemRepository.deleteOrderItemsByOrderId(id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order with ID " + id, e);
        }
    }
}
