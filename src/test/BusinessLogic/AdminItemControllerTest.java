package test.BusinessLogic;
import main.java.BusinessLogic.AdminItemController;
import main.java.ORM.ItemDAO;
import main.java.DomainModel.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class AdminItemControllerTest {
    private AdminItemController adminItemController;
    private ItemDAO itemDAO;
    private int itemId1;
    private int itemId2;

    @BeforeEach
    void setUp() throws SQLException , ClassNotFoundException {
        adminItemController = new AdminItemController();
        itemDAO = new ItemDAO();
        itemId1=itemDAO.addItem("itemNameTest1","descriptionTest1","typeTest1",10.0f,0);
        itemId2=itemDAO.addItem("itemNameTest2","descriptionTest2","typeTest2",10.0f,10);

    }
    @AfterEach
    void tearDown() throws SQLException , ClassNotFoundException {
        itemDAO.removeItem(itemId1);
        itemDAO.removeItem(itemId2);
    }

    @Test
    void editItemTest() throws SQLException , ClassNotFoundException {
        adminItemController.editItem(itemId1,"1","newDescription");
        assertEquals(itemDAO.getItem(itemId1).getDescription(),"newDescription");
    }

    @Test
    void viewOffersTest() throws SQLException , ClassNotFoundException {
        ArrayList<Item> items =adminItemController.viewOffers();
        assertTrue(items.stream().anyMatch(item -> item.getItemId() == itemId2));
        assertFalse(items.stream().anyMatch(item -> item.getItemId() == itemId1));
    }
    @Test
    void removeItemFromMenuTest() throws SQLException , ClassNotFoundException {
        adminItemController.removeItemFromMenu(itemId1);
        Item item = itemDAO.getItem(itemId1);
        assertNull(item);

    }
}
