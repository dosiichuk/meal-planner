package mealplanner;

import mealplanner.dictionaries.LoggerPrompts;

import java.util.Scanner;

public class Logger {
    private Scanner scanner = new Scanner(System.in);

    public Logger() {
    }

    public String takeUserInput() {
        String userInput = scanner.nextLine();
        return userInput;
    }

    public void generatePrompt(LoggerPrompts loggerPrompt) {
        System.out.println(loggerPrompt.getPrompt());
    }

    public void log(String log, boolean isFormattedString, String... args) {
        if (isFormattedString) {
            System.out.printf(log + "\n", args);
        } else {
            System.out.println(log);
        }
    }
}
