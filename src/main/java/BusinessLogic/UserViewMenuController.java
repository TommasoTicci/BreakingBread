package main.java.BusinessLogic;
import main.java.DomainModel.Order;
import main.java.DomainModel.User;
import main.java.DomainModel.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import main.java.ORM.ItemDAO;
import main.java.ORM.OrderDAO;

public class UserViewMenuController {
    private User user;

    public UserViewMenuController(User user) {
        this.user = user;
    }

    public ArrayList<Item> viewMenu()throws SQLException,ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getAllItems();
    }

    public ArrayList<Order> viewOrders()throws SQLException,ClassNotFoundException{
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getOrdersByUser(this.user.getId());
    }

}

