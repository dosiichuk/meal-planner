package mealplanner;

import mealplanner.service.MealPlannerService;

public class MealPlanner implements Runnable {
    private Logger logger;
    private MealPlannerService mealPlannerService;

    public MealPlanner() {
        this.logger = new Logger();
        this.mealPlannerService = new MealPlannerService(this.logger);
    }

    @Override
    public void run() {
        mealPlannerService.addMeal();
    }
}
