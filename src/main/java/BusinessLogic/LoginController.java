package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.ORM.UserDAO;

import java.sql.SQLException;

public class LoginController {

    //CONSTRUCTOR
    public LoginController() {}

    public User login(String username, String password) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.verifyPassword(username, password);
    }

    public User admin(String password) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return userDAO.verifyPassword("ADMIN", password);
    }

    public User register(String name, String surname, String username, String email, int age, String password, String sex) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        userDAO.addUser(name, surname, username, age, sex, email, password);
        return userDAO.verifyPassword(username, password);
    }

    public User register(String name, String surname, String username, String email, int age, String password, String sex, String cardNum, String cardExp, String cardCVV) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        userDAO.addUser(name, surname, username, age, sex, email, password, cardNum, cardExp, cardCVV, 0.01F);
        return userDAO.verifyPassword(username, password);
    }
}
