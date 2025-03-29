package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.ORM.UserDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminUserController {

    //CONSTRUCTOR
    public AdminUserController() {}

    public ArrayList<User> viewUsers() throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.getAllUsers();
    }

    public ArrayList<User> searchUser(String username) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        ArrayList<User> users = new ArrayList<>();
        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("User not found");
            return null;
        }
        users.add(user);
        return users;
    }

    public void removeUser(String username) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("User not found");
            return;
        }
        userDAO.removeUser(username);
        System.out.println("User removed successfully");
    }
}
