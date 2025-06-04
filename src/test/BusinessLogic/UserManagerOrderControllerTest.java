package test.BusinessLogic;

import main.java.BusinessLogic.UserManagerOrderController;
import main.java.DomainModel.Item;
import main.java.DomainModel.User;
import main.java.DomainModel.Order;
import main.java.ORM.ItemDAO;
import main.java.ORM.UserDAO;
import main.java.ORM.OrderDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import java.util.ArrayList;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class  UserManagerOrderControllerTest {
    private UserManagerOrderController userManagerOrderController;
    private User user;
    private UserDAO userDAO;
    private ItemDAO itemDAO;
    private OrderDAO orderDAO;
    private int ItemId1;
    private int ItemId2;
    private int ItemId3;


   @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        userDAO = new UserDAO();
        userDAO.addUser("nameTest", "surnameTest", "usernameTest", 25, "M", "test@email.com", "password","1234567812345678","05/26", "123",0.01F,"nameTest","surnameTest");
        user = userDAO.getUser("usernameTest");
        userManagerOrderController = new UserManagerOrderController(user);

        itemDAO = new ItemDAO();
        ItemId1 =itemDAO.addItem("itemTest1","descriptionTest","typeTest",10.0f,0);
        ItemId2 =itemDAO.addItem("itemTest2","descriptionTest","typeTest",10.0f,0);
        ItemId3 =itemDAO.addItem("itemTest3","descriptionTest","typeTest",10.0f,0);

        orderDAO = new OrderDAO();
    }

   @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException  {
        userDAO.removeUser("usernameTest");
        itemDAO.removeItem(ItemId1);
        itemDAO.removeItem(ItemId2);
        itemDAO.removeItem(ItemId3);

    }


    @Test
    void addItemTest()throws SQLException, ClassNotFoundException {

        boolean found=userManagerOrderController.addItem(ItemId1,3);
        assertTrue(found);

        assertEquals(user.getCart().get(0),ItemId1);
        assertEquals(user.getCart().get(1),ItemId1);
        assertEquals(user.getCart().get(2),ItemId1);

    }

    @Test
    void viewCurrentOrderTest()throws SQLException, ClassNotFoundException {
        userManagerOrderController.addItem(ItemId1,2);
        userManagerOrderController.addItem(ItemId3,1);
        ArrayList<Item> currentOrder = userManagerOrderController.viewCurrentOrder();

        assertEquals(currentOrder.get(0).getItemId(),ItemId1);
        assertEquals(currentOrder.get(1).getItemId(),ItemId1);
        assertEquals(currentOrder.get(2).getItemId(),ItemId3);
    }

    @Test
    void confirmCurrentOrderTest()throws SQLException, ClassNotFoundException {
        orderDAO = new OrderDAO();

        String input = "y\n123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        userManagerOrderController.addItem(ItemId1, 2);
        userManagerOrderController.addItem(ItemId3, 1);
        userManagerOrderController.confirmCurrentOrder();

        ArrayList<Order> order = orderDAO.getOrdersByUser(user.getId());

        ArrayList<Item> items=order.get(0).getItems();

        assertEquals(items.get(0).getItemId(),ItemId1);
        assertEquals(items.get(1).getItemId(),ItemId1);
        assertEquals(items.get(2).getItemId(),ItemId3);

        assertEquals(user.getCart().size(), 0);
        
    }

    @Test
    void cancelOrderTest()throws SQLException, ClassNotFoundException {

        String input = "y\n123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        userManagerOrderController.addItem(ItemId1, 2);
        userManagerOrderController.addItem(ItemId3, 1);
        userManagerOrderController.confirmCurrentOrder();

        ArrayList<Order> order = orderDAO.getOrdersByUser(user.getId());

        userManagerOrderController.cancelOrder(order.get(0).getOrderId());

        assertNull(orderDAO.getOrder(order.get(0).getOrderId()));

    }
}
