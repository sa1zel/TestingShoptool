public class Item{
    private String name;
    private double oldPrice;
    private double newPrice;
    private int discount;

    public Item(String name, double oldPrice, double newPrice, int discount) {
        this.name = name;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public int getDiscount() {
        return discount;
    }
}