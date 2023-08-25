package mealplanner.service;

import mealplanner.entities.Ingredient;
import mealplanner.interfaces.IngredientDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbIngredientDao implements IngredientDao {

    private static String DB_URL = "jdbc:postgresql://localhost:5432/meals_db";
    private static String USER = "postgres";
    private static String PASS = "1111";
    private static String CREATE_INGREDIENTS_TABLE = "CREATE TABLE IF NOT EXISTS ingredients(ingredient VARCHAR(100),\n" +
                                                                                             "ingredient_id INTEGER PRIMARY KEY,\n" +
                                                                                             "meal_id INTEGER REFERENCES meals(meal_id))";
    private final String INSERT = "INSERT INTO ingredients (ingredient, ingredient_id, meal_id) VALUES (?, ?, ?)";
    private final String FIND_ALL = "SELECT * FROM ingredients";
    private final String FIND_BY_MEAL = "SELECT * FROM ingredients WHERE meal_id = ?";

    public DbIngredientDao() {
    }

    public void initIngredientsTable() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(CREATE_INGREDIENTS_TABLE)
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(Ingredient ingredient) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(INSERT)
        ) {
            preparedStatement.setString(1, ingredient.getTitle());
            preparedStatement.setInt(2, ingredient.getId());
            preparedStatement.setInt(3, ingredient.getMealId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ingredient> findAll() {
        List<Ingredient> ingredientList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                String ingredient = resultSet.getString("ingredient");
                int id = resultSet.getInt("ingredient_id");
                int meal_id = resultSet.getInt("meal_id");
                ingredientList.add(new Ingredient(id, meal_id, ingredient));
            }
            return ingredientList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredientList;
    }

    public List<Ingredient> findByMealId(int meal_id) {
        List<Ingredient> ingredientList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(FIND_BY_MEAL)
        ) {
            preparedStatement.setInt(1, meal_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ingredient = resultSet.getString("ingredient");
                int id = resultSet.getInt("ingredient_id");
                ingredientList.add(new Ingredient(id, meal_id, ingredient));
            }
            return ingredientList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredientList;
    }

    public Ingredient find(Ingredient ingredient) {
        return null;
    }

    public void update(Ingredient ingredient) {

    }

    public void delete(Ingredient ingredient) {

    }
}
