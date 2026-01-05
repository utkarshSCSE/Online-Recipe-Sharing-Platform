package com.recipe.model;

public class Recipe {
    private int id;
    private String title;
    private String ingredients;
    private String author;
    private String description;

    // Constructor for creating new recipes
    public Recipe(String title, String ingredients, String author, String description) {
        this.title = title;
        this.ingredients = ingredients;
        this.author = author;
        this.description = description;
    }

    // Constructor for loading from DB
    public Recipe(int id, String title, String ingredients, String author, String description) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.author = author;
        this.description = description;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getIngredients() { return ingredients; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
}