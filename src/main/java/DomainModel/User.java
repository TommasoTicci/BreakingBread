package main.java.DomainModel;

import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String surname;
    private String username;
    private int age;
    private String sex;
    private String email;
    private String password;
    private PaymentMethod paymentMethod;
    private ArrayList<String> cart;

    //CONSTRUCTORS
    public User() {}


    public User(int id,String name,String surname,String username,int age,String sex,String email,String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.password = password;
        this.paymentMethod=null;
    }

    public User(int id,String name,String surname,String username,int age,String email,String password,String cardNumber,String cardExpiryDate,String cardCVV) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.age = age;
        this.email = email;
        this.password = password;
        this.paymentMethod = new PaymentMethod(name,surname,cardNumber,cardExpiryDate,cardCVV);
    }

    public User(String name,String surname,String username,int age,String email,String password,PaymentMethod paymentMethod) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.age = age;
        this.email = email;
        this.password = password;
        this.paymentMethod = paymentMethod;
    }

    public User(String name,String surname,String username,int age,String email,String password,String cardNumber,String cardExpiryDate,String cardCVV) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.age = age;
        this.email = email;
        this.password = password;
        this.paymentMethod = new PaymentMethod(name,surname,cardNumber,cardExpiryDate,cardCVV);
    }

    public User(String name,String surname,String username,int age,String email,String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    //GETTERS
    public int getId() {return id;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public String getUsername() {return username;}
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
    public  ArrayList<String> getCart() {return cart;}

    //SETTERS
    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
    public void setUsername(String username) {this.username = username;}
    public void setAge(int age) {this.age = age;}
    public void setSex(String sex) {this.sex = sex;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setPaymentMethod(PaymentMethod paymentMethod) {this.paymentMethod = paymentMethod;}
    public void setPaymentMethod(String cardNumber,String cardExpiryDate,String cardCVV) {
        this.paymentMethod = new PaymentMethod(name,surname,cardNumber,cardExpiryDate,cardCVV);
    }
    public void setCart(ArrayList<String> cart) {this.cart = cart;}
}



