package ua.edu.ucu.decorator;

public class PaperDecorator extends ItemDecorator {
    private static final double PAPER_COST = 13.0;

    public PaperDecorator(Item item) {
        super(item);
    }

    @Override
    public double price() {
        return PAPER_COST + item.price();
    }

    @Override
    public String getDescription() {
        return item.getDescription() + " wrapped in paper";
    }
}