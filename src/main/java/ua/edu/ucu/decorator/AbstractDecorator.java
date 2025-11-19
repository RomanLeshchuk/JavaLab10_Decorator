package ua.edu.ucu.decorator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractDecorator implements Document {
    protected Document document;

    @Override
    public String parse() {
        return document.parse();
    }
}