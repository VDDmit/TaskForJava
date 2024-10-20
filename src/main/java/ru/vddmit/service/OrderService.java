package ru.vddmit.service;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.Order;
import ru.vddmit.repository.OrderRepository;

import java.util.List;

@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(long id) {
        orderRepository.delete(id);
    }
}