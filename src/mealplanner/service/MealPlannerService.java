package mealplanner.service;

import mealplanner.Logger;
import mealplanner.dictionaries.LoggerPrompts;
import mealplanner.dictionaries.MealType;
import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MealPlannerService {
    private Logger logger;
    private List<Meal> mealList;

    public MealPlannerService(Logger logger) {
        this.logger = logger;
        this.mealList = new ArrayList<>();
    }

    public void addMeal() {
        logger.generatePrompt(LoggerPrompts.ADD_MEAL_QUERY);
        MealType mealType = MealType.getMealTypeByTitle(logger.takeUserInput());
        logger.generatePrompt(LoggerPrompts.GET_MEAL_NAME);
        String mealTitle = logger.takeUserInput();
        logger.generatePrompt(LoggerPrompts.GET_INGREDIENTS);
        List<Ingredient> ingredientList = parseIngredientList(logger.takeUserInput());
        Meal newMeal = new Meal(mealType, mealTitle, ingredientList);
        mealList.add(newMeal);
        logger.log(newMeal.toString(), false);
    }

    public List<Ingredient> parseIngredientList(String ingredientString) {
        String[] ingredientArray = ingredientString.split(",");
        logger.log("", false);
        return Arrays.stream(ingredientArray).map(s -> new Ingredient(s)).collect(Collectors.toList());
    }

//    public void logMealInfo(Meal meal) {
//        logger.log(LoggerPrompts.CATEGORY_LOG.getPrompt(), true, meal.getMealType().getTitle());
//        logger.log(LoggerPrompts.NAME_LOG.getPrompt(), true, meal.getTitle());
//        logger.log(LoggerPrompts.INGREDIENTS_LOG.getPrompt(), false);
//        meal.getIngredientList().forEach(ingredient -> logger.log(ingredient.getTitle(), false));
//        logger.log(LoggerPrompts.MEAL_ADDED.getPrompt(), false);
//    }
}
