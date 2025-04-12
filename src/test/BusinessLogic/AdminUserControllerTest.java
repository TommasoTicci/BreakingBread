package test.BusinessLogic;
import main.java.BusinessLogic.UserManagerOrderController;
import main.java.DomainModel.Order;
import main.java.DomainModel.User;
import main.java.ORM.ItemDAO;
import main.java.ORM.OrderDAO;
import main.java.ORM.UserDAO;
import main.java.BusinessLogic.AdminUserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class AdminUserControllerTest {
    private AdminUserController adminUserController;
    private UserDAO userDAO;


    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        adminUserController = new AdminUserController();
        userDAO = new UserDAO();
        userDAO.addUser("nameTest", "surnameTest", "usernameTest", 25, "M", "test@email.com", "password");
    }


    void tearDown() throws SQLException, ClassNotFoundException  {
        userDAO.removeUser("usernameTest");
    }



    @Test
    void searchUserTest() throws SQLException, ClassNotFoundException {
         ArrayList<User> user = adminUserController.searchUser("usernameTest");
         assertNotNull(user.get(0));
         tearDown();
    }

    @Test
    void removeUserTest() throws SQLException, ClassNotFoundException {
        adminUserController.removeUser("usernameTest");
        assertNull(userDAO.getUser("usernameTest"));
    }



}
