package ua.edu.ucu.decorator;

import java.util.ArrayList;
import java.util.List;

public class FlowerBucket implements Item {
    private List<Item> items = new ArrayList<>();

    public void addFlower(Item item) {
        items.add(item);
    }

    @Override
    public double price() {
        return items.stream()
                .mapToDouble(Item::price)
                .sum();
    }

    @Override
    public String getDescription() {
        if (items.isEmpty()) {
            return "Empty flower bucket";
        }
        
        StringBuilder description = new StringBuilder("Flower bucket containing: ");
        for (int i = 0; i < items.size(); i++) {
            description.append(items.get(i).getDescription());
            if (i < items.size() - 1) {
                description.append(", ");
            }
        }
        return description.toString();
    }
}