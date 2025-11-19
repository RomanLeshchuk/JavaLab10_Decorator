package ua.edu.ucu.decorator;

public class RibbonDecorator extends ItemDecorator {
    private static final double RIBBON_COST = 40.0;

    public RibbonDecorator(Item item) {
        super(item);
    }

    @Override
    public double price() {
        return RIBBON_COST + item.price();
    }

    @Override
    public String getDescription() {
        return item.getDescription() + " with ribbon";
    }
}