package main.java.ORM;

import main.java.DomainModel.Item;

import java.sql.*;
import java.util.ArrayList;

public class ItemDAO {

    private Connection con;

    public ItemDAO() {
        try{
            this.con = ConnectionManager.getInstance().getConnection();
        }
        catch(SQLException | ClassNotFoundException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void addItem(String name, String description, String type, float price, int discount) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;

        String sqlStatement = "INSERT INTO Item (name, description, type, price, discount) VALUES (?, ?, ?, ?, ?)";

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.setString(1, name);
            pStatement.setString(2, description);
            pStatement.setString(3, type);
            pStatement.setFloat(4, price);
            pStatement.setInt(5, discount);
            pStatement.executeUpdate();
            System.out.println("Item added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public void removeItem(int id) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;

        String sqlStatement = "DELETE FROM Item WHERE id = ?";

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.setInt(1, id);
            pStatement.executeUpdate();
            int rowsAffected = pStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item found with id " + id);
            } else {
                System.out.println("Item deleted successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public Item getItem(int id) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;
        ResultSet resultSet = null;
        Item item = null;

        try {
            String sqlStatement = String.format("SELECT * FROM Item WHERE id = %d", id);
            pStatement = con.prepareStatement(sqlStatement);
            resultSet = pStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String type = resultSet.getString("type");
                float price = resultSet.getFloat("price");
                int discount = resultSet.getInt("discount");

                item = new Item(id, name, description, type, price, discount);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (pStatement != null) pStatement.close();
        }

        return item;
    }

    public ArrayList<Item> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> items = new ArrayList<Item>();

        String sqlStatement = String.format("SELECT * FROM Item");

        PreparedStatement pStatement = null;
        ResultSet resultSet = null;

        try {
            pStatement = con.prepareStatement(sqlStatement);
            resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getString("type"), resultSet.getFloat("price"), resultSet.getInt("discount"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null) { pStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return items;
    }

    public ArrayList<Item> getAllDiscountedItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> ditems = new ArrayList<Item>();

        String sqlStatement = String.format("SELECT * FROM Item WHERE discount > %d", 0);

        PreparedStatement pStatement = null;
        ResultSet resultSet = null;

        try {
            pStatement = con.prepareStatement(sqlStatement);
            resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getString("type"), resultSet.getFloat("price"), resultSet.getInt("discount"));
                ditems.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null) { pStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        }

        return ditems;
    }

    public void updateDiscount(int id, int discount) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;
        String sqlStatement = "UPDATE Item SET discount = ? WHERE id = ?";

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.setInt(1, discount);
            pStatement.setInt(2, id);
            pStatement.executeUpdate();
            int rowsAffected = pStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item found with id " + id);
            } else {
                System.out.println("Item (discount) updated successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null) {
                pStatement.close();
            }
        }
    }

    public void updatePrice(int id, float price) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;
        String sqlStatement = "UPDATE Item SET price = ? WHERE id = ?";

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.setFloat(1, price);
            pStatement.setInt(2, id);
            pStatement.executeUpdate();
            int rowsAffected = pStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item found with id " + id);
            } else {
                System.out.println("Item (price) updated successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null) {
                pStatement.close();
            }
        }
    }

    public void updateDescription(int id, String description) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;
        String sqlStatement = "UPDATE Item SET description = ? WHERE id = ?";

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.setString(1, description);
            pStatement.setInt(2, id);
            pStatement.executeUpdate();
            int rowsAffected = pStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item found with id " + id);
            } else {
                System.out.println("Item (description) updated successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null) {
                pStatement.close();
            }
        }
    }
}
