-- Create database if not exists
CREATE DATABASE IF NOT EXISTS recipes_db;

-- Use the database
USE recipes_db;

-- Create table if not exists
CREATE TABLE IF NOT EXISTS recipes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    ingredients TEXT NOT NULL,
    author VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT unique_recipe UNIQUE (title, author)
);

-- Insert only if recipe does NOT already exist
INSERT INTO recipes (title, ingredients, author, description)
SELECT 'Pasta', 'Noodles, Cheese, Sauce', 'Utkarsh', 'A yummy pasta recipe'
WHERE NOT EXISTS (
    SELECT 1 FROM recipes WHERE title='Pasta' AND author='Utkarsh'
);

-- Show table structure
DESCRIBE recipes;

-- Show all records
SELECT * FROM recipes;
