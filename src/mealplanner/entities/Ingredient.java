package mealplanner.entities;

import java.util.Objects;

public class Ingredient {
    private int id;
    private int mealId;
    private String title;
    public static int currId = 0;

    public Ingredient(int id, int mealId, String title) {
        this.id = id;
        this.mealId = mealId;
        this.title = title;
        currId++;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public int getMealId() {
        return mealId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }
}
