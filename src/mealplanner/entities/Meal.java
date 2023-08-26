package mealplanner.entities;

import mealplanner.dictionaries.MealType;

import java.util.List;
import java.util.stream.Collectors;

public class Meal {

    private int id;
    private String title;
    private MealType mealType;
    private List<Ingredient> ingredientList;
    public static int currId = 0;

    public Meal(int id, MealType mealType, String title, List<Ingredient> ingredientList) {
        this.id = id;
        this.mealType = mealType;
        this.title = title;
        this.ingredientList = ingredientList;
        currId++;
    }

    public MealType getMealType() {
        return mealType;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    @Override
    public String toString() {
        return String.format("""
                Name: %s
                Ingredients:
                %s
                """, this.title,
                String.join("\n",
                        ingredientList
                                .stream()
                                .map(ingredient -> ingredient.getTitle())
                                .collect(Collectors.toList())));
    }
}
