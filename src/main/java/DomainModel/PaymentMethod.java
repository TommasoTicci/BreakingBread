package main.java.DomainModel;

public class PaymentMethod {
    private String ownerName;
    private String ownerSurname;
    private String cardNumber;
    private String cardExpiryDate;
    private String cardCVV;
    private float withheld;
    //todo pay method refund

    public PaymentMethod() {};

    public PaymentMethod(String ownerName,String ownerSurname, String cardNumber, String cardExpiryDate, String cardCVV) {
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.cardCVV = cardCVV;
    };

    //GETTER
    public String getOwnerName() {return ownerName;}
    public String getOwnerSurname() {return ownerSurname;}
    public String getCardNumber() {return cardNumber;}
    public String getCardExpiryDate() {return cardExpiryDate;}
    public String getCardCVV() {return cardCVV;}
    public float getWithheld() {return withheld;}

    //SETTER
    public void setOwnerName(String ownerName) {this.ownerName = ownerName;}
    public void setOwnerSurname(String ownerSurname) {this.ownerSurname = ownerSurname;}
    public void setCardNumber(String cardNumber) {this.cardNumber = cardNumber;}
    public void setCardExpiryDate(String cardExpiryDate) {this.cardExpiryDate = cardExpiryDate;}
    public void setCardCVV(String cardCVV) {this.cardCVV = cardCVV;}
    public void setWithheld(float withheld) {this.withheld = withheld;}
}
