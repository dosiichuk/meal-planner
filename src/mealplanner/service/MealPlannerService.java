package mealplanner.service;

import mealplanner.Logger;
import mealplanner.Validator;
import mealplanner.dictionaries.LoggerPrompts;
import mealplanner.dictionaries.MealType;
import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MealPlannerService {
    private Logger logger;
    private List<Meal> mealList;
    private static List<Ingredient> ingredientList;
    private DbMealDao dbMealDao;
    private final DbIngredientDao dbIngredientDao;


    public MealPlannerService(Logger logger) {
        this.logger = logger;
        this.dbMealDao = new DbMealDao();
        this.dbMealDao.initMealsTable();
        this.dbIngredientDao = new DbIngredientDao();
        this.dbIngredientDao.initIngredientsTable();
        this.mealList = dbMealDao.findAll();
        Meal.currId = mealList.size() + 1;
        this.ingredientList = dbIngredientDao.findAll();
        Ingredient.currId = ingredientList.size() + 1;
    }

    public void addMeal() {
        MealType mealType = getMealType();
        String mealTitle = getMealTitle();


        Integer meal_id = Meal.currId;
//                dbMealDao.find(mealType, mealTitle);
        List<Ingredient> ingredientList = getIngredientList(meal_id);
        Meal newMeal = new Meal(meal_id, mealType, mealTitle, ingredientList);
        dbMealDao.add(newMeal);
        ingredientList.stream().forEach(ingredient -> dbIngredientDao.add(ingredient));
        logger.log(LoggerPrompts.MEAL_ADDED.getPrompt(), false);
    }

    public MealType getMealType() {
        logger.generatePrompt(LoggerPrompts.ADD_MEAL_QUERY);
        while (true) {
            MealType mealType = MealType.getMealTypeByTitle(logger.takeUserInput());
            if (mealType == null) {
                logger.log(LoggerPrompts.INVALID_MEAL_TYPE.getPrompt(), false);
            } else {
                return mealType;
            }
        }
    }

    public String getMealTitle() {
        logger.generatePrompt(LoggerPrompts.GET_MEAL_NAME);
        String mealTitle;
        while(true) {
            mealTitle = logger.takeUserInput();
            if (!Validator.isValidAlphaString(mealTitle)) {
                logger.log(LoggerPrompts.INVALID_INGREDIENT_STRING.getPrompt(), false);
            } else {
                break;
            }
        }
        return mealTitle;
    }

    public List<Ingredient> getIngredientList(int mealId) {
        logger.generatePrompt(LoggerPrompts.GET_INGREDIENTS);
        String ingredientString;
        String[] ingredientArray;
        while(true) {
            ingredientString = logger.takeUserInput();
            ingredientArray = ingredientString.split(",");
            boolean isIngredientStringValid = Arrays.stream(ingredientArray).allMatch(s -> Validator.isValidAlphaString(s.strip()));
            if (!isIngredientStringValid) {
                logger.log(LoggerPrompts.INVALID_INGREDIENT_STRING.getPrompt(), false);
            } else {
                break;
            }
        }
        return createIngredientList(ingredientArray, mealId);
    }

    public List<Ingredient> createIngredientList(String[] ingredientArray, int mealId) {
        return Arrays.stream(ingredientArray).map(s -> new Ingredient(Ingredient.currId, mealId, s.strip())).collect(Collectors.toList());
    }

    public void showMeals() {
        logger.log(LoggerPrompts.SHOW_MEALS_QUERY.getPrompt(), false);
        while (true) {
            String userInput = logger.takeUserInput();
            MealType mealType = MealType.getMealTypeByTitle(userInput);
            if (mealType == null) {
                logger.log(LoggerPrompts.INVALID_MEAL_TYPE.getPrompt(), false);
            } else {
                List<Meal> mealList = dbMealDao.findByMealType(mealType);
                if (mealList.size() == 0) {
                    logger.log(LoggerPrompts.NOTHING_TO_SHOW.getPrompt(), false);
                    break;
                } else {
                    logger.log("", false);
                    logger.log("Category: %s\n", true, mealType.getTitle());
                    mealList.forEach(meal -> logger.log(meal.toString(), false));
                    break;
                }
            }
        }
    }

}
