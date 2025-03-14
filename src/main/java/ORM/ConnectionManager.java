package main.java.ORM;

import java.sql.*;

public class ConnectionManager {
    private static final String username1 = "tonky";
    private static final String password1 = "Aglio.2022!!";
    private static final String username2 = "guillermo31415";
    private static final String password2 = "Salo123";
    private static final String url = "jdbc:postgresql://localhost:5432/breakingbread";
    private static Connection connection = null;
    private static ConnectionManager instance = null; // SINGLETON

    private ConnectionManager() {}

    public static ConnectionManager getInstance() {

        if (instance == null) { instance = new ConnectionManager(); }

        return instance;

    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");

        if (connection == null)
            try {
                connection = DriverManager.getConnection(url, username1, password1);
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }

        if (connection == null)
            try {
                connection = DriverManager.getConnection(url, username2, password2);
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }

        return connection;

    }


}
