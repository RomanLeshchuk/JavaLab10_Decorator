package ua.edu.ucu.decorator;

public class DecoratorUsageExample {
    
    public static void main(String[] args) {
        demonstrateFlowerDecorators();
        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstrateDocumentDecorators();
    }

    private static void demonstrateFlowerDecorators() {
        System.out.println("FLOWER DECORATOR PATTERN DEMO");
        System.out.println("=".repeat(60));
        
        // Example 1: Simple flower
        System.out.println("\n1. Basic Cactus Flower:");
        Item cactus = new CactusFlower();
        System.out.println("   Description: " + cactus.getDescription());
        System.out.println("   Price: $" + cactus.price());
        
        // Example 2: Decorated flower
        System.out.println("\n2. Cactus Flower with Paper:");
        Item decoratedCactus = new PaperDecorator(cactus);
        System.out.println("   Description: " + decoratedCactus.getDescription());
        System.out.println("   Price: $" + decoratedCactus.price());
        
        // Example 3: Multiple decorators
        System.out.println("\n3. Cactus Flower with Paper, Basket, and Ribbon:");
        Item multiDecorated = new PaperDecorator(cactus);
        multiDecorated = new BasketDecorator(multiDecorated);
        multiDecorated = new RibbonDecorator(multiDecorated);
        System.out.println("   Description: " + multiDecorated.getDescription());
        System.out.println("   Price: $" + multiDecorated.price());
        
        // Example 4: Flower bucket
        System.out.println("\n4. Flower Bucket with Multiple Flowers:");
        FlowerBucket bucket = new FlowerBucket();
        bucket.addFlower(new CactusFlower());
        bucket.addFlower(new RomashkaFlower());
        bucket.addFlower(new PaperDecorator(new CactusFlower()));
        System.out.println("   Description: " + bucket.getDescription());
        System.out.println("   Price: $" + bucket.price());
        
        // Example 5: Decorated bucket
        System.out.println("\n5. Decorated Flower Bucket:");
        Item decoratedBucket = new BasketDecorator(bucket);
        decoratedBucket = new RibbonDecorator(decoratedBucket);
        System.out.println("   Description: " + decoratedBucket.getDescription());
        System.out.println("   Price: $" + decoratedBucket.price());
    }

    private static void demonstrateDocumentDecorators() {
        System.out.println("DOCUMENT DECORATOR PATTERN DEMO");
        System.out.println("=".repeat(60));
        
        // Note: This requires actual Google Cloud Vision setup
        // Using mock example for demonstration
        System.out.println("\n1. Basic SmartDocument:");
        System.out.println("   SmartDocument uses Google Cloud Vision API");
        System.out.println("   to extract text from images stored in GCS");
        
        System.out.println("\n2. TimedDocument (with SmartDocument):");
        System.out.println("   Measures how long the parsing takes");
        System.out.println("   Example output: 'Document parsing took 1234 ms'");
        
        System.out.println("\n3. CachedDocument (with SmartDocument):");
        System.out.println("   First call: Fetches from Google Cloud Vision");
        System.out.println("   Subsequent calls: Retrieves from SQLite cache");
        System.out.println("   Significantly improves performance for repeated access");
        
        System.out.println("\n4. Combined: TimedDocument + CachedDocument:");
        System.out.println("   Provides both timing metrics and caching");
        System.out.println("   First call: Timed GCS fetch + cache storage");
        System.out.println("   Second call: Timed cache retrieval (much faster!)");
        
        // Pseudo-code example
        System.out.println("\n5. Code Example:");
        System.out.println("   SmartDocument doc = new SmartDocument(\"gs://bucket/doc.pdf\");");
        System.out.println("   Document cached = new CachedDocument(doc);");
        System.out.println("   Document timed = new TimedDocument(cached);");
        System.out.println("   String content = timed.parse(); // Cached + Timed!");
    }
}