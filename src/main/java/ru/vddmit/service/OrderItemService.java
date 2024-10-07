package ru.vddmit.service;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.OrderItem;
import ru.vddmit.repository.OrderItemRepository;

import java.util.List;

@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public void addOrderItem(OrderItem orderItem, long orderId) {
        orderItemRepository.addOrderItem(orderItem, orderId);
    }

    public List<OrderItem> getOrderItemsByOrderId(long orderId) {
        return orderItemRepository.getOrderItemsByOrderId(orderId);
    }

    public void deleteOrderItemsByOrderId(long orderId) {
        orderItemRepository.deleteOrderItemsByOrderId(orderId);
    }
}