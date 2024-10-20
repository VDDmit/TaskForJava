package ru.vddmit.repository;

import ru.vddmit.model.Order;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order> {

    List<Order> findOrdersByIds(List<Long> orderIds);
}
