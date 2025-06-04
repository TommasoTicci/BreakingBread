package test.ORM;
import main.java.DomainModel.Item;
import main.java.ORM.OrderDAO;
import main.java.ORM.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.DomainModel.Order;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OrderDAOTest {
    OrderDAO orderDAO;
    UserDAO userDAO;
    int orderId;
    ArrayList<Integer> items;



    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        items = new ArrayList<>();
        items.add(1);
        items.add(2);
        items.add(3);
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
        userDAO.addUser("nameTest", "surnameTest", "usernameTest", 25, "M", "test@email.com", "password");
        orderId = orderDAO.createOrder(userDAO.getUser("usernameTest").getId());
        orderDAO.createOrderItemFromIds(orderId,items);

    }

    @AfterEach
    public void tearDown() throws SQLException , ClassNotFoundException {
        userDAO.removeUser("usernameTest");
        orderDAO.removeOrder(orderId);
    }

    @Test
    public void getOrderTest() throws SQLException, ClassNotFoundException {
        Order order = orderDAO.getOrder(orderId);
        assertEquals(order.getUser().getUsername(), "usernameTest");
    }

    @Test
    public void removeItemsFromEveryOrderItemsTest () throws SQLException, ClassNotFoundException {
        orderDAO.removeItemsFromEveryOrderItems(1);
        Order order=orderDAO.getOrder(orderId);
        for (Item item :order.getItems()){
            assertNotEquals(item.getItemId(),1);
        }
    }





}
