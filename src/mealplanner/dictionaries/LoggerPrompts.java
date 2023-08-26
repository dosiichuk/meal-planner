package mealplanner.dictionaries;

public enum LoggerPrompts {
    TO_DO_COMMAND("What would you like to do (add, show, plan, exit)?"),
    ADD_MEAL_QUERY("Which meal do you want to add (breakfast, lunch, dinner)?"),
    SHOW_MEALS_QUERY("Which category do you want to print (breakfast, lunch, dinner)?"),
    GET_MEAL_NAME("Input the meal's name:"),
    GET_INGREDIENTS("Input the ingredients:"),
    NOTHING_TO_SHOW("No meals found."),
    INVALID_MEAL_TYPE("Wrong meal category! Choose from: breakfast, lunch, dinner."),
    INVALID_INGREDIENT_STRING("Wrong format. Use letters only!"),
    MEAL_ADDED("The meal has been added!"),
    BYE("Bye!");

    private String prompt;

    LoggerPrompts(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
