package ru.vddmit.service;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.Coffee;
import ru.vddmit.repository.CoffeeRepository;

import java.util.List;

@RequiredArgsConstructor
public class CoffeeService {
    private final CoffeeRepository coffeeRepository;

    public void addCoffee(Coffee coffee) {
        coffeeRepository.addCoffee(coffee);
    }

    public Coffee getCoffeeById(long id) {
        return coffeeRepository.getCoffeeById(id);
    }

    public List<Coffee> getAllCoffees() {
        return coffeeRepository.getAllCoffees();
    }

    public void updateCoffee(Coffee coffee) {
        coffeeRepository.updateCoffee(coffee);
    }

    public void deleteCoffee(long id) {
        coffeeRepository.deleteCoffee(id);
    }

}
