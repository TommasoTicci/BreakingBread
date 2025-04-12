package test.BusinessLogic;

import main.java.BusinessLogic.LoginController;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private LoginController loginController;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() throws SQLException {
        loginController = new LoginController();
        userDAO = new UserDAO();
        userDAO.addUser("nameTest","surnameTest","usernameTest",33,"m","test@gmail.com","password");
    }
    @AfterEach
    void tearDown() throws SQLException {
        userDAO.removeUser("usernameTest");
    }


    @Test
    void loginTest() throws SQLException ,ClassNotFoundException  {

        User RealUser= loginController.login("usernameTest","password");
        assertNotNull(RealUser);

        User UserWithWrongPassword= loginController.login("usernameTest","wrongPassword");
        assertNull(UserWithWrongPassword);

        User FakeUser= loginController.login("fakeUser","FakePassword");
        assertNull(FakeUser);
    }

    @Test
    void RegisterTest() throws SQLException ,ClassNotFoundException  {

        User User= loginController.register("pino","gini","pinos","pino@gmail.com",33,"password","m");
        assertNotNull(User);
        assertEquals("pinos",User.getUsername());
        userDAO.removeUser("pinos");
    }




}