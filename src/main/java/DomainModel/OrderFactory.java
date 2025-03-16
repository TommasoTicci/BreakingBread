package main.java.DomainModel;

import java.util.ArrayList;

public class OrderFactory {
    public static Order createOrder(int orderId, User user, ArrayList<Item> items, String status, String date) {
        return new Order(orderId, user, items, status, date);
    }
}
