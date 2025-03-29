package main.java.BusinessLogic;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import main.java.ORM.PaymentMethodDAO;
import main.java.DomainModel.PaymentMethod;

import java.sql.SQLException;

public class UserProfileController {
    private User user;

    public UserProfileController(User user) {
        this.user = user;
    }

    public void updateUsername(String newUserName)throws SQLException  {
        UserDAO userDAO = new UserDAO();
        userDAO.updateUsername(user.getUsername(),newUserName);
        user.setUsername(newUserName);
    }

    public void updateEmail(String newEmail)throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateEmail(user.getUsername(),newEmail);
        user.setEmail(newEmail);
    }

    public void updatePassword(String newPassword)throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updatePassword(user.getUsername(),newPassword);
        user.setPassword(newPassword);
    }

    public void updateName(String newName)throws SQLException {
       UserDAO userDAO = new UserDAO();
       userDAO.updateName(user.getUsername(),newName);
       user.setName(newName);
    }

    public void updateSurname(String newSurname)throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateSurname(user.getUsername(),newSurname);
        user.setSurname(newSurname);
    }

    public void updateAge(int newAge)throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateAge(user.getUsername(),newAge);
        user.setAge(newAge);
    }

    public void updateGender(String newGender)throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.updateSex(user.getUsername(),newGender);
        user.setSex(newGender);
    }

    public void setPaymentMethod(String cardNumber, String cardExpiryDate, String cardCVV, String ownerName, String ownerSurname) throws SQLException {
        PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();
        UserDAO userDAO = new UserDAO();
        PaymentMethod paymentMethod = new PaymentMethod(ownerName, ownerSurname, cardNumber, cardExpiryDate, cardCVV);
        paymentMethodDAO.addPaymentMethod(paymentMethod);
        userDAO.updatePayment(user.getUsername(), cardNumber, cardExpiryDate, cardCVV, ownerName, ownerSurname, 0.01F);
        user.setPaymentMethod(paymentMethod);
    }

    public void changePaymentMethod(String oldCardNumber,String cardNumber, String cardExpiryDate, String cardCVV, String ownerName, String ownerSurname) throws SQLException {
        PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();
        UserDAO userDAO = new UserDAO();
        paymentMethodDAO.removePaymentMethod(oldCardNumber);
        user.setPaymentMethod(null);
        PaymentMethod paymentMethod = new PaymentMethod(ownerName, ownerSurname, cardNumber, cardExpiryDate, cardCVV);
        paymentMethodDAO.addPaymentMethod(paymentMethod);
        userDAO.updatePayment(user.getUsername(), cardNumber, cardExpiryDate, cardCVV, ownerName, ownerSurname, 0.01F);
        user.setPaymentMethod(paymentMethod);
    }

    public void removePaymentMethod(String cardNumber) throws SQLException {
        PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();
        UserDAO userDAO = new UserDAO();
        paymentMethodDAO.removePaymentMethod(cardNumber);
        user.setPaymentMethod(null);
    }

}
