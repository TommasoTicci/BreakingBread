package main;

import main.java.BusinessLogic.*;
import main.java.DomainModel.Item;
import main.java.DomainModel.Order;
import main.java.DomainModel.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

//FIXME dobbiamo pensare come gestire l'OwnerName e l'OwnerSurname del PaymentMethod

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        loginHandler();
    }

    public static void loginHandler() throws SQLException, ClassNotFoundException {
        LoginController loginController = new LoginController();
        String in;
        Scanner s = new Scanner(System.in);

        do{
            System.out.println("BREAKING BREAD\n Type the following numbers to choose your action:\n  1. Login\n  2. Register\n  3. Admin access\n  0. Exit");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    Scanner sl = new Scanner(System.in);
                    System.out.println("Enter Your Username:");
                    String username = sl.nextLine();
                    System.out.println("Enter Your Password:");
                    String password = sl.nextLine();
                    User user = loginController.login(username, password);
                    if (user != null) {
                        userHomeHandler(user);
                    }
                    else {
                        System.out.println("(!) - Username or password is incorrect.");
                    }
                    break;
                }
                case "2": {
                    String[] registration = register();
                    User user = null;
                    if (registration != null) {
                        if (registration[7] == null) {
                            user = loginController.register(registration[0], registration[1], registration[2], registration[3], Integer.parseInt(registration[4]), registration[5], registration[6]);
                        }
                        else {
                            user = loginController.register(registration[0], registration[1], registration[2], registration[3], Integer.parseInt(registration[4]), registration[5], registration[6], registration[7], registration[8], registration[9], registration[10], registration[11]);
                        }
                    }
                    if (user != null) {
                        userHomeHandler(user);
                    }
                    else {
                        System.out.println("(!) - An error has occurred.");
                    }
                    break;
                }
                case "3": {
                    Scanner sa = new Scanner(System.in);
                    System.out.print("Admin password:\n");
                    String password = sa.nextLine();
                    if(loginController.admin(password) != null){
                        adminHomeHandler();
                    }
                    else{
                        System.out.println("(!) - Incorrect password.");
                    }
                    break;
                }
                case "0": {
                    System.out.println("\nThank you for using Breaking Bread!");
                    return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        }while (true);
    }

    public static void userHomeHandler(User user) throws SQLException, ClassNotFoundException {
        String in;
        Scanner s = new Scanner(System.in);

        System.out.println("Welcome " + user.getUsername()+"!");
        do {
            System.out.println("HOME PAGE\n Type the following numbers to choose your action:\n  1. View menu\n  2. Manage cart\n  3. View your orders\n  4. Manage profile\n  0. Logout");
            in = s.nextLine();
            switch (in) {
                case "1": {userMenuHandler(); break;}
                case "2": {userCartHandler(user); break;}
                case "3": {userOrdersHandler(user); break;}
                case "4": {userProfilePageHandler(user); break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void userMenuHandler() throws SQLException, ClassNotFoundException {
        UserViewMenuController vmc = new UserViewMenuController();
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("BREAKING BREAD MENU\n Type the following numbers to choose your action:\n  1. View menu\n  2. View offers\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {printItems(vmc.viewMenu()); break;}
                case "2": {printItems(vmc.viewOffers()); break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void userCartHandler(User user) throws SQLException, ClassNotFoundException {
        UserViewMenuController vmc = new UserViewMenuController();
        UserManagerOrderController moc =  new UserManagerOrderController(user);
        String in;
        int q;
        boolean found;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("YOUR CART\n Type the following numbers to choose your action:\n  1. Add item to cart\n  2. Remove item from cart\n  3. View cart\n  4. Confirm and Pay\n  5. Clear cart\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    try {
                        Scanner sai = new Scanner(System.in);
                        System.out.println("Please enter the id of the item you would like to add to your cart:");
                        int id = sai.nextInt();
                        do {
                            System.out.println("Please enter the quantity:");
                            q = sai.nextInt();
                        } while (q < 0 || q > 10);
                        found = moc.addItem(id, q);
                        if (!found) {
                            System.out.println("(!) - Item not found.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;
                }
                case "2": {
                    try {
                        Scanner sri = new Scanner(System.in);
                        System.out.println("Please enter the id of the item you would like to remove from your cart:");
                        int id = sri.nextInt();
                        moc.removeItem(id);
                    } catch (InputMismatchException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;
                }
                case "3": {
                    printItems(moc.viewCurrentOrder());
                    break;
                }
                case "4": {
                    moc.confirmCurrentOrder();
                    break;
                }
                case "5": {
                    moc.cancelCurrentOrder();
                    break;
                }
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void userOrdersHandler(User user) throws SQLException, ClassNotFoundException {
        UserManagerOrderController moc = new UserManagerOrderController(user);
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("YOUR ORDERS\n Type the following numbers to choose your action:\n  1. View orders\n  2. Refund order (if possible)\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    printOrder(moc.viewOrders(), true);
                    break;}
                case "2": {
                    try {
                        System.out.println("Please enter the id of the order you would like to refund:");
                        Scanner so = new Scanner(System.in);
                        String id = so.nextLine();
                        moc.cancelOrder(Integer.parseInt(id));
                    } catch (NumberFormatException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void userProfilePageHandler(User user) throws SQLException, ClassNotFoundException {
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("YOUR PROFILE\n Type the following numbers to choose your action:\n  1. View profile\n  2. Edit profile\n  3. Edit payment method\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    printProfile(user);
                    break;}
                case "2": {userProfileEditorHandler(user); break;}
                case "3": {userPaymentMethodHandler(user); break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void userProfileEditorHandler(User user) throws SQLException, ClassNotFoundException {
        UserProfileController upc = new UserProfileController(user);
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("PROFILE EDITOR\n Type the following numbers to choose your action:\n  1. Change password\n  2. Edit username\n  3. Edit email\n  4. Edit name\n  5. Edit surname\n  6. Edit age\n  7. Edit sex\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    Scanner sp = new Scanner(System.in);
                    System.out.println("Enter new Password:");
                    String npw = sp.nextLine();
                    System.out.println("Confirm new Password:");
                    String cpw = sp.nextLine();
                    if (npw.equals(cpw) && !(npw.isEmpty())) {
                        upc.updatePassword(npw);
                    }
                    else {
                        System.out.println("(!) - Passwords do not match.");
                    }
                    break;}
                case "2": {
                    Scanner su = new Scanner(System.in);
                    System.out.println("Enter new Username:");
                    String nu = su.nextLine();
                    if (nu == null || nu.isEmpty()){
                        System.out.println("(!) - Invalid input.");
                    } else {
                        upc.updateUsername(nu);
                    }
                    break;}
                case "3": {
                    Scanner se = new Scanner(System.in);
                    System.out.println("Enter new Email:");
                    String ne = se.nextLine();
                    if (ne == null || !ne.contains("@")){
                        System.out.println("(!) - Invalid input.");
                    } else {
                        upc.updateEmail(ne);
                    }
                    break;}
                case "4": {
                    Scanner sn = new Scanner(System.in);
                    System.out.println("Enter new Name:");
                    String nn = sn.nextLine();
                    if (nn == null || nn.isEmpty()){
                        System.out.println("(!) - Invalid input.");
                    } else {
                        upc.updateName(nn);
                    }
                    break;}
                case "5": {
                    Scanner ss = new Scanner(System.in);
                    System.out.println("Enter new Surname:");
                    String ns = ss.nextLine();
                    if (ns == null || ns.isEmpty()){
                        System.out.println("(!) - Invalid input.");
                    } else {
                        upc.updateSurname(ns);
                    }
                    break;}
                case "6": {
                    try {
                        Scanner sa = new Scanner(System.in);
                        System.out.println("Enter new Age:");
                        String na = sa.nextLine();
                        if (na == null || Integer.parseInt(na) < 6 || Integer.parseInt(na) > 130) {
                            System.out.println("(!) - Invalid input.");
                        } else {
                            upc.updateAge(Integer.parseInt(na));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;}
                case "7": {
                    Scanner sg = new Scanner(System.in);
                    System.out.println("Enter new Sex:");
                    String ng = sg.nextLine();
                    if (!(ng.equals("M")) && !(ng.equals("F")) && !(ng.equals("O")) && !(ng.equals("X"))){
                        System.out.println("(!) - Invalid input");
                    } else {
                        upc.updateGender(ng);
                    }
                    break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void userPaymentMethodHandler(User user) throws SQLException, ClassNotFoundException {
        UserProfileController upc = new UserProfileController(user);
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("PAYMENT METHOD MANAGER\n Type the following numbers to choose your action:\n  1. Update payment method\n  2. Remove payment method\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    String[] paymentMethodV = setPaymentMethod();
                    if (user.getPaymentMethod() != null) {
                        upc.changePaymentMethod(user.getPaymentMethod().getCardNumber(), paymentMethodV[2], paymentMethodV[3], paymentMethodV[4], paymentMethodV[0], paymentMethodV[1]);
                    } else {
                        upc.setPaymentMethod(paymentMethodV[2], paymentMethodV[3], paymentMethodV[4], paymentMethodV[0], paymentMethodV[1]);
                    }
                    break;}
                case "2": {
                    if (user.getPaymentMethod() == null) {
                        System.out.println("(!) - No payment method found.");
                    } else {
                        upc.removePaymentMethod(user.getPaymentMethod().getCardNumber());
                    }
                    break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void adminHomeHandler() throws SQLException, ClassNotFoundException {
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("ADMIN PAGE\n Type the following numbers to choose your action:\n  1. Manage users\n  2. Manage menu\n  3. Manage orders\n  4. Manage database\n  0. Logout");
            in = s.nextLine();
            switch (in) {
                case "1": {adminUsersHandler(); break;}
                case "2": {adminMenuHandler(); break;}
                case "3": {adminOrdersHandler(); break;}
                case "4": {adminDatabaseHandler(); break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void adminUsersHandler() throws SQLException, ClassNotFoundException {
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("ADMIN - USER MANAGER\n Type the following numbers to choose your action:\n  1. View users\n  2. Search user\n  3. Remove user\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    AdminUserController auc = new AdminUserController();
                    printUsers(auc.viewUsers());
                    break;}
                case "2": {
                    AdminUserController auc = new AdminUserController();
                    Scanner su = new Scanner(System.in);
                    System.out.println("Enter username:");
                    String username = su.nextLine();
                    printUsers(auc.searchUser(username));
                    break;}
                case "3": {
                    AdminUserController auc = new AdminUserController();
                    Scanner su = new Scanner(System.in);
                    System.out.println("Enter username:");
                    String username = su.nextLine();
                    auc.removeUser(username);
                    break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void adminMenuHandler() throws SQLException, ClassNotFoundException {
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("ADMIN - MENU MANAGER\n Type the following numbers to choose your action:\n  1. View menu\n  2. Add item\n  3. Remove item\n  4. View offers\n  5. Edit item\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    AdminItemController aic = new AdminItemController();
                    printItems(aic.viewMenu());
                    break;}
                case "2": {
                    AdminItemController aic = new AdminItemController();
                    Scanner sa = new Scanner(System.in);
                    System.out.println("Enter item name:");
                    String name = sa.nextLine();
                    System.out.println("Enter item description:");
                    String description = sa.nextLine();
                    String type;
                    do{
                        System.out.println("Select item type: [1 = Food, 2 = Drink, 3 = Dessert, 4 = Other]");
                        type = sa.nextLine();
                        if (!(type.equals("1")) && !(type.equals("2")) && !(type.equals("3")) && !(type.equals("4"))){
                            System.out.println("(!) - Invalid input");
                        }
                    } while(!(type.equals("1")) && !(type.equals("2")) && !(type.equals("3")) && !(type.equals("4")));
                    switch (type) {
                        case "1": {type = "Food"; break;}
                        case "2": {type = "Drink"; break;}
                        case "3": {type = "Dessert"; break;}
                        case "4": {type = "Other"; break;}
                    }
                    String price;
                    do {
                        try {
                            System.out.println("Enter item price:");
                            price = sa.nextLine();
                            if (Float.parseFloat(price) < 0) {
                                System.out.println("(!) - Invalid input");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("(!) - Invalid input");
                            price = "-1";
                        }
                    } while (Float.parseFloat(price) < 0);
                    String discount;
                    do {
                        try {
                            System.out.println("Enter item discount percentage:");
                            discount = sa.nextLine();
                            if (Integer.parseInt(discount) < 0 || Integer.parseInt(discount) > 100) {
                                System.out.println("(!) - Invalid input");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("(!) - Invalid input");
                            discount = "-1";
                        }
                    } while (Integer.parseInt(discount) < 0 || Integer.parseInt(discount) > 100);
                    aic.addItemToMenu(name, description, type, Float.parseFloat(price), Integer.parseInt(discount));
                    break;}
                case "3": {
                    try{
                        AdminItemController aic = new AdminItemController();
                        Scanner sr = new Scanner(System.in);
                        System.out.println("Enter item id:");
                        int id = sr.nextInt();
                        aic.removeItemFromMenu(id);
                    } catch (InputMismatchException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;}
                case "4": {
                    AdminItemController aic = new AdminItemController();
                    printItems(aic.viewOffers());
                    break;}
                case "5": {
                    try {
                        AdminItemController aic = new AdminItemController();
                        Scanner so = new Scanner(System.in);
                        System.out.println("Enter item id:");
                        String id = so.nextLine();
                        String option;
                        do {
                            System.out.println("Select option: [1 = Description, 2 = Price, 3 = Discount]");
                            option = so.nextLine();
                        } while (!(option.equals("1")) && !(option.equals("2")) && !(option.equals("3")));
                        System.out.println("Enter new value:");
                        String edit = so.nextLine();
                        aic.editItem(Integer.parseInt(id), option, edit);
                    } catch (NumberFormatException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;}
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);

    }

    public static void adminOrdersHandler() throws SQLException, ClassNotFoundException {
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("ADMIN - ORDER MANAGER\n Type the following numbers to choose your action:\n  1. View orders\n  2. Search order by Id\n  3. Filter orders by user Id\n  4. Update order\n  5. Cancel order\n  6. Delete completed orders\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    AdminOrderController aoc = new AdminOrderController();
                    System.out.println("View details? [y to confirm]");
                    String details = s.nextLine();
                    printOrder(aoc.viewOrders(), details.equals("y"));
                    break;}
                case "2": {
                    AdminOrderController aoc = new AdminOrderController();
                    Scanner so = new Scanner(System.in);
                    ArrayList<Order> orders = new ArrayList<>();
                    try {
                        System.out.println("Enter order id:");
                        String id = so.nextLine();
                        if (aoc.viewOrder(Integer.parseInt(id)) != null) {
                            orders.add(aoc.viewOrder(Integer.parseInt(id)));
                        } else {
                            orders = null;
                        }
                        printOrder(orders, true);
                    } catch (NumberFormatException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;}
                case "3": {
                    AdminOrderController aoc = new AdminOrderController();
                    Scanner su = new Scanner(System.in);
                    try {
                        System.out.println("Enter user id:");
                        String id = su.nextLine();
                        System.out.println("View details? [y to confirm]");
                        String details = s.nextLine();
                        printOrder(aoc.viewOrdersByUser(Integer.parseInt(id)), details.equals("y"));
                    } catch (NumberFormatException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;}
                case "4": {
                    AdminOrderController aoc = new AdminOrderController();
                    Scanner su = new Scanner(System.in);
                    try {
                        System.out.println("Enter order id:");
                        String id = su.nextLine();
                        System.out.println("Enter new status [quick choice: 1 = Received, 2 = Preparing, 3 = Completed]");
                        String status = su.nextLine();
                        switch (status) {
                            case "1": {status = "Received"; break;}
                            case "2": {status = "Preparing"; break;}
                            case "3": {status = "Completed"; break;}
                            default: {System.out.println("(!) - Invalid input."); break;}
                        }
                        aoc.updateOrderStatus(Integer.parseInt(id), status);
                    } catch (NumberFormatException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;}
                case "5": {
                    AdminOrderController aoc = new AdminOrderController();
                    Scanner su = new Scanner(System.in);
                    try {
                        System.out.println("Enter order id:");
                        String id = su.nextLine();
                        Order order = aoc.viewOrder(Integer.parseInt(id));
                        String refund = "n";
                        if (order == null) {
                            System.out.println("(!) - Order not found.");
                            break;
                        }
                        if (order.getStatus().equals("Received")) {
                            System.out.println("Refund? [y to confirm]");
                            refund = s.nextLine();
                        }
                        aoc.cancelOrder(Integer.parseInt(id), refund.equals("y"));
                    } catch (NumberFormatException e) {
                        System.out.println("(!) - Invalid input.");
                    }
                    break;}
                case "6": {
                    AdminOrderController aoc = new AdminOrderController();
                    aoc.deleteCompletedOrders();
                    break;
                }
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static void adminDatabaseHandler() throws SQLException, ClassNotFoundException {
        AdminDatabaseOptionsController ad = new AdminDatabaseOptionsController();
        LoginController lc = new LoginController();
        String in;
        Scanner s = new Scanner(System.in);

        do {
            System.out.println("MANAGE DATABASE\n Type the following numbers to choose your action:\n  1. Clear and reset database\n  2. Insert 'dummy data'\n  3. Change password\n  0. Go back");
            in = s.nextLine();
            switch (in) {
                case "1": {
                    Scanner sp  = new Scanner(System.in);
                    System.out.println("Enter admin Password again:");
                    String pw = sp.nextLine();
                    if(lc.admin(pw) != null){
                        ad.clearDatabase();
                    }
                    else{
                        System.out.println("(!) - Incorrect password.");
                    }
                    break;
                }
                case "2": {ad.dummyDataGenerator(); break;}
                case "3": {
                    Scanner sp = new Scanner(System.in);
                    System.out.println("Enter new Password:");
                    String npw = sp.nextLine();
                    System.out.println("Confirm new Password:");
                    String cpw = sp.nextLine();
                    if (npw.equals(cpw)) {
                        ad.updatePassword(npw);
                    }
                    else {
                        System.out.println("(!) - Passwords do not match.");
                    }
                    break;
                }
                case "0": {return;}
                default: {System.out.println("(!) - Invalid input. Please try again.");}
            }
        } while (true);
    }

    public static String[] register() {
        Scanner s = new Scanner(System.in);
        String name, surname, sex, username, email, age, password, confirmPassword, pm, cardnum, cardexp, cardcvv, cardownername, cardownersurname;

        System.out.println("Welcome to Breaking Bread!");
        do {
            System.out.println("Please enter your name:");
            name = s.nextLine();
            System.out.println("Please enter your surname:");
            surname = s.nextLine();
            if (name == null || surname == null || name.isEmpty() || surname.isEmpty()){
                System.out.println("(!) - Invalid input.");
            }
        } while (name == null || surname == null || name.isEmpty() || surname.isEmpty());
        do {
            System.out.println("Please enter your sex [M = male, F = female, O = other, X = unspecified]: ");
            sex = s.nextLine();
            if (!(sex.equals("M")) && !(sex.equals("F")) && !(sex.equals("O")) && !(sex.equals("X"))){
                System.out.println("(!) - Invalid input");
            }
        } while(!(sex.equals("M")) && !(sex.equals("F")) && !(sex.equals("O")) && !(sex.equals("X")));
        do {
            try {
                System.out.println("Please enter your age:");
                age = s.nextLine();
                if (!(Integer.parseInt(age) >= 6 && Integer.parseInt(age) <= 130)) {
                    System.out.println("(!) - Invalid input");
                }
            } catch (NumberFormatException e) {
                System.out.println("(!) - Invalid input");
                age = "-1";
            }
        } while (Integer.parseInt(age) < 6 || Integer.parseInt(age) > 130);
        do  {
            System.out.println("Please enter your email:");
            email = s.nextLine();
            if (email == null || !email.contains("@")){
                System.out.println("(!) - Invalid input.");
            }
        } while (email == null || !email.contains("@"));
        do  {
            System.out.println("Please enter your username:");
            username = s.nextLine();
            if (username == null || username.isEmpty()){
                System.out.println("(!) - Invalid input.");
            }
        } while (username == null || username.isEmpty());
        do {
            System.out.println("Please enter your password:");
            password = s.nextLine();
            System.out.println("Confirm password:");
            confirmPassword = s.nextLine();
            if (!confirmPassword.equals(password)){
                System.out.println("(!) - Passwords do not match.");
            } else if (password.isEmpty()) {
                System.out.println("(!) - Invalid input.");
            }
        } while (password == null || password.isEmpty() || !confirmPassword.equals(password));
        System.out.println("Would you like to add your payment method? (you can always add it later) [y to confirm]");
        pm  = s.nextLine();
        if(pm.equals("y")){
            String[] paymentMethod = setPaymentMethod();
            cardnum = paymentMethod[2];
            cardexp = paymentMethod[3];
            cardcvv = paymentMethod[4];
            cardownername = paymentMethod[0];
            cardownersurname = paymentMethod[1];
        }
        else {
            cardnum = null;
            cardexp = null;
            cardcvv = null;
            cardownername = null;
            cardownersurname = null;
        }

        return new String[] {name, surname, username, email, age, password, sex, cardnum, cardexp, cardcvv, cardownername, cardownersurname};
    }

    public static String[] setPaymentMethod() {
        Scanner s = new Scanner(System.in);
        String name, surname, cardnum, cardexp, cardcvv, mm, yy;
        do {
            do {
                System.out.println("Please enter owner name:");
                name = s.nextLine();
                System.out.println("Please enter owner surname:");
                surname = s.nextLine();
                if (name == null || surname == null || name.isEmpty() || surname.isEmpty()) {
                    System.out.println("(!) - Invalid input.");
                }
            } while (name == null || surname == null || name.isEmpty() || surname.isEmpty());
            do {
                System.out.println("Please enter your card number:");
                cardnum = s.nextLine();
                try {
                    Long.parseLong(cardnum);
                } catch (NumberFormatException e) {
                    cardnum = "";
                }
                if (cardnum.length() != 16){
                    System.out.println("(!) - Invalid input.");
                }
            } while (cardnum.length() != 16);
            do {
                try {
                    System.out.println("Please enter your card expired month [mm]:");
                    mm = s.nextLine();
                    System.out.println("Please enter your card expired year [yy]:");
                    yy = s.nextLine();
                    if (Integer.parseInt(mm) < 1 || Integer.parseInt(mm) > 12 || Integer.parseInt(yy) < 21 || Integer.parseInt(yy) > 99) {
                        System.out.println("(!) - Invalid input.");
                    }
                } catch (NumberFormatException e) {
                    mm = "-1";
                    yy = "-1";
                }
            } while (Integer.parseInt(mm) < 1 || Integer.parseInt(mm) > 12 || Integer.parseInt(yy) < 21 || Integer.parseInt(yy) > 99);
            cardexp = mm + "/" + yy;
            do {
                System.out.println("Please enter your card cvv:");
                cardcvv = s.nextLine();
                try {
                    Integer.parseInt(cardcvv);
                } catch (NumberFormatException e) {
                    cardcvv = "";
                }
                if (cardcvv.length() != 3){
                    System.out.println("(!) - Invalid input.");
                }
            } while (cardcvv.length() != 3);
            if (cardexp.length() != 5){
                System.out.println("(!) - Invalid input.");
            }
        } while (cardexp.length() != 5);
        return new String[] {name, surname, cardnum, cardexp, cardcvv};
    }

    public static void printUsers(ArrayList<User> users) {
        System.out.printf("\n%-5s | %-20s | %-20s | %-20s | %-30s | %-5s | %-5s | %-20s%n",
                "ID", "Name", "Surname", "Username", "Email", "Age", "Sex", "Card Number");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
        if (users == null || users.isEmpty()) {
            System.out.println(" no results");
            System.out.println(" ");
            return;
        }
        for(User user : users){
            if (user.getPaymentMethod() == null) {
                System.out.printf("%-5d | %-20s | %-20s | %-20s | %-30s | %-5d | %-5s | %-20s%n",
                        user.getId(), user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), user.getAge(), user.getSex(), "null");
            } else {
                System.out.printf("%-5d | %-20s | %-20s | %-20s | %-30s | %-5d | %-5s | %-20s%n",
                        user.getId(), user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), user.getAge(), user.getSex(), user.getPaymentMethod().getCardNumber());
            }
        }
        if (users.isEmpty()){
            System.out.println(" no results");
        }
        System.out.println(" ");
    }

    public static void printItems(ArrayList<Item> items) {
        System.out.printf("\n%-5s | %-15s | %-50s | %-10s | %-10s | %-8s%n",
                "ID", "Name", "Description", "Type", "Price (€)", "Discount");
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        if (items == null || items.isEmpty()) {
            System.out.println(" no results");
            System.out.println(" ");
            return;
        }
        for(Item item : items){
            System.out.printf("%-5d | %-15s | %-50s | %-10s | %-10.2f | %-8d%%%n",
                    item.getItemId(), item.getName(), item.getDescription(), item.getType(), item.getPrice(), item.getDiscountPercentage());
        }
        if (items.isEmpty()){
            System.out.println(" no results");
        }
        System.out.println(" ");
    }

    public static void printOrder(ArrayList<Order> orders, boolean details) {
        System.out.printf("\n%-5s | %-20s | %-10s | %-15s | %-15s%n",
                "ID", "User", "User id", "Status", "Price (€)");
        System.out.println("---------------------------------------------------------------------------");
        if (orders == null || orders.isEmpty()) {
            System.out.println(" no results");
            System.out.println(" ");
            return;
        }
        for(Order order : orders){
            System.out.printf("%-5d | %-20s | %-10s | %-15s | %-15f%n",
                    order.getOrderId(), order.getUser().getUsername(), order.getUser().getId(), order.getStatus(), order.getTotal());
            if (details) {
                for (Item item : order.getItems()) {
                    System.out.printf("- Item: %-3d : %-10s%n", item.getItemId(), item.getName());
                }
                System.out.println("---------------------------------------------------------------------------");
            }
        }
        if (orders.isEmpty()){
            System.out.println(" no results");
        }
        System.out.println(" ");
    }

    public static void printProfile(User user) {
        System.out.println("\nPERSONAL INFORMATION");
        System.out.println("Name: " + user.getName());
        System.out.println("Surname: " + user.getSurname());
        System.out.println("Age: " + user.getAge());
        System.out.println("Sex: " + user.getSex());
        System.out.println("\nLOGIN INFORMATION");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());
        System.out.println("\nPAYMENT INFORMATION");
        if (user.getPaymentMethod() != null) {
            String cardNumber = user.getPaymentMethod().getCardNumber();
            String maskedCard = "************" + cardNumber.substring(12);
            System.out.println("Owner Name: " + user.getPaymentMethod().getOwnerName());
            System.out.println("Owner Surname: " + user.getPaymentMethod().getOwnerSurname());
            System.out.println("Card Number: " + maskedCard);
        } else {
            System.out.println("No payment method added.");
        }
        System.out.println(" ");
    }
}