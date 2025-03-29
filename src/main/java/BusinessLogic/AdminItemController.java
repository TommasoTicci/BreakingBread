package main.java.BusinessLogic;

import main.java.DomainModel.Item;
import main.java.DomainModel.Order;
import main.java.ORM.ItemDAO;
import main.java.ORM.OrderDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminItemController {

    //CONSTRUCTOR
    public AdminItemController() {}

    public ArrayList<Item> viewMenu() throws SQLException, ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getAllItems();
    }

    public void addItemToMenu(String name, String description, String type, float price, int discount)  throws SQLException, ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAO();
        itemDAO.addItem(name, description, type, price, discount);
        System.out.println("Item added successfully");
    }

    public void  removeItemFromMenu(int id) throws SQLException, ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAO();
        OrderDAO orderDAO = new OrderDAO();
        Item item = itemDAO.getItem(id);
        ArrayList<Order> orders = orderDAO.getAllOrdersByStatus(false);
        boolean check = false;
        for (Order order : orders) {
            ArrayList<Item> items = order.getItems();
            for (Item item1 : items) {
                if (item1.getItemId() == id) {
                    check = true;
                    break;
                }
            }
        }
        if (item == null) {
            System.out.println("Item not found");
            return;
        }
        if (check) {
            System.out.println("Item cannot be removed, some users currently have it in their order");
            return;
        }
        itemDAO.removeItem(id);
    }

    public ArrayList<Item> viewOffers() throws SQLException, ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getAllDiscountedItems();
    }

    public void editItem(int id, String option, String edit) throws SQLException, ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAO();
        switch (option) {
            case "1": {
                try {
                    itemDAO.updateDescription(id, edit);
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;}
            case "2": {
                try {
                    itemDAO.updatePrice(id, Float.parseFloat(edit));
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;}
            case "3": {
                try {
                    itemDAO.updateDiscount(id, Integer.parseInt(edit));
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;}
            default: {System.out.println("Invalid option"); return;}
        }
        System.out.println("Item updated successfully");
    }
}
