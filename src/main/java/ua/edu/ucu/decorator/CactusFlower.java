package ua.edu.ucu.decorator;

public class CactusFlower implements Item {
    @Override
    public double price() {
        return 10.0;
    }

    @Override
    public String getDescription() {
        return "Cactus flower";
    }
}