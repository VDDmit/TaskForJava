package ru.vddmit.repository;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.Order;
import ru.vddmit.model.Customer;
import ru.vddmit.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final Connection connection;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void save(Order order) {
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
    public Order findById(Long id) {
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
    public List<Order> findAll() {
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
    public void delete(Long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            orderItemRepository.deleteOrderItemsByOrderId(id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order with ID " + id, e);
        }
    }

    @Override
    public List<Order> findOrdersByIds(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "SELECT * FROM orders WHERE id IN (" +
                     String.join(", ", Collections.nCopies(orderIds.size(), "?")) + ")";

        List<Order> orders = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < orderIds.size(); i++) {
                preparedStatement.setLong(i + 1, orderIds.get(i));
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long orderId = resultSet.getLong("id");
                    long customerId = resultSet.getLong("customer_id");
                    Customer customer = customerRepository.findById(customerId);
                    List<OrderItem> items = orderItemRepository.getOrderItemsByOrderId(orderId);

                    orders.add(new Order(orderId, customer, items));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching orders by IDs: " + orderIds, e);
        }

        return orders;
    }


}
