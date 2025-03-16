package main.java.ORM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/breakingbread";
    private static final String USERNAME = "guillermo31415";
    private static final String PASSWORD = "Salo123";
    private static Connection connection = null;
    private static volatile ConnectionManager instance = null;


    private ConnectionManager() {}

    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connection to database established successfully!");
            } catch (SQLException | ClassNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }


    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection to database closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error while closing connection: " + e.getMessage());
            }
        }
    }
}