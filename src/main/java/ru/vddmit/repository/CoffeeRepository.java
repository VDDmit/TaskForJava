package ru.vddmit.repository;

import ru.vddmit.model.Coffee;

import java.util.List;

public interface CoffeeRepository {
    void addCoffee(Coffee coffee);

    Coffee getCoffeeById(long id);

    List<Coffee> getAllCoffees();

    void updateCoffee(Coffee coffee);

    void deleteCoffee(long id);
}
