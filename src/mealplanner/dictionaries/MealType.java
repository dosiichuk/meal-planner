package mealplanner.dictionaries;

public enum MealType {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner");

    private String title;

    MealType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static MealType getMealTypeByTitle(String title) {
        for (MealType mealType: MealType.values()) {
            if (mealType.getTitle().equals(title)) {
                return mealType;
            }
        }
        return null;
    }
}
