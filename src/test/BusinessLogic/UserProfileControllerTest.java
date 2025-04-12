package test.BusinessLogic;

import main.java.BusinessLogic.LoginController;
import main.java.BusinessLogic.UserManagerOrderController;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.Item;
import main.java.DomainModel.PaymentMethod;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import main.java.ORM.PaymentMethodDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;


public class UserProfileControllerTest {
    private UserProfileController userProfileController;
    private User user;
    private UserDAO userDAO;
    private PaymentMethodDAO paymentMethodDAO;



    @BeforeEach
    void setUp()throws SQLException {
        userDAO = new UserDAO();
        userDAO.addUser("nameTest", "surnameTest", "usernameTest", 25, "M", "test@email.com", "password","1111111111111111","05/26", "123",0.01F,"nameTest","surnameTest");
        user = userDAO.getUser("usernameTest");
        userProfileController =new UserProfileController(user);

        paymentMethodDAO = new PaymentMethodDAO();
    }

    @AfterEach
    void tearDown() throws SQLException {
        userDAO.removeUser("usernameTest");
    }

    @Test
    void foo()throws SQLException{


    }

    @Test
    void updateUsernameTest() throws SQLException {
        userProfileController.updateName("newNameTest");
        assertEquals("newNameTest", user.getName());
    }

    @Test
    void changePasswordTest() throws SQLException {
        userProfileController.changePaymentMethod("1111111111111111","2222222222222222","25/02","666","nameTest","surnameTest");
        assertEquals("2222222222222222",user.getPaymentMethod().getCardNumber());
        PaymentMethod paymentMethod=  paymentMethodDAO.getPaymentMethod("2222222222222222");
        assertNotNull(paymentMethod);
        assertEquals(paymentMethod.getOwnerName(),"nameTest");

    }

    @Test
    void removePaymentMethodTest() throws SQLException {
        userProfileController.removePaymentMethod("1111111111111111");
        assertNull(user.getPaymentMethod());
        assertNull(paymentMethodDAO.getPaymentMethod("1111111111111111"));
    }

    @Test
    void setPaymentMethod() throws SQLException {
        userProfileController.removePaymentMethod("1111111111111111");
        userProfileController.setPaymentMethod("3333333333333333","25/02","666","nameTest","surnameTest");
        assertEquals("3333333333333333",user.getPaymentMethod().getCardNumber());
        assertEquals(paymentMethodDAO.getPaymentMethod("3333333333333333").getOwnerName(),user.getPaymentMethod().getOwnerName());
        assertEquals(paymentMethodDAO.getPaymentMethod("3333333333333333").getOwnerSurname(),user.getPaymentMethod().getOwnerSurname());


    }


}
