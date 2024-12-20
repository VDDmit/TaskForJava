package ru.vddmit.service;

import lombok.RequiredArgsConstructor;
import ru.vddmit.model.Coffee;
import ru.vddmit.repository.CoffeeRepository;

import java.util.List;

@RequiredArgsConstructor
public class CoffeeService {
    private final CoffeeRepository coffeeRepository;

    public void addCoffee(Coffee coffee) {
        coffeeRepository.save(coffee);
    }

    public Coffee getCoffeeById(long id) {
        return coffeeRepository.findById(id);
    }

    public List<Coffee> getAllCoffees() {
        return coffeeRepository.findAll();
    }

    public void updateCoffee(Coffee coffee) {
        coffeeRepository.update(coffee);
    }

    public void deleteCoffee(long id) {
        coffeeRepository.delete(id);
    }

}
