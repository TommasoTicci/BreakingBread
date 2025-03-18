package main.java.BusinessLogic;

import main.java.DomainModel.Order;
import main.java.DomainModel.User;
import main.java.ORM.OrderDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminOrderController {

    //CONSTRUCTOR
    public AdminOrderController() {}

    public ArrayList<Order> viewOrders() throws SQLException,  ClassNotFoundException {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getAllOrders();
    }

    public ArrayList<Order> viewOrdersByStatus(boolean completed) throws SQLException,  ClassNotFoundException {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getAllOrdersByStatus(completed);
    }

    public ArrayList<Order> viewOrdersByUser(int userId) throws SQLException,  ClassNotFoundException {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getOrdersByUser(userId);
    }

    public void cancelOrder(int id, boolean toBeRefunded) throws SQLException, ClassNotFoundException {
        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.getOrder(id);
        if (order == null) {
            System.out.println("Order not found");
            return;
        }
        if (toBeRefunded) {
            User user = order.getUser();
            user.getPaymentMethod().refund(order);
        }
        orderDAO.removeOrderItem(id);
        orderDAO.removeOrder(id);
    }

    public void updateOrderStatus(int id, String status) throws SQLException, ClassNotFoundException {
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.updateStatus(id, status);
    }
}
