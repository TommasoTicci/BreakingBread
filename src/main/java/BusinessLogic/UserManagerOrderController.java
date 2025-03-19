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

    //fixme in caso si decida di mettere un attributo qty a item
    public void addItem(int itemId) {
        user.getCart().add(itemId);
    }
    //fixme uguale a sopra
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
        int orderId=orderDAO.createOrder(user.getId());
        orderDAO.createOrderItemFromIds(orderId,user.getCart());
        user.getCart().clear();
    }

    public void cancelCurrentOrder(){
        user.getCart().clear();
    }

    public ArrayList<Order> viewOrders() throws SQLException ,ClassNotFoundException{
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getOrdersByUser(user.getId());
    }

    public void cancelOrder(int orderId) throws SQLException ,ClassNotFoundException{
        OrderDAO orderDAO = new OrderDAO();
        if(orderDAO.getOrder(orderId).getStatus().equals("Recived")) {
            orderDAO.removeOrder(orderId);
        }
    }


}
