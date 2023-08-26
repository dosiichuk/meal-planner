package mealplanner.interfaces;

import mealplanner.entities.Meal;

import java.util.List;

public interface PlanDao {
    void add(String mealOption, String mealCategory, String weekday, int mealId);
    List<Meal> findAll();
    List<Meal> findByWeekday(String weekday);
}
