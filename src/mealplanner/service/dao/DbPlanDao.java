package mealplanner.service.dao;

import mealplanner.dictionaries.MealType;
import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;
import mealplanner.interfaces.PlanDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbPlanDao implements PlanDao {

    private static String DB_URL = "jdbc:postgresql://localhost:5432/meals_db";
    private static String USER = "postgres";
    private static String PASS = "1111";
    public final String CREATE_PLAN_TABLE = "CREATE TABLE IF NOT EXISTS plan(meal_option VARCHAR(100),\n" +
            "meal_category VARCHAR(100),\n" +
            "weekday VARCHAR(100),\n" +
            "id SERIAL PRIMARY KEY,\n" +
            "meal_id INTEGER REFERENCES meals(meal_id))";
    private final String INSERT = "INSERT INTO plan (meal_option, meal_category, weekday, meal_id) VALUES (?, ?, ?, ?)";
    private final String FIND_ALL = "SELECT * FROM plan";
    private final String FIND_BY_WEEKDAY = "SELECT * FROM plan WHERE weekday = ?";

    public DbPlanDao() {
    }

    public void initPlanTable() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(CREATE_PLAN_TABLE);

        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(String mealOption, String mealCategory, String weekday, int mealId) {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(INSERT)
        ) {
            preparedStatement.setString(1, mealOption);
            preparedStatement.setString(2, mealCategory);
            preparedStatement.setString(3, weekday);
            preparedStatement.setInt(4, mealId);
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
                String category = resultSet.getString("meal_category");
                String mealTitle = resultSet.getString("meal_option");
                int id = resultSet.getInt("meal_id");
                mealList.add(new Meal(id, MealType.getMealTypeByTitle(category), mealTitle, new ArrayList<>()));
            }
            return mealList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealList;
    }

    public List<Meal> findByWeekday(String weekday) {
        List<Meal> mealList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = con.prepareStatement(FIND_BY_WEEKDAY);
        ) {
            preparedStatement.setString(1, weekday);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String category = resultSet.getString("meal_category");
                String mealTitle = resultSet.getString("meal_option");
                int id = resultSet.getInt("meal_id");
                mealList.add(new Meal(id, MealType.getMealTypeByTitle(category), mealTitle, new ArrayList<>()));
            }
            return mealList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealList;
    }
}
