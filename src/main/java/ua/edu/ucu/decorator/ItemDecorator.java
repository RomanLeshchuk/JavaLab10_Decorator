package ua.edu.ucu.decorator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ItemDecorator implements Item {
    protected Item item;

    @Override
    public abstract double price();

    @Override
    public abstract String getDescription();
}
