package com.recipe.dao;

import com.recipe.model.Recipe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    private String url = "jdbc:mysql://localhost:3306/recipes_db";
    private String user = "root";
    private String pass = "pandey@12345";

    public void insertRecipe(Recipe r) throws SQLException {
        String sql = "INSERT INTO recipes (title, ingredients, author, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getTitle());
            ps.setString(2, r.getIngredients());
            ps.setString(3, r.getAuthor());
            ps.setString(4, r.getDescription());
            ps.executeUpdate();
        }
    }

    public List<Recipe> fetchAllRecipes() throws SQLException {
        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT * FROM recipes ORDER BY created_at DESC";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Recipe(rs.getInt("id"), rs.getString("title"), 
                       rs.getString("ingredients"), rs.getString("author"), rs.getString("description")));
            }
        }
        return list;
    }
}