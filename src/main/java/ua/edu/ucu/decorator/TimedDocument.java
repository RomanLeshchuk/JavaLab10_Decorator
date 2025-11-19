package ua.edu.ucu.decorator;

public class TimedDocument extends AbstractDecorator {
    public TimedDocument(Document document) {
        super(document);
    }

    @Override
    public String parse() {
        long startTime = System.currentTimeMillis();
        String result = super.parse();
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        System.out.println("Document parsing took " + duration + " ms");
        
        return result;
    }
}
