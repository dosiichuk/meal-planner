package mealplanner.dictionaries;

public enum LoggerPrompts {
    ADD_MEAL_QUERY("Which meal do you want to add (breakfast, lunch, dinner)?"),
    GET_MEAL_NAME("Input the meal's name:"),
    GET_INGREDIENTS("Input the ingredients:");

    private String prompt;

    LoggerPrompts(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
