package com.foodallergy.app.meals;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MealsRepository extends CrudRepository<Meals, Integer> {
    List<Meals> findByUserId(int id);
    Meals findById(int id);
}
