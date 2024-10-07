package ru.vddmit.repository;

import ru.vddmit.model.Order;

import java.util.List;

public interface OrderRepository {
    void addOrder(Order order);

    Order getOrderById(long id);

    List<Order> getAllOrders();

    void deleteOrder(long id);
}
