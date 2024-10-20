package ru.vddmit.service;

import lombok.RequiredArgsConstructor;
import ru.vddmit.dto.CustomerDTO;
import ru.vddmit.model.Customer;
import ru.vddmit.model.Order;
import ru.vddmit.repository.CustomerRepository;
import ru.vddmit.repository.OrderRepository;

import java.util.List;

@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void deleteCustomerByEmail(String email) {
        customerRepository.deleteByEmail(email);
    }

    public Customer getCustomerById(long id) {
        return customerRepository.findById(id);
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    public List<Order> getOrdersForCustomer(Customer customer) {
        return orderRepository.findOrdersByIds(customer.getOrderIds());
    }
    public CustomerDTO getCustomerWithOrders(Long customerId){
        Customer customer = customerRepository.findById(customerId);
        if(customer == null){
            throw new RuntimeException("Customer not found");
        }
        List<Order> orders = orderRepository.findOrdersByIds(customer.getOrderIds());
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail(), orders);
    }
}