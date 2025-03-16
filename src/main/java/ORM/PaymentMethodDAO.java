package main.java.ORM;
import main.java.DomainModel.PaymentMethod;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class PaymentMethodDAO {

    private Connection connection;

    public PaymentMethodDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        String sql = "INSERT INTO PaymentMethod (cardNumber, expirationDate, cvv, ownerName, ownerSurname, withheld) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, paymentMethod.getCardNumber());
            pstmt.setString(2, paymentMethod.getCardExpiryDate());
            pstmt.setString(3, paymentMethod.getCardCVV());
            pstmt.setString(4, paymentMethod.getOwnerName());
            pstmt.setString(5, paymentMethod.getOwnerSurname());
            pstmt.setFloat(6, paymentMethod.getWithheld());

            pstmt.executeUpdate();
            System.out.println("Payment method added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding payment method: " + e.getMessage());
        }
    }

    public void removePaymentMethod(String cardNumber) {
        String sql = "DELETE FROM PaymentMethod WHERE cardNumber = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Payment method with card number " + cardNumber + " removed successfully.");
            } else {
                System.out.println("No payment method found with card number " + cardNumber + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error removing payment method: " + e.getMessage());
        }
    }

    public PaymentMethod getPaymentMethod(String cardNumber) {
        String sql = "SELECT * FROM PaymentMethod WHERE cardNumber = ?";
        PaymentMethod paymentMethod = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Recupera i dati dal ResultSet e crea un oggetto PaymentMethod
                    String ownerName = rs.getString("ownerName");
                    String ownerSurname = rs.getString("ownerSurname");
                    String expirationDate = rs.getString("expirationDate");
                    String cvv = rs.getString("cvv");
                    float withheld = rs.getFloat("withheld");

                    paymentMethod = new PaymentMethod(ownerName, ownerSurname, cardNumber, expirationDate, cvv);
                    paymentMethod.setWithheld(withheld);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving payment method: " + e.getMessage());
        }

        return paymentMethod;
    }

    public ArrayList<PaymentMethod> getAllPaymentMethods() {
        String sql = "SELECT * FROM PaymentMethod";
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>(); // Usa direttamente ArrayList

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Recupera i dati dal ResultSet e crea un oggetto PaymentMethod
                String cardNumber = rs.getString("cardNumber");
                String ownerName = rs.getString("ownerName");
                String ownerSurname = rs.getString("ownerSurname");
                String expirationDate = rs.getString("expirationDate");
                String cvv = rs.getString("cvv");
                float withheld = rs.getFloat("withheld");

                PaymentMethod paymentMethod = new PaymentMethod(ownerName, ownerSurname, cardNumber, expirationDate, cvv);
                paymentMethod.setWithheld(withheld);

                // Aggiungi l'oggetto PaymentMethod alla lista
                paymentMethods.add(paymentMethod);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving payment methods: " + e.getMessage());
        }

        return paymentMethods; // Restituisce direttamente un ArrayList
    }



}
