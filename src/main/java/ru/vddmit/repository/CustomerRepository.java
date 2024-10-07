package ru.vddmit.repository;

import ru.vddmit.model.Customer;

import java.util.List;

public interface CustomerRepository {
    void save(Customer customer);

    void deleteByEmail(String email);

    Customer findById(long id);

    Customer findByEmail(String email);

    List<Customer> findAll();
}
