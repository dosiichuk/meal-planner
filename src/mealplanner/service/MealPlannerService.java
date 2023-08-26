package mealplanner.service;

import mealplanner.Logger;
import mealplanner.Validator;
import mealplanner.dictionaries.LoggerPrompts;
import mealplanner.dictionaries.MealType;
import mealplanner.dictionaries.Weekday;
import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;
import mealplanner.service.dao.DbIngredientDao;
import mealplanner.service.dao.DbMealDao;
import mealplanner.service.dao.DbPlanDao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MealPlannerService {
    private Logger logger;
    private List<Meal> mealList;
    private static List<Ingredient> ingredientList;
    private DbMealDao dbMealDao;
    private final DbIngredientDao dbIngredientDao;
    private final DbPlanDao dbPlanDao;


    public MealPlannerService(Logger logger) {
        this.logger = logger;
        this.dbMealDao = new DbMealDao();
        this.dbMealDao.initMealsTable();
        this.dbIngredientDao = new DbIngredientDao();
        this.dbIngredientDao.initIngredientsTable();
        this.dbPlanDao = new DbPlanDao();
        this.dbPlanDao.initPlanTable();
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

    public void planMeals() {
        for (Weekday weekday: Weekday.values()) {
            planMealsForWeekday(weekday);
            logger.log("Yeah! We planned the meals for %s.\n", true, weekday.getDayName());
        }
        showWeeklyPlan();
    }

    public void planMealsForWeekday(Weekday weekday) {
        logger.log(weekday.getDayName(), false);
        for (MealType mealType: MealType.values()) {
            selectMeal(mealType, weekday);
        }
    }

    public void selectMeal(MealType mealType, Weekday weekday) {
        List<Meal> mealList = dbMealDao.findByMealType(mealType);
        Collections.sort(mealList, new Comparator<Meal>() {
            @Override
            public int compare(Meal o1, Meal o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        mealList.stream().forEach(meal -> logger.log("%s", true, meal.getTitle()));
        logger.log("Choose the %s for %s from the list above:", true, mealType.getTitle(), weekday.getDayName());
        Meal selectedMeal;
        while (true) {
            String userInput = logger.takeUserInput();
            Meal identifiedMeal = identifyMealByTitle(userInput, mealList);
            if (identifiedMeal == null) {
                logger.log("This meal doesn’t exist. Choose a meal from the list above.", false);
            } else {
                dbPlanDao.add(identifiedMeal.getTitle(), identifiedMeal.getMealType().getTitle(), weekday.getDayName(), identifiedMeal.getId());
                break;
            }
        }

    }

    public Meal identifyMealByTitle(String mealTitle, List<Meal> mealList) {
        for (Meal meal: mealList) {
            if (meal.getTitle().equals(mealTitle)) {
                return meal;
            }
        }
        return null;
    }

    public void showWeeklyPlan() {
        for (Weekday weekday: Weekday.values()) {
            List<Meal> mealList = dbPlanDao.findByWeekday(weekday.getDayName());
            logger.log("", false);
            logger.log("%s", true, weekday.getDayName());
            mealList.stream().forEach(meal ->
                    logger.log("%s: %s",
                            true,
                            meal.getMealType().getTitle(),
                            meal.getTitle()));
            logger.log("", false);
        }
    }

    public void saveShoppingList() {
        Map<Ingredient, Integer> ingredientMap = new HashMap();
        List<Meal> mealList = dbPlanDao.findAll();
        if(mealList.size() ==0) {
            logger.log("Unable to save. Plan your meals first.", false);
        } else {
            mealList.stream().forEach(meal -> {
                List<Ingredient> ingredients = dbIngredientDao.findByMealId(meal.getId());
                ingredients.stream().forEach(ingredient -> {
                    if (ingredientMap.get(ingredient) == null) {
                        ingredientMap.put(ingredient, 1);
                    } else {
                        Integer currCount = ingredientMap.get(ingredient);
                        ingredientMap.put(ingredient, currCount + 1);
                    }
                });
            });
            try {
                saveListToFile(ingredientMap);
            } catch (IOException e) {

            }
        }


    }

    public void saveListToFile(Map<Ingredient, Integer> ingredientMap) throws IOException {
        logger.log("Input a filename:", false);
        String userInput = logger.takeUserInput();
        StringBuilder outputString = new StringBuilder();
        for (Map.Entry<Ingredient, Integer> ingredientIntegerEntry: ingredientMap.entrySet()) {
            if (ingredientIntegerEntry.getValue() == 1) {
                outputString.append(ingredientIntegerEntry.getKey().getTitle() + "\n");
            } else {
                outputString.append(ingredientIntegerEntry.getKey().getTitle() + " x" +ingredientIntegerEntry.getValue() + "\n");
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(userInput));
        writer.write(String.valueOf(outputString));
        writer.close();
        logger.log("Saved!", false);
    }
}
