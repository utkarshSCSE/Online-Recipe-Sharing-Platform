/* 
  RecipeManagementSystem.java
  Single-file project that implements:
    - OOP: inheritance, polymorphism, interface
    - Collections & Generics (ArrayList, HashMap)
    - Multithreading & Synchronization
    - Database classes (Connection + DAO)
    - JDBC connectivity (MySQL)
    - Swing GUI (Add + Load)
    - Exception handling
  Save as: RecipeManagementSystem.java
*/

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

/* ===============================
   Interface: Savable (rubric)
   =============================== */
interface Savable {
    void save(); // demonstrate a common behavior
}

/* ===============================
   Base abstract class (Inheritance)
   =============================== */
abstract class Content {
    protected String title;
    abstract void display(); // polymorphic method
}

/* ===============================
   Recipe class (Inheritance + Interface)
   - implements Savable
   - extends Content
   =============================== */
class Recipe extends Content implements Savable {
    private int id;               // DB id (0 if not from DB yet)
    private String ingredients;
    private String author;

    // Constructor for new (no id)
    public Recipe(String title, String ingredients, String author) {
        this.id = 0;
        this.title = title;
        this.ingredients = ingredients;
        this.author = author;
    }

    // Constructor with id (from DB)
    public Recipe(int id, String title, String ingredients, String author) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.author = author;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getIngredients() { return ingredients; }
    public String getAuthor() { return author; }

    @Override
    public void save() {
        // simple illustrative implementation (not writing file here)
        System.out.println("[Savable] Recipe.save() called for: " + title);
    }

    @Override
    public void display() {
        System.out.println("Recipe: " + title + " by " + author);
    }

    @Override
    public String toString() {
        return title + " (by " + author + ")";
    }
}

/* ===============================
   Collections + Synchronization
   - ArrayList<Recipe> with synchronized access
   - HashMap<Integer, Recipe> to demonstrate index retrieval
   =============================== */
class RecipeCollection {
    private final List<Recipe> list = new ArrayList<>();
    private final Map<Integer, Recipe> map = new HashMap<>();
    private int idCounter = 1;

    // Adds a recipe; synchronized for thread-safety
    public synchronized int addRecipe(Recipe r) {
        list.add(r);
        int assignedId = idCounter++;
        map.put(assignedId, r);
        return assignedId;
    }

    // Returns a sorted copy of the list (by title)
    public synchronized List<Recipe> getAllRecipesSorted() {
        List<Recipe> copy = new ArrayList<>(list);
        copy.sort(Comparator.comparing(Recipe::getTitle, String.CASE_INSENSITIVE_ORDER));
        return copy;
    }

    public synchronized Recipe getRecipeByAssignedId(int assignedId) {
        return map.get(assignedId);
    }

    public synchronized int size() {
        return list.size();
    }
}

/* ===============================
   DatabaseConnection class (JDBC)
   - change USER and PASSWORD to match your MySQL
   - provides getConnection()
   =============================== */
class DatabaseConnection {
    // Update these values if different on your machine:
    private static final String URL = "jdbc:mysql://localhost:3306/recipes_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "pandey@12345"; // set to your MySQL password

    // load driver (optional for modern JDBC but kept explicit)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) { }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

/* ===============================
   DAO: RecipeDAO (Database operations)
   - insertRecipe(Recipe)
   - fetchAllRecipes() -> List<Recipe>
   Demonstrates classes for DB operations (rubric)
   =============================== */
class RecipeDAO {

    // Inserts a recipe into DB. Returns generated DB id or -1 on failure.
    public int insertRecipe(Recipe r) throws SQLException {
        String sql = "INSERT INTO recipes (title, ingredients, author) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getTitle());
            ps.setString(2, r.getIngredients());
            ps.setString(3, r.getAuthor());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    // Fetches all recipes from DB and returns a List<Recipe>
    public List<Recipe> fetchAllRecipes() throws SQLException {
        List<Recipe> result = new ArrayList<>();
        String sql = "SELECT id, title, ingredients, author FROM recipes ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String ingredients = rs.getString("ingredients");
                String author = rs.getString("author");
                result.add(new Recipe(id, title, ingredients, author));
            }
        }
        return result;
    }
}

/* ===============================
   GUI + Multithreading + App logic
   - Provides Add Recipe (local collection + DB)
   - Load Recipes from DB (uses RecipeDAO.fetchAllRecipes)
   - Uses a small background thread for startup animation
   =============================== */
public class RecipeManagementSystem extends JFrame {

    // UI components
    private final JTextField titleField = new JTextField(28);
    private final JTextField authorField = new JTextField(20);
    private final JTextArea ingredientsArea = new JTextArea(5, 28);
    private final JButton addButton = new JButton("Add & Save");
    private final JButton loadButton = new JButton("Load from DB");
    private final JTextArea outputArea = new JTextArea(12, 50);

    // App components
    private final RecipeCollection collection = new RecipeCollection();
    private final RecipeDAO dao = new RecipeDAO();

    // Constructor sets up GUI
    public RecipeManagementSystem() {
        super("Online Recipe Sharing Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Top input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add a Recipe"));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Title:"));
        row1.add(titleField);
        inputPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Author:"));
        row2.add(authorField);
        inputPanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(new JLabel("Ingredients:"));
        JScrollPane ingScroll = new JScrollPane(ingredientsArea);
        row3.add(ingScroll);
        inputPanel.add(row3);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonRow.add(addButton);
        buttonRow.add(loadButton);
        inputPanel.add(buttonRow);

        add(inputPanel);

        // Output area (read-only)
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Output / Recipes"));
        add(outputScroll);

        pack();
        setLocationRelativeTo(null); // center window

        // Button listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAddRecipe();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadRecipes();
            }
        });

        // Startup animation in background (multithreading demo)
        new Thread(() -> {
            outputArea.append("Starting application");
            try {
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(500);
                    outputArea.append(".");
                }
            } catch (InterruptedException ignored) {}
            outputArea.append("\nReady.\n");
        }, "StartupThread").start();
    }

    // Called when Add & Save clicked
    private void onAddRecipe() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String ingredients = ingredientsArea.getText().trim();

        // Exception handling & validation
        try {
            if (title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty");
            if (author.isEmpty()) throw new IllegalArgumentException("Author cannot be empty");

            Recipe r = new Recipe(title, ingredients, author);

            // add to local collection (synchronized)
            int assignedId = collection.addRecipe(r);

            // Save to DB via DAO (JDBC)
            int dbId = -1;
            try {
                dbId = dao.insertRecipe(r);
            } catch (SQLException sq) {
                // If DB fails, report but keep local copy
                outputArea.append("[WARNING] Could not save to DB: " + sq.getMessage() + "\n");
            }

            // Use polymorphic behavior: call save() (interface)
            r.save();

            // Update UI
            outputArea.append("Added locally (assignedId=" + assignedId + ")");
            if (dbId > 0) outputArea.append(", saved to DB (id=" + dbId + ")");
            outputArea.append(": " + r + "\n");

            // Clear inputs after add
            titleField.setText("");
            authorField.setText("");
            ingredientsArea.setText("");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Input error: " + ex.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            // catch-all to avoid crashing the GUI
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Called when Load from DB clicked: uses DAO.fetchAllRecipes()
    private void onLoadRecipes() {
        // Run DB fetch in a background thread to keep UI responsive (multithreading)
        new Thread(() -> {
            outputArea.append("\nLoading recipes from database...\n");
            List<Recipe> fromDb;
            try {
                fromDb = dao.fetchAllRecipes();
                if (fromDb.isEmpty()) {
                    outputArea.append("No recipes found in database.\n");
                    return;
                }
                // show fetched recipes in UI (safely append from EDT)
                SwingUtilities.invokeLater(() -> {
                    for (Recipe r : fromDb) {
                        outputArea.append("DB: " + r.getId() + " | " + r.getTitle() + " by " + r.getAuthor() + "\n");
                    }
                });
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> {
                    outputArea.append("Error loading recipes: " + e.getMessage() + "\n");
                });
            }
        }, "DBFetchThread").start();
    }

    // Main entry
    public static void main(String[] args) {
        // Ensure look & feel is cross-platform friendly (optional)
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            RecipeManagementSystem app = new RecipeManagementSystem();
            app.setVisible(true);
        });
    }
}
