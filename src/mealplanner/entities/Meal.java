package mealplanner.entities;

import mealplanner.dictionaries.MealType;

import java.util.List;
import java.util.stream.Collectors;

public class Meal {

    private String title;
    private MealType mealType;
    private List<Ingredient> ingredientList;

    public Meal(MealType mealType, String title, List<Ingredient> ingredientList) {
        this.mealType = mealType;
        this.title = title;
        this.ingredientList = ingredientList;
    }

    public MealType getMealType() {
        return mealType;
    }

    public String getTitle() {
        return title;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    @Override
    public String toString() {
        return String.format("""
                Category: %s
                Name: %s
                Ingredients:
                %s
                The meal has been added!
                """, this.mealType.getTitle(), this.title,
                String.join("\n ",
                        ingredientList
                                .stream()
                                .map(ingredient -> ingredient.getTitle())
                                .collect(Collectors.toList())));
    }
}
