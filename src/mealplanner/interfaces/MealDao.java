package mealplanner.interfaces;

import mealplanner.dictionaries.MealType;
import mealplanner.entities.Meal;

import java.util.List;

public interface MealDao {
    List<Meal> findAll();
    Integer find(MealType mealType, String mealTitle);
    List<Meal> findByMealType(MealType mealType);
    void add(Meal meal);
    void update(Meal meal);
    void delete(Meal meal);
}
