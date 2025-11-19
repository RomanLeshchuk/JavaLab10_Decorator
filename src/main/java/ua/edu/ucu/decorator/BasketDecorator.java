package ua.edu.ucu.decorator;

public class BasketDecorator extends ItemDecorator {
    private static final double BASKET_COST = 4.0;

    public BasketDecorator(Item item) {
        super(item);
    }

    @Override
    public double price() {
        return BASKET_COST + item.price();
    }

    @Override
    public String getDescription() {
        return item.getDescription() + " in a basket";
    }
}
