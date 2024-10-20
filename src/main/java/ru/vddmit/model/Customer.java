package ru.vddmit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vddmit.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long id;
    private String name;
    private String email;

    private List<Long> orderIds;

    public List<Order> getOrders(CustomerService customerService) {
        return customerService.getOrdersForCustomer(this);
    }

    public Customer(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.orderIds = new ArrayList<>();
    }
}
