package main.java.DomainModel;

import java.util.ArrayList;

public class Order {
    private int orderId;
    private String status;
    private String date;
    User user;
    ArrayList<Item>  items;

    //CONSTRUCTORS
    public Order(int orderId, User user, ArrayList<Item> items, String status, String date) {}

    //GETTERS
    public int getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
    public User getUser() { return user; }
    public ArrayList<Item> getItems() { return items; }

    //SETTERS
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setStatus(String status) { this.status = status; }
    public void setDate(String date) { this.date = date; }
    public void setUser(User user) { this.user = user; }
    public void setItems(ArrayList<Item> items) { this.items = items; }

    //METHOD
    public float getTotal() {
        float total = 0;
        for (Item item : items) {
            total += item.getPrice();
        }
        return total;
    }
}
