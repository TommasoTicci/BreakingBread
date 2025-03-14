package main.java.DomainModel;

public class User {
    private int id;
    private String name;
    private String surname;
    private int age;
    private String sex;
    private String email;
    private String password;
    private PaymentMethod paymentMethod;
    private String[]cart;

    public User() {}

    public User(String name, String surname, int age, String email, String password, PaymentMethod paymentMethod) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.paymentMethod = paymentMethod;
    }

    public User(String name, String surname, int age, String email, String password,String cardNumber,String cardExpiryDate,String cardCVV) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.paymentMethod = new PaymentMethod(name,surname,cardNumber,cardExpiryDate,cardCVV);
    }

    public User(String name, String surname, int age, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    //GETTER
    public int getId() {return id;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public int getAge() {return age;}
    public String getSex() {return sex;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public PaymentMethod getPaymentMethod() {
        if(paymentMethod != null) {
            return paymentMethod;
        }
        else {
            return null;
        }
    }
    public String[] getCart() {return cart;}

    //SETTER
    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
    public void setAge(int age) {this.age = age;}
    public void setSex(String sex) {this.sex = sex;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setPaymentMethod(PaymentMethod paymentMethod) {this.paymentMethod = paymentMethod;}
    public void setPaymentMethod(String cardNumber,String cardExpiryDate,String cardCVV) {
        this.paymentMethod = new PaymentMethod(name,surname,cardNumber,cardExpiryDate,cardCVV);
    }
    public void setCart(String[] cart) {this.cart = cart;}
}



