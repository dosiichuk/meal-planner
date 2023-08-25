package mealplanner;

import mealplanner.dictionaries.CommandType;
import mealplanner.dictionaries.LoggerPrompts;
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
        while(true) {
            String toDo = "";
            while (!CommandType.isValidCommandType(toDo)) {
                logger.generatePrompt(LoggerPrompts.TO_DO_COMMAND);
                toDo = logger.takeUserInput();
            }
            CommandType commandType = CommandType.getCommandTypeByTitle(toDo);
            switch (commandType) {
                case ADD:
                    mealPlannerService.addMeal();
                    break;
                case SHOW:
                    mealPlannerService.showMeals();
                    break;
                case EXIT:
                    logger.log(LoggerPrompts.BYE.getPrompt(), false);
                    System.exit(0);
            }

        }

    }
}
