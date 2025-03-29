package main.java.DomainModel;

import java.util.Scanner;

public class PaymentMethod {
    private String ownerName;
    private String ownerSurname;
    private String cardNumber;
    private String cardExpiryDate;
    private String cardCVV;
    private float withheld;

    //CONSTRUCTORS
    public PaymentMethod() {};
    public PaymentMethod(String ownerName,String ownerSurname, String cardNumber, String cardExpiryDate, String cardCVV) {
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.cardCVV = cardCVV;
    };

    //GETTERS
    public String getOwnerName() {return ownerName;}
    public String getOwnerSurname() {return ownerSurname;}
    public String getCardNumber() {return cardNumber;}
    public String getCardExpiryDate() {return cardExpiryDate;}
    public String getCardCVV() {return cardCVV;}
    public float getWithheld() {return withheld;}

    //SETTERS
    public void setOwnerName(String ownerName) {this.ownerName = ownerName;}
    public void setOwnerSurname(String ownerSurname) {this.ownerSurname = ownerSurname;}
    public void setCardNumber(String cardNumber) {this.cardNumber = cardNumber;}
    public void setCardExpiryDate(String cardExpiryDate) {this.cardExpiryDate = cardExpiryDate;}
    public void setCardCVV(String cardCVV) {this.cardCVV = cardCVV;}
    public void setWithheld(float withheld) {this.withheld = withheld;}

    //METHODS
    public void pay(float amount) {
        int tries = 0;
        Scanner scanner = new Scanner(System.in);

        amount += amount * withheld;

        System.out.println("Paying order - Total amount: " + amount);
        System.out.println("Type y to confirm");

        String confirmation = scanner.nextLine().toLowerCase();

        if (!confirmation.equals("y")){
            System.out.println("Payment cancelled");
            throw new RuntimeException("Payment cancelled");
        }

        System.out.println("Payment in progress. Please wait.");
        System.out.println("Enter your credit card CVV");

        String cVV;

        do {
            cVV = scanner.nextLine();
            if (!(cVV.equals(cardCVV))) {
                System.out.println("Error: Invalid CVV. Please try again.");
            }
            tries++;
        } while (!(cVV.equals(cardCVV)) && tries < 5);

        if (tries == 5) {
            System.out.println("Error: too many tries.");
            throw new RuntimeException("Payment cancelled");
        }

        System.out.println("Payment successful!");
    }

    public void refund(Order order) {
        String cr = "Received";
        String co = order.getStatus();
        float total =  order.getTotal() + order.getTotal() * withheld;

        if (!(cr.equals(co))) {
            System.out.println("Refund failed. Your order cannot be refunded.");
            throw new RuntimeException("Refund failed");
        }

        System.out.println("Refunding order: " + order.getOrderId() + " - Total amount: " + total);
        System.out.println("Refund complete. Please wait.");
        System.out.println("Refund successful!");
    }
}
