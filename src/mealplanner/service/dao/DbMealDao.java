package mealplanner.service.dao;

import mealplanner.dictionaries.MealType;
import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;
import mealplanner.interfaces.MealDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbMealDao implements MealDao {
    private final DbIngredientDao dbIngredientDao = new DbIngredientDao();
    private static String DB_URL = "jdbc:postgresql://localhost:5432/meals_db";
    private static String USER = "postgres";
    private static String PASS = "1111";
    public final String CREATE_MEALS_TABLE = "CREATE TABLE IF NOT EXISTS meals(category VARCHAR(100),\n" +
                                                                               "meal VARCHAR(100),\n" +
                                                                               "meal_id INTEGER PRIMARY KEY)";
    private final String INSERT = "INSERT INTO meals (category, meal, meal_id) VALUES (?, ?, ?)";
    private final String FIND_ALL = "SELECT * FROM meals";
    private final String FIND_BY_MEAL_TYPE = "SELECT * FROM meals WHERE category = ?";
    private final String FIND_ONE = "SELECT * FROM meals WHERE category = ? and meal = ?";

    public DbMealDao() {
    }

    public void initMealsTable() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(CREATE_MEALS_TABLE)
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(Meal meal) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(INSERT)
        ) {
            preparedStatement.setString(1, meal.getMealType().getTitle());
            preparedStatement.setString(2, meal.getTitle());
            preparedStatement.setInt(3, meal.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Meal> findAll() {
        List<Meal> mealList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                String meal = resultSet.getString("meal");
                int id = resultSet.getInt("meal_id");
                List<Ingredient> ingredientList = dbIngredientDao.findByMealId(id);
                mealList.add(new Meal(id, MealType.getMealTypeByTitle(category), meal, ingredientList));
            }
            return mealList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealList;
    }

    public Integer find(MealType mealType, String mealTitle) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(FIND_ONE);

        ) {
            preparedStatement.setString(1, mealType.getTitle());
            preparedStatement.setString(2, mealTitle);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("meal_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Meal> findByMealType(MealType mealType) {
        List<Meal> mealList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(FIND_BY_MEAL_TYPE);
        ) {
            preparedStatement.setString(1, mealType.getTitle());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                String meal = resultSet.getString("meal");
                int id = resultSet.getInt("meal_id");
                List<Ingredient> ingredientList = dbIngredientDao.findByMealId(id);
                mealList.add(new Meal(id, MealType.getMealTypeByTitle(category), meal, ingredientList));
            }
            return mealList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealList;
    }

    public void update(Meal meal) {

    }

    public void delete(Meal meal) {

    }
}
