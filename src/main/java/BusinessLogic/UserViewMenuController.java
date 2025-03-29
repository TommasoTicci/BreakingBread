package main.java.BusinessLogic;
import main.java.DomainModel.Order;
import main.java.DomainModel.User;
import main.java.DomainModel.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import main.java.ORM.ItemDAO;
import main.java.ORM.OrderDAO;

public class UserViewMenuController {

    public UserViewMenuController() {}

    //TODO view by type

    public ArrayList<Item> viewMenu()throws SQLException,ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getAllItems();
    }

    public ArrayList<Item> viewOffers()throws SQLException,ClassNotFoundException{
        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getAllDiscountedItems();
    }

}