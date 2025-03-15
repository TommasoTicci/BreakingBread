package main.java.ORM;

import java.sql.*;

public class AdminDAO {

    private Connection con;

    public AdminDAO() {
        try{
            this.con = ConnectionManager.getInstance().getConnection();
        }
        catch(SQLException | ClassNotFoundException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void clearDatabase(String sqlStatement) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;
        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.executeUpdate();
            System.out.println("Database cleared.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public void generateDummyData(String sqlStatement) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;
        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.executeUpdate();
            System.out.println("Dummy data generated.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }

    }

}
