package test.DomainModel;

import main.java.DomainModel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {
    private PaymentMethod paymentMethod;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentMethod = new PaymentMethod("Mario", "Rossi", "1234567890123456", "12/25", "123");

        User user = new User(1, "Luca", "Bianchi", "lucab", 30, "M", "luca@email.com", "password");
        order = new Order();
        order.setOrderId(1001);
        order.setStatus("Received");
        order.setUser(user);

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(1, "Burger", "Cheeseburger", "Food", 5.99f, 0));
        order.setItems(items);
    }

    @Test
    void testPaySuccess() {
        String input = "y\n123";
        provideInput(input);

        assertDoesNotThrow(() -> paymentMethod.pay(50.0f));
    }

    @Test
    void testPayCancelledByUser() {
        String input = "n";
        provideInput(input);

        assertThrows(RuntimeException.class, () -> paymentMethod.pay(50.0f));
    }

    @Test
    void testPayWrongCVV() {
        String input = "y\n111\ny\n222\ny\n333\ny\n444\ny\n555";
        provideInput(input);

        assertThrows(RuntimeException.class, () -> paymentMethod.pay(50.0f));
    }

    @Test
    void testRefundSuccess() {
        assertDoesNotThrow(() -> paymentMethod.refund(order));
    }

    @Test
    void testRefundFailed() {
        order.setStatus("Shipped");
        assertThrows(RuntimeException.class, () -> paymentMethod.refund(order));
    }

    @Test
    void testPayWithWithheld() {
        paymentMethod.setWithheld(0.1f);
        String input = "y\n123";
        provideInput(input);

        assertDoesNotThrow(() -> paymentMethod.pay(100.0f));
    }

    private void provideInput(String data) {
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
    }
}