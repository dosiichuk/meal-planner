package mealplanner.dictionaries;

public enum Weekday {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String dayName;

    Weekday(String dayName) {
        this.dayName = dayName;
    }

    public String getDayName() {
        return dayName;
    }

    public static Weekday getWeekdayByName(String dayName) {
        for (Weekday weekdays: Weekday.values()) {
            if (weekdays.getDayName().equals(dayName)) {
                return weekdays;
            }
        }
        return null;
    }
}
