package main.java.DomainModel;

public class Item {
    private int itemId;
    private String name, description, type;
    private float price;
    private int discountPercentage;

    //CONSTRUCTORS
    public Item() {};
    public Item(int itemId, String name, String description, String type, float price, int discountPercentage) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.discountPercentage = discountPercentage;
    }

    //GETTERS
    public int getItemId() { return itemId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public float getPrice() { return price; }
    public int getDiscountPercentage() { return discountPercentage; }

    //SETTERS
    public void setItemId(int itemId) { this.itemId = itemId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public void setPrice(float price) { this.price = price; }
    public void setDiscountPercentage(int discountPercentage) { this.discountPercentage = discountPercentage; }
}
