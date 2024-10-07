package ru.vddmit.repository;

import ru.vddmit.model.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    void addOrderItem(OrderItem orderItem, long orderId);

    List<OrderItem> getOrderItemsByOrderId(long orderId);

    void deleteOrderItemsByOrderId(long orderId);
}