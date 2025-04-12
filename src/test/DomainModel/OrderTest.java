package test.DomainModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.DomainModel.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class OrderTest {
    private Order order;
    private User user;
    private ArrayList<Item> items;

    @BeforeEach
    void setUp() {
        user = new User(1, "Marco", "Rossi", "marcor", 25, "M", "marco@email.com", "5678");

        items = new ArrayList<>();
        items.add(new Item(1, "Cheeseburger", "Classic cheeseburger with fries", "Burger", 5.99f, 0));
        items.add(new Item(2, "Chicken Nuggets", "6-piece chicken nuggets", "Chicken", 3.99f, 10));
        items.add(new Item(3, "Soda", "Large cola", "Drink", 2.49f, 0));
        items.add(new Item(4, "Ice Cream", "Vanilla ice cream cone", "Dessert", 1.99f, 5));

        order = new Order();
        order.setOrderId(1001);
        order.setStatus("Preparing");
        order.setDate("2023-06-20");
        order.setUser(user);
        order.setItems(items);
    }

    @Test
    void testGetOrderId() {
        assertEquals(1001, order.getOrderId());
    }

    @Test
    void testGetStatus() {
        assertEquals("Preparing", order.getStatus());
    }

    @Test
    void testGetDate() {
        assertEquals("2023-06-20", order.getDate());
    }

    @Test
    void testGetUser() {
        assertNotNull(order.getUser());
        assertEquals("Marco", order.getUser().getName());
        assertEquals("Rossi", order.getUser().getSurname());
    }

    @Test
    void testGetItems() {
        assertEquals(4, order.getItems().size());
        assertEquals("Cheeseburger", order.getItems().get(0).getName());
        assertEquals("Ice Cream", order.getItems().get(3).getName());
    }

    @Test
    void testSetItems() {
        ArrayList<Item> newItems = new ArrayList<>();
        newItems.add(new Item(5, "Fish Burger", "Fish fillet with tartar sauce", "Burger", 6.49f, 0));
        order.setItems(newItems);

        assertEquals(1, order.getItems().size());
        assertEquals("Fish Burger", order.getItems().get(0).getName());
    }

    @Test
    void testGetTotal() {
        // Calculate expected total manually:
        // Cheeseburger: 5.99 - 0% = 5.99
        // Chicken Nuggets: 3.99 - 10% = 3.591
        // Soda: 2.49 - 0% = 2.49
        // Ice Cream: 1.99 - 5% = 1.8905
        // Total = 5.99 + 3.591 + 2.49 + 1.8905 ≈ 13.9615
        float expectedTotal = 5.99f + 3.591f + 2.49f + 1.8905f;
        float actualTotal = order.getTotal();

        assertEquals(expectedTotal, actualTotal, 0.001);
    }

    @Test
    void testGetTotalWithEmptyItems() {
        order.setItems(new ArrayList<>());
        assertEquals(0.0f, order.getTotal());
    }

    @Test
    void testGetTotalWithSingleItem() {
        ArrayList<Item> singleItem = new ArrayList<>();
        singleItem.add(new Item(6, "Family Meal", "2 burgers, nuggets and fries", "Combo", 15.99f, 15));
        order.setItems(singleItem);

        float expected = 15.99f * 0.85f; // 15% discount
        assertEquals(expected, order.getTotal(), 0.001);
    }

    @Test
    void testGetTotalWithMultipleSameItems() {
        ArrayList<Item> multipleItems = new ArrayList<>();
        multipleItems.add(new Item(7, "Fries", "Medium french fries", "Side", 2.49f, 0));
        multipleItems.add(new Item(7, "Fries", "Medium french fries", "Side", 2.49f, 0));
        multipleItems.add(new Item(7, "Fries", "Medium french fries", "Side", 2.49f, 0));
        order.setItems(multipleItems);

        float expected = 2.49f * 3;
        assertEquals(expected, order.getTotal(), 0.001);
    }
}