package ua.edu.ucu.decorator;

public class RomashkaFlower implements Item {
    @Override
    public double price() {
        return 8.0;
    }

    @Override
    public String getDescription() {
        return "Romashka (chamomile) flower";
    }
}