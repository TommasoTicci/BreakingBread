package test.BusinessLogic;
import  main.java.BusinessLogic.AdminOrderController;
import main.java.ORM.OrderDAO;
import main.java.ORM.UserDAO;
import main.java.ORM.ItemDAO;
import main.java.DomainModel.User;
import main.java.DomainModel.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AdminOrderControllerTest {
    private AdminOrderController adminOrderController;
    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private ItemDAO itemDAO;
    private User user1;
    private User user2;
    private int orderId1;
    private int orderId2;
    private int ItemId1;
    private int ItemId2;



    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException{
        adminOrderController = new AdminOrderController();

        userDAO = new UserDAO();

        userDAO.addUser("nameTest", "surnameTest", "usernameTest", 25, "M", "test@email.com", "password");
        user1 = userDAO.getUser("usernameTest");

        userDAO.addUser("nameTest_", "surnameTest_", "usernameTest_", 25, "M", "test@email.com_", "password");
        user2 = userDAO.getUser("usernameTest_");

        itemDAO = new ItemDAO();
        ItemId1 =itemDAO.addItem("itemTest1","descriptionTest","typeTest",10.0f,0);
        ItemId2 =itemDAO.addItem("itemTest2","descriptionTest","typeTest",10.0f,0);

        ArrayList<Integer> cart = new ArrayList<Integer>();
        cart.add(ItemId1);
        cart.add(ItemId2);

        orderDAO = new OrderDAO();

        user1.setCart(cart);
        orderId1 =orderDAO.createOrder(user1.getId());
        orderDAO.createOrderItemFromIds(orderId1, cart);
        orderDAO.updateStatus(orderId1,"Completed");

        orderId2 =orderDAO.createOrder(user2.getId());
        orderDAO.createOrderItemFromIds(orderId2, cart);

    }

    @AfterEach
    void tearDown() throws SQLException, ClassNotFoundException{
        userDAO.removeUser("usernameTest");
        userDAO.removeUser("usernameTest_");
        itemDAO.removeItem(ItemId1);
        itemDAO.removeItem(ItemId2);
        orderDAO.removeOrder(orderId1);
    }


    @Test
    void CancelOrderTest() throws SQLException, ClassNotFoundException {
        adminOrderController.cancelOrder(orderId1,false);
        assertNull(orderDAO.getOrder(orderId1));
    }

   @Test
    void deleteCompletedOrdersTest() throws SQLException, ClassNotFoundException {
        adminOrderController.deleteCompletedOrders();
        assertNull(orderDAO.getOrder(orderId1));
        assertNotNull(orderDAO.getOrder(orderId2));
   }

   @Test
    void viewOrdersByUserTest() throws SQLException, ClassNotFoundException {
        ArrayList<Order> order =adminOrderController.viewOrdersByUser(user1.getId());
        assertNotNull(order);
        assertEquals(order.get(0).getUser().getUsername(),"usernameTest");

   }





}
