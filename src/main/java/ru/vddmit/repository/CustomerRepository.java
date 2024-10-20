package ru.vddmit.repository;

import ru.vddmit.model.Customer;


public interface CustomerRepository extends CrudRepository<Customer> {

    void deleteByEmail(String email);

    Customer findByEmail(String email);

}
