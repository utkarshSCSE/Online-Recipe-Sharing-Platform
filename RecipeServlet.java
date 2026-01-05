package com.recipe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.recipe.model.Recipe;
import com.recipe.dao.RecipeDAO;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/recipeAction")
public class RecipeServlet extends HttpServlet {
    private RecipeDAO dao = new RecipeDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String ing = request.getParameter("ingredients");
        String desc = request.getParameter("description");

        // Data Validation (Rubric Requirement)
        if (title == null || title.isEmpty() || author == null || author.isEmpty()) {
            response.sendRedirect("index.jsp?error=validation");
            return;
        }

        try {
            dao.insertRecipe(new Recipe(title, ing, author, desc));
            response.sendRedirect("recipeAction"); // Redirect to doGet to show list
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("recipeList", dao.fetchAllRecipes());
            request.getRequestDispatcher("viewRecipes.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}