# Online Recipe Sharing Platform

A small Java desktop application (single-file) that demonstrates basic OOP, collections, multithreading, JDBC (MySQL), and a Swing GUI for adding and loading recipes.

This repository is intended as an educational/demo project — not production-ready.

Contents
- `RecipeManagementSystem.java` — Main application (Swing GUI + DAO + JDBC + in-memory collection + multithreading).
- `sql.sql` — SQL script to create the database/table and insert a sample record.

Key features
- Add recipes locally (in-memory) and attempt to save them to a MySQL database.
- Load recipes from the database and display them in the GUI.
- Demonstrates interfaces, abstract classes, collections (ArrayList, HashMap), synchronization, JDBC DAO pattern, background threads, and Swing UI code.

Prerequisites
- JDK 8 or higher
- MySQL server (or compatible)
- MySQL Connector/J (JDBC driver). Download from MySQL or install via your build system.
- Basic familiarity with running Java programs and executing SQL scripts

Quick start

1) Clone the repository
   git clone https://github.com/utkarshSCSE/Online-Recipe-Sharing-Platform.git
   cd Online-Recipe-Sharing-Platform

2) Create the database and table
   Run the provided SQL script (`sql.sql`) with your MySQL client. Example using the mysql CLI:
   mysql -u root -p < sql.sql

   The script:
   - creates database `recipes_db` (if not exists),
   - creates `recipes` table with a unique constraint on (title, author),
   - inserts a sample "Pasta" recipe if it doesn't already exist.

3) Configure the JDBC connection
   Open `RecipeManagementSystem.java` and edit the database credentials in `DatabaseConnection` if needed:

   private static final String URL = "jdbc:mysql://localhost:3306/recipes_db?serverTimezone=UTC";
   private static final String USER = "root";
   private static final String PASSWORD = "your_mysql_password";

   IMPORTANT: Do not commit real production passwords into version control. For local testing, replace with your local credential. For better security, refactor the code to read credentials from environment variables or a config file.

4) Add MySQL Connector/J to the classpath
   - If running from the command line, download the Connector/J .jar and use `-cp` when running the app.
   - If using an IDE, add the connector as a library dependency.

Build & run (example using command line)
- Compile:
  javac -cp .:mysql-connector-java-8.0.x.jar RecipeManagementSystem.java

- Run:
  java -cp .:mysql-connector-java-8.0.x.jar RecipeManagementSystem

(Windows: replace `:` with `;` in the classpath)

Usage
- Start the app; a small Swing window appears.
- Fill in Title, Author and Ingredients and click "Add & Save".
  - The recipe is added to the in-memory collection and the app attempts to save it to the DB via the DAO.
  - If the DB save fails, the app will keep the local copy and display a warning in the output area.
- Click "Load from DB" to fetch and display recipes stored in the database.

Notes & limitations
- This project is a simple single-file demo. Consider refactoring into multiple classes/files and adding tests for real projects.
- The app currently hard-codes DB credentials inside `DatabaseConnection`. Use environment variables or external configuration for better security.
- The GUI is minimal and not internationalized.
- The SQL script enforces a uniqueness constraint on (title, author) to reduce duplicates.
- Error handling is basic — improve for production (retries, validation, prepared statement checks, logging).

Possible improvements / contributions
- Split code into packages and separate files (DAO, model, UI).
- Add unit/integration tests for DAO logic (use an embedded DB for CI).
- Add better validation, logging, and error reporting.
- Package as an executable JAR and include the JDBC driver or use a build tool (Maven/Gradle).
- Migrate to a client-server architecture (REST backend + web/mobile clients).

License
- No license is specified in this repository. Add a LICENSE file if you want to apply an open-source license.

Contact
- Repository owner: @utkarshSCSE
