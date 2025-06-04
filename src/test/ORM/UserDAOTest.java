package test.ORM;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        userDAO = new UserDAO();
        userDAO.addUser("nameTest", "surnameTest", "usernameTest", 25, "M", "test@email.com", "password","1234567812345678","05/26", "123",0.01F,"nameTest","surnameTest");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        userDAO.removeUser("usernameTest");
    }


    @Test
    public void AddUserTest() throws SQLException {
        User user = userDAO.verifyPassword("usernameTest", "password");
        assertEquals(user.getName(), "nameTest");
        assertEquals(user.getSurname(), "surnameTest");
    }

    @Test
    public void updateNameTest() throws SQLException {
        userDAO.updateName("usernameTest","newNameTest");
        assertEquals("newNameTest", userDAO.getUser("usernameTest").getName());


    }


}
