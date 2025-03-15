package main.java.ORM;

import main.java.DomainModel.Item;
import main.java.DomainModel.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.*;

public class OrderDAO {

    private Connection con;

    public OrderDAO() {
        try{
            this.con = ConnectionManager.getInstance().getConnection();
        }
        catch(SQLException | ClassNotFoundException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void createOrder(int userId) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;

        String sqlStatement = String.format("INSERT INTO Orders (userid, status, date) " +
                "VALUES (%d, '%s', '%s')", userId, "Received", LocalDate.now());

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.executeUpdate();
            System.out.println("Order inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public void createOrderItem(int orderId, ArrayList<Item> items) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;

        String sqlStatement = String.format("INSERT INTO OrdersItem (orderid, itemid) VALUES (?, ?)");

        try {
            pStatement = con.prepareStatement(sqlStatement);

            for (Item item : items) {
                pStatement.setInt(1, orderId);
                pStatement.setInt(2, item.getItemId());
                pStatement.addBatch();
            }

            pStatement.executeBatch();
            System.out.println("Items inserted successfully to Order.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public void removeOrder(int orderId) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;

        String sqlStatement = String.format("DELETE FROM Orders WHERE orderid = %d", orderId);

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.executeUpdate();
            System.out.println("Orders deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public void removeOrderItem(int orderId) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;

        String sqlStatement = String.format("DELETE FROM OrdersItem WHERE orderid = %d", orderId);

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.executeUpdate();
            System.out.println("Item deleted successfully from Order.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public void removeOrderByUser(int userId) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;

        String sqlStatement = String.format("DELETE FROM Orders WHERE userid = %d", userId);

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.executeUpdate();
            System.out.println("Orders by user#"+ userId +" deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public void removeOrderItemByUser(int userId) throws SQLException, ClassNotFoundException {
        PreparedStatement selectStatement = null;
        PreparedStatement deleteStatement = null;
        ResultSet resultSet = null;

        try {
            // Recupero dell'orderId associato all'utente
            String selectSQL = String.format("SELECT id FROM Orders WHERE userid = %d", userId);
            selectStatement = con.prepareStatement(selectSQL);
            resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");

                // Eliminazione degli elementi dall'ordine
                String deleteSQL = String.format("DELETE FROM OrdersItem WHERE orderid = ?");
                deleteStatement = con.prepareStatement(deleteSQL);
                deleteStatement.setInt(1, orderId);
                deleteStatement.executeUpdate();

                System.out.println("Item from Orders by user#" + userId + " deleted successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (selectStatement != null) selectStatement.close();
            if (deleteStatement != null) deleteStatement.close();
        }
    }

    public void removeCompletedOrders() throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;
        String c = "Completed";

        String sqlStatement = String.format("DELETE FROM Orders WHERE status = '%s'", c);

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.executeUpdate();
            System.out.println("Completed Orders deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    public void removeCompletedOrderItem() throws SQLException, ClassNotFoundException {
        PreparedStatement selectStatement = null;
        PreparedStatement deleteStatement = null;
        ResultSet resultSet = null;
        String c = "Completed";

        try {
            // Recupero dell'orderId completati
            String selectSQL = String.format("DELETE FROM Orders WHERE status = '%s'", c);
            selectStatement = con.prepareStatement(selectSQL);
            resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");

                // Eliminazione degli elementi di ordini completati
                String deleteSQL = String.format("DELETE FROM OrdersItem WHERE orderid = ?");
                deleteStatement = con.prepareStatement(deleteSQL);
                deleteStatement.setInt(1, orderId);
                deleteStatement.executeUpdate();

                System.out.println("Item from completed Orders deleted successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (selectStatement != null) selectStatement.close();
            if (deleteStatement != null) deleteStatement.close();
        }
    }

    //TODO: ??? rimuovere ordine da data

    public void removeItemsFromEveryOrderItems(int itemId) throws SQLException, ClassNotFoundException {
        PreparedStatement pStatement = null;

        String sqlStatement = String.format("DELETE FROM OrdersItem WHERE itemid = %d", itemId);

        try {
            pStatement = con.prepareStatement(sqlStatement);
            pStatement.executeUpdate();
            System.out.println("Item deleted successfully from every Orders.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (pStatement != null)
                pStatement.close();
        }
    }

    //TODO: NECESSITO DELLO USERDAO E ITEMDAO

    /*
    //BOZZA: DA MODIFICARE (ovviamente)

    public Order getOrder(int orderId) throws SQLException, ClassNotFoundException {
        PreparedStatement orderStatement = null;
        PreparedStatement itemsStatement = null;
        ResultSet orderResult = null;
        ResultSet itemsResult = null;
        Order order = null;
        ArrayList<Item> items = new ArrayList<>();

        try {
            // Recupero i dettagli dell'ordine
            String orderSQL = String.format("SELECT * FROM Orders WHERE id = %d", orderId);
            orderStatement = con.prepareStatement(orderSQL);
            orderResult = orderStatement.executeQuery();

            if (orderResult.next()) {
                int userId = orderResult.getInt("userid");
                String status = orderResult.getString("status");
                String date = orderResult.getDate("date").toString();

                // 2. Recupero gli item associati all'ordine
                String itemsSQL = String.format("SELECT itemid FROM OrdersItem WHERE orderid = %d", orderId);
                itemsStatement = con.prepareStatement(itemsSQL);
                itemsResult = itemsStatement.executeQuery();

                while (itemsResult.next()) {
                    int itemId = itemsResult.getInt("itemid");
                    items.add(new Item(itemId)); // Supponendo che Item abbia un costruttore che accetta solo itemId
                }

                // 3. Creazione dell'oggetto Order con la lista di item
                order = new Order(orderId, userId, items, status, date);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (orderResult != null) orderResult.close();
            if (itemsResult != null) itemsResult.close();
            if (orderStatement != null) orderStatement.close();
            if (itemsStatement != null) itemsStatement.close();
        }

        return order; // Se l'ordine non esiste, restituisce null
    }
    */

}

