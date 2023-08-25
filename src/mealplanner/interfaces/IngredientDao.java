package mealplanner.interfaces;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;

import java.util.List;

public interface IngredientDao {
    List<Ingredient> findAll();
    List<Ingredient> findByMealId(int meal_id);
    Ingredient find(Ingredient ingredient);
    void add(Ingredient ingredient);
    void update(Ingredient ingredient);
    void delete(Ingredient ingredient);
}
