package main.java.ORM;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import main.java.DomainModel.User;

public class UserDAO {

    private Connection connection;

    public UserDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void addUser(String name,String surname, String username, int age, String sex, String email, String password) throws SQLException {
        String sql = "INSERT INTO Users (name, surname, username, age, sex, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, username);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, sex);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, password);

            preparedStatement.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    public void addUser(String name, String surname, String username, int age, String sex, String email, String password, String paymentMethod, String cardExpirationDate, String cardCVV, float withheld) throws SQLException {

        connection.setAutoCommit(false);

        try {
            String paymentMethodSql = "INSERT INTO PaymentMethod (cardNumber, expirationDate, cvv, ownerName, ownerSurname, withheld) VALUES (?, ?, ?, ?, ?, ?) "
                    + "ON CONFLICT (cardNumber) DO NOTHING"; // Se il paymentMethod esiste già, non fare nulla

            try (PreparedStatement paymentStatement = connection.prepareStatement(paymentMethodSql)) {
                paymentStatement.setString(1, paymentMethod);
                paymentStatement.setDate(2, Date.valueOf(cardExpirationDate));
                paymentStatement.setString(3, cardCVV);
                paymentStatement.setString(4, name);
                paymentStatement.setString(5, surname);
                paymentStatement.setFloat(6, withheld);

                paymentStatement.executeUpdate();
            }

            String userSql = "INSERT INTO Users (name, surname, username, age, sex, email, password, paymentMethod) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement userStatement = connection.prepareStatement(userSql)) {
                userStatement.setString(1, name);
                userStatement.setString(2, surname);
                userStatement.setString(3, username);
                userStatement.setInt(4, age);
                userStatement.setString(5, sex);
                userStatement.setString(6, email);
                userStatement.setString(7, password);
                userStatement.setString(8, paymentMethod); // Associa il paymentMethod all'utente

                userStatement.executeUpdate();
            }

            connection.commit();
            System.out.println("User and PaymentMethod added successfully.");
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Error: " + e.getMessage());
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void removeUser(String username) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Ottieni il paymentMethod associato all'utente
            String getPaymentMethodSql = "SELECT paymentMethod FROM Users WHERE username = ?";
            String paymentMethod = null;

            try (PreparedStatement getPaymentMethodStatement = connection.prepareStatement(getPaymentMethodSql)) {
                getPaymentMethodStatement.setString(1, username);
                try (ResultSet resultSet = getPaymentMethodStatement.executeQuery()) {
                    if (resultSet.next()) {
                        paymentMethod = resultSet.getString("paymentMethod");
                    }
                }
            }

            // 2. Rimuovi l'utente dalla tabella Users
            String removeUserSql = "DELETE FROM Users WHERE username = ?";
            try (PreparedStatement removeUserStatement = connection.prepareStatement(removeUserSql)) {
                removeUserStatement.setString(1, username);
                int rowsAffected = removeUserStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User removed successfully.");
                } else {
                    System.out.println("No user found with username: " + username);
                    return; // Esci dal metodo se l'utente non esiste
                }
            }

            // 3. Se l'utente aveva un paymentMethod associato, verifica se è ancora utilizzato da altri utenti
            if (paymentMethod != null) {
                String checkPaymentMethodUsageSql = "SELECT COUNT(*) AS userCount FROM Users WHERE paymentMethod = ?";
                try (PreparedStatement checkPaymentMethodUsageStatement = connection.prepareStatement(checkPaymentMethodUsageSql)) {
                    checkPaymentMethodUsageStatement.setString(1, paymentMethod);
                    try (ResultSet resultSet = checkPaymentMethodUsageStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt("userCount") == 0) {
                            // Se il paymentMethod non è più utilizzato, rimuovilo dalla tabella PaymentMethod
                            String removePaymentMethodSql = "DELETE FROM PaymentMethod WHERE cardNumber = ?";
                            try (PreparedStatement removePaymentMethodStatement = connection.prepareStatement(removePaymentMethodSql)) {
                                removePaymentMethodStatement.setString(1, paymentMethod);
                                removePaymentMethodStatement.executeUpdate();
                                System.out.println("Unused PaymentMethod removed successfully.");
                            }
                        }
                    }
                }
            }

            // Conferma la transazione
            connection.commit();
        } catch (SQLException e) {
            // Annulla la transazione in caso di errore
            connection.rollback();
            System.err.println("Error: " + e.getMessage());
            throw e;
        } finally {
            // Ripristina l'autocommit
            connection.setAutoCommit(true);
        }
    }

    public void removePaymentMethod(String username) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Ottieni il paymentMethod associato all'utente
            String getPaymentMethodSql = "SELECT paymentMethod FROM Users WHERE username = ?";
            String paymentMethod = null;

            try (PreparedStatement getPaymentMethodStatement = connection.prepareStatement(getPaymentMethodSql)) {
                getPaymentMethodStatement.setString(1, username);
                try (ResultSet resultSet = getPaymentMethodStatement.executeQuery()) {
                    if (resultSet.next()) {
                        paymentMethod = resultSet.getString("paymentMethod");
                    }
                }
            }

            // Se l'utente non ha un metodo di pagamento, esci dal metodo
            if (paymentMethod == null) {
                System.out.println("No payment method found for user: " + username);
                return;
            }

            // 2. Verifica se il paymentMethod è ancora utilizzato da altri utenti
            String checkPaymentMethodUsageSql = "SELECT COUNT(*) AS userCount FROM Users WHERE paymentMethod = ?";
            try (PreparedStatement checkPaymentMethodUsageStatement = connection.prepareStatement(checkPaymentMethodUsageSql)) {
                checkPaymentMethodUsageStatement.setString(1, paymentMethod);
                try (ResultSet resultSet = checkPaymentMethodUsageStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt("userCount") == 1) {
                        // Se è utilizzato solo da questo utente, rimuovilo dalla tabella PaymentMethod
                        String removePaymentMethodSql = "DELETE FROM PaymentMethod WHERE cardNumber = ?";
                        try (PreparedStatement removePaymentMethodStatement = connection.prepareStatement(removePaymentMethodSql)) {
                            removePaymentMethodStatement.setString(1, paymentMethod);
                            removePaymentMethodStatement.executeUpdate();
                            System.out.println("Payment method removed successfully.");
                        }
                    } else {
                        System.out.println("Payment method is still used by other users. Not removed.");
                    }
                }
            }

            // 3. Rimuovi il riferimento al metodo di pagamento dall'utente
            String removeUserPaymentMethodSql = "UPDATE Users SET paymentMethod = NULL WHERE username = ?";
            try (PreparedStatement removeUserPaymentMethodStatement = connection.prepareStatement(removeUserPaymentMethodSql)) {
                removeUserPaymentMethodStatement.setString(1, username);
                removeUserPaymentMethodStatement.executeUpdate();
                System.out.println("User's payment method reference removed.");
            }

            // Conferma la transazione
            connection.commit();
        } catch (SQLException e) {
            // Annulla la transazione in caso di errore
            connection.rollback();
            System.err.println("Error: " + e.getMessage());
            throw e;
        } finally {
            // Ripristina l'autocommit
            connection.setAutoCommit(true);
        }
    }

    //todo removeOrder()

    public User verifyPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String userUsername = resultSet.getString("username");
                    int age = resultSet.getInt("age");
                    String sex = resultSet.getString("sex");
                    String email = resultSet.getString("email");
                    String userPassword = resultSet.getString("password");
                    String paymentMethod = resultSet.getString("paymentMethod");

                    if (paymentMethod == null) {
                        return new User(id, name, surname, userUsername, age, sex, email, userPassword);
                    } else {

                        String paymentSql = "SELECT * FROM PaymentMethod WHERE cardNumber = ?";
                        try (PreparedStatement paymentStatement = connection.prepareStatement(paymentSql)) {
                            paymentStatement.setString(1, paymentMethod);
                            try (ResultSet paymentResultSet = paymentStatement.executeQuery()) {
                                if (paymentResultSet.next()) {
                                    String cardNumber = paymentResultSet.getString("cardNumber");
                                    String cardExpiryDate = paymentResultSet.getString("expirationDate");
                                    String cardCVV = paymentResultSet.getString("cvv");

                                    return new User(id, name, surname, userUsername, age, email, userPassword, cardNumber, cardExpiryDate, cardCVV);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        // Query per recuperare tutti gli utenti
        String sql = "SELECT * FROM Users";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Itera attraverso tutti i risultati della query
            while (resultSet.next()) {
                // Estrai i dettagli dell'utente
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String username = resultSet.getString("username");
                int age = resultSet.getInt("age");
                String sex = resultSet.getString("sex");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String paymentMethod = resultSet.getString("paymentMethod");

                // Se il paymentMethod è nullo, usa il primo costruttore
                if (paymentMethod == null) {
                    users.add(new User(id, name, surname, username, age, sex, email, password));
                } else {
                    // Caso in cui il paymentMethod non è nullo, recupera i dettagli dal PaymentMethod
                    String paymentSql = "SELECT * FROM PaymentMethod WHERE cardNumber = ?";
                    try (PreparedStatement paymentStatement = connection.prepareStatement(paymentSql)) {
                        paymentStatement.setString(1, paymentMethod);
                        try (ResultSet paymentResultSet = paymentStatement.executeQuery()) {
                            if (paymentResultSet.next()) {
                                String cardNumber = paymentResultSet.getString("cardNumber");
                                String cardExpiryDate = paymentResultSet.getString("expirationDate");
                                String cardCVV = paymentResultSet.getString("cvv");

                                // Crea l'oggetto User con il secondo costruttore che include PaymentMethod
                                users.add(new User(id, name, surname, username, age, email, password, cardNumber, cardExpiryDate, cardCVV));
                            }
                        }
                    }
                }
            }
        }

        return users;
    }

    public User getUser(int id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Estrai i dettagli dell'utente
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String userUsername = resultSet.getString("username");
                    int age = resultSet.getInt("age");
                    String sex = resultSet.getString("sex");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String paymentMethod = resultSet.getString("paymentMethod");

                    // Se il paymentMethod è nullo, usa il primo costruttore
                    if (paymentMethod == null) {
                        return new User(id, name, surname, userUsername, age, sex, email, password);
                    } else {
                        // Caso in cui il paymentMethod non è nullo, recupera i dettagli dal PaymentMethod
                        String paymentSql = "SELECT * FROM PaymentMethod WHERE cardNumber = ?";
                        try (PreparedStatement paymentStatement = connection.prepareStatement(paymentSql)) {
                            paymentStatement.setString(1, paymentMethod);
                            try (ResultSet paymentResultSet = paymentStatement.executeQuery()) {
                                if (paymentResultSet.next()) {
                                    String cardNumber = paymentResultSet.getString("cardNumber");
                                    String cardExpiryDate = paymentResultSet.getString("expirationDate");
                                    String cardCVV = paymentResultSet.getString("cvv");

                                    // Crea l'oggetto User con il secondo costruttore che include PaymentMethod
                                    return new User(id, name, surname, userUsername, age, email, password, cardNumber, cardExpiryDate, cardCVV);
                                }
                            }
                        }
                    }
                }
            }
        }
        // Se l'utente non viene trovato, restituisci null
        return null;
    }

    public User getUser(String username) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Estrai i dettagli dell'utente
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String userUsername = resultSet.getString("username");
                    int age = resultSet.getInt("age");
                    String sex = resultSet.getString("sex");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String paymentMethod = resultSet.getString("paymentMethod");

                    // Se il paymentMethod è nullo, usa il primo costruttore
                    if (paymentMethod == null) {
                        return new User(id, name, surname, userUsername, age, sex, email, password);
                    } else {
                        // Caso in cui il paymentMethod non è nullo, recupera i dettagli dal PaymentMethod
                        String paymentSql = "SELECT * FROM PaymentMethod WHERE cardNumber = ?";
                        try (PreparedStatement paymentStatement = connection.prepareStatement(paymentSql)) {
                            paymentStatement.setString(1, paymentMethod);
                            try (ResultSet paymentResultSet = paymentStatement.executeQuery()) {
                                if (paymentResultSet.next()) {
                                    String cardNumber = paymentResultSet.getString("cardNumber");
                                    String cardExpiryDate = paymentResultSet.getString("expirationDate");
                                    String cardCVV = paymentResultSet.getString("cvv");

                                    // Crea l'oggetto User con il secondo costruttore che include PaymentMethod
                                    return new User(id, name, surname, userUsername, age, email, password, cardNumber, cardExpiryDate, cardCVV);
                                }
                            }
                        }
                    }
                }
            }
        }
        // Se l'utente non viene trovato, restituisci null
        return null;
    }

    public void updateName(String username, String newName) throws SQLException {
        String sql = "UPDATE Users SET name = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newName);  // nuovo nome
            preparedStatement.setString(2, username); // username dell'utente da aggiornare

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Name updated successfully for username: " + username);
            } else {
                System.out.println("No user found with the username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating name: " + e.getMessage());
            throw e;
        }
    }

    public void updateSurname(String username, String newSurname) throws SQLException {
        String sql = "UPDATE Users SET surname = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newSurname);  // nuovo nome
            preparedStatement.setString(2, username); // username dell'utente da aggiornare

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Surname updated successfully for username: " + username);
            } else {
                System.out.println("No user found with the username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating surname: " + e.getMessage());
            throw e;
        }
    }

    public void updateUsername(String username, String newUsername) throws SQLException {
        String sql = "UPDATE Users SET username = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newUsername);  // nuovo nome
            preparedStatement.setString(2, username); // username dell'utente da aggiornare

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Username updated successfully for username: " + username);
            } else {
                System.out.println("No user found with the username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating username: " + e.getMessage());
            throw e;
        }
    }

    public void updateAge(String username, int newAge) throws SQLException {
        String sql = "UPDATE Users SET age = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, newAge);
            preparedStatement.setString(2, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Age updated successfully for username: " + username);
            } else {
                System.out.println("No user found with the username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating age: " + e.getMessage());
            throw e;
        }
    }

    public void updateEmail(String username, String newEmail) throws SQLException {
        String sql = "UPDATE Users SET email = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newEmail);  // nuovo nome
            preparedStatement.setString(2, username); // username dell'utente da aggiornare

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Email updated successfully for username: " + username);
            } else {
                System.out.println("No user found with the username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating email: " + e.getMessage());
            throw e;
        }
    }

    public void updatePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE Users SET password = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPassword);  // nuovo nome
            preparedStatement.setString(2, username); // username dell'utente da aggiornare

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Password updated successfully for username: " + username);
            } else {
                System.out.println("No user found with the username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating Password: " + e.getMessage());
            throw e;
        }
    }

    public void updateSex(String username, String newSex) throws SQLException {
        String sql = "UPDATE Users SET sex = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newSex);  // nuovo nome
            preparedStatement.setString(2, username); // username dell'utente da aggiornare

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sex updated successfully for username: " + username);
            } else {
                System.out.println("No user found with the username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error while updating Sex: " + e.getMessage());
            throw e;
        }
    }

    public void updatePayment(String username, String cardNumber, String expirationDate, String cvv, String ownerName, String ownerSurname, float withheld) throws SQLException {
        // Disattiva l'autocommit per gestire la transazione
        connection.setAutoCommit(false);

        try {
            // 1. Inserisci o aggiorna il PaymentMethod
            String paymentMethodSql = "INSERT INTO PaymentMethod (cardNumber, expirationDate, cvv, ownerName, ownerSurname, withheld) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (cardNumber) DO UPDATE " +
                    "SET expirationDate = EXCLUDED.expirationDate, " +
                    "cvv = EXCLUDED.cvv, " +
                    "ownerName = EXCLUDED.ownerName, " +
                    "ownerSurname = EXCLUDED.ownerSurname, " +
                    "withheld = EXCLUDED.withheld";

            try (PreparedStatement paymentStatement = connection.prepareStatement(paymentMethodSql)) {
                paymentStatement.setString(1, cardNumber);
                paymentStatement.setDate(2, java.sql.Date.valueOf(expirationDate));
                paymentStatement.setString(3, cvv);
                paymentStatement.setString(4, ownerName);
                paymentStatement.setString(5, ownerSurname);
                paymentStatement.setFloat(6, withheld);

                paymentStatement.executeUpdate();
            }

            // 2. Aggiorna il metodo di pagamento dell'utente
            String updateUserSql = "UPDATE Users SET paymentMethod = ? WHERE username = ?";

            try (PreparedStatement userStatement = connection.prepareStatement(updateUserSql)) {
                userStatement.setString(1, cardNumber);
                userStatement.setString(2, username);

                int rowsAffected = userStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Payment method updated successfully for user: " + username);
                } else {
                    System.out.println("No user found with the username: " + username);
                }
            }

            // Conferma la transazione
            connection.commit();
        } catch (SQLException e) {
            // Rollback in caso di errore
            connection.rollback();
            System.err.println("Error while updating payment method: " + e.getMessage());
            throw e;
        } finally {
            // Riattiva l'autocommit
            connection.setAutoCommit(true);
        }
    }


}