package main.java.BusinessLogic;

import main.java.ORM.AdminDAO;
import main.java.ORM.UserDAO;

import java.io.*;
import java.sql.SQLException;

public class AdminDatabaseOptionsController {

    //CONSTRUCTOR
    public AdminDatabaseOptionsController() {}

    public void updatePassword(String password) throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updatePassword("ADMIN", password);
    }

    public void clearDatabase() throws SQLException, ClassNotFoundException {
        StringBuilder sb_sqlStatement = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/sql/database.sql"))) {
            String line;
            while ((line = br.readLine()) != null) {sb_sqlStatement.append(line).append("\n");}
        } catch (IOException e) {
            System.err.println("Error: "+e);
            return;
        }
        String sqlStatement = sb_sqlStatement.toString();
        AdminDAO adminDAO = new AdminDAO();
        adminDAO.clearDatabase(sqlStatement);
    }

    public void dummyDataGenerator() throws SQLException, ClassNotFoundException {
        StringBuilder sb_sqlStatement = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/sql/dummydata.sql"))) {
            String line;
            while ((line = br.readLine()) != null) {sb_sqlStatement.append(line).append("\n");}
        } catch (IOException e) {
            System.err.println("Error: "+e);
            return;
        }
        clearDatabase();
        String sqlStatement = sb_sqlStatement.toString();
        AdminDAO adminDAO = new AdminDAO();
        adminDAO.generateDummyData(sqlStatement);
    }
}
