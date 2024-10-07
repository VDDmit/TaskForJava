package ru.vddmit.service;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.Customer;
import ru.vddmit.repository.CustomerRepository;

import java.util.List;

@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

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
}