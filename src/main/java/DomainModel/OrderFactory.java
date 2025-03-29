package main.java.DomainModel;

import java.util.ArrayList;

public class OrderFactory {
    public static Order createOrder(int orderId, User user, ArrayList<Item> items, String status, String date) {
        if (user == null || items == null || status == null || date == null) {
            return null;
        }
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUser(user);
        order.setItems(items);
        order.setStatus(status);
        order.setDate(date);
        return order;
    }
}
