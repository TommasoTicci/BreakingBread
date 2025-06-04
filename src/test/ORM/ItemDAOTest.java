package test.ORM;
import main.java.DomainModel.Item;
import main.java.ORM.ItemDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ItemDAOTest {
    int itemId;
    private ItemDAO itemdao;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        itemdao = new ItemDAO();
        itemId=itemdao.addItem("itemNameTest","descriptionTest","type",10.0f,12);

    }

    @AfterEach
    public void tearDown()throws SQLException, ClassNotFoundException{
        itemdao.removeItem(itemId);
    }

    @Test
    void addItemTest() throws SQLException, ClassNotFoundException {
        Item item =itemdao.getItem(itemId);
        assertEquals(item.getName(),"itemNameTest");
        assertEquals(item.getDescription(),"descriptionTest");
        assertEquals(item.getType(),"type");
    }

    @Test
    void getAllItemsTest() throws SQLException, ClassNotFoundException {
        ArrayList<Item> items = itemdao.getAllItems();
        for (Item item : items) {
            assertNotEquals(item.getPrice(),0);
        }
    }

    @Test
    void updateDescription() throws SQLException, ClassNotFoundException {
        itemdao.updateDescription(itemId,"newDescription");
        assertEquals(itemdao.getItem(itemId).getDescription(),"newDescription");
    }




}
