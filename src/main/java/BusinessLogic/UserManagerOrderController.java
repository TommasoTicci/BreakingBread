package main.java.BusinessLogic;
import java.util.ArrayList;
import main.java.DomainModel.Item;
import main.java.DomainModel.Order;
import main.java.DomainModel.User;
import main.java.ORM.ItemDAO;
import main.java.ORM.OrderDAO;
import java.sql.SQLException;

public class UserManagerOrderController {
    private User user;

    public UserManagerOrderController(User user) {
        this.user = user;
    }

    public boolean addItem(int itemId, int quantity) throws SQLException, ClassNotFoundException {
        UserViewMenuController vmc = new UserViewMenuController();
        boolean found = false;
        ArrayList<Item> items = vmc.viewMenu();
        for (Item item : items) {
            if (item.getItemId() == itemId) {
                found = true;
                break;
            }
        }
        if (found) {
            for (int i = 0; i < quantity; i++) {
                user.getCart().add(itemId);
            }
        }
        return found;
    }

    public void removeItem(int itemId) {
        user.getCart().remove(Integer.valueOf(itemId));
    }

    public ArrayList<Item> viewCurrentOrder() throws SQLException ,ClassNotFoundException{
        ArrayList<Item> currentOrder = new ArrayList<>();
        ItemDAO itemDAO = new ItemDAO();

        if (user.getCart() != null) {
            for (Integer itemId : user.getCart()) {
                Item item = itemDAO.getItem(itemId);
                if (item != null) {
                    currentOrder.add(item);
                }
            }
        }
        return currentOrder;
    }

    public void confirmCurrentOrder()throws SQLException ,ClassNotFoundException{
        OrderDAO orderDAO = new OrderDAO();
        ItemDAO itemDAO = new ItemDAO();
        float total = 0;
        ArrayList<Integer> cart = user.getCart();
        for (Integer itemId : cart) {
            total += itemDAO.getItem(itemId).getPrice() * (1 - ((float) itemDAO.getItem(itemId).getDiscountPercentage() / 100));
        }
        if (user.getCart().isEmpty()) {
            System.err.println("No items in cart.");
            return;
        }
        if (user.getPaymentMethod() == null)
        {
            System.err.println("No payment method found. Please add a payment method from your profile page.");
        }
        else{
            try {
                user.getPaymentMethod().pay(total);
                int orderId = orderDAO.createOrder(user.getId());
                orderDAO.createOrderItemFromIds(orderId, user.getCart());
                user.getCart().clear();
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void cancelCurrentOrder(){
        if (user.getCart() != null) {
            user.getCart().clear();
        }
    }

    public ArrayList<Order> viewOrders() throws SQLException ,ClassNotFoundException{
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getOrdersByUser(user.getId());
    }

    public void cancelOrder(int orderId) throws SQLException ,ClassNotFoundException{
        OrderDAO orderDAO = new OrderDAO();
        if (orderDAO.getOrder(orderId) == null || orderDAO.getOrder(orderId).getUser().getId() != user.getId()) {
            System.err.println("Order not found.");
            return;
        }
        if (user.getPaymentMethod() == null){
            System.err.println("Refound not possible. No payment method found. Please add a payment method from your profile page.");
            return;
        }
        if(orderDAO.getOrder(orderId).getStatus().equals("Received")) {
            orderDAO.removeOrder(orderId);
        } else {
            System.err.println("Order cannot be canceled.");
        }
    }


}
