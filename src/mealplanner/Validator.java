package mealplanner;

import java.util.regex.Pattern;

public class Validator {

    public static boolean isValidAlphaString(String mealTitle) {
        return Pattern.compile("[A-Za-z ]+").matcher(mealTitle).matches();
    }

}
