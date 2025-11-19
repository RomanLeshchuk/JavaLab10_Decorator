package ua.edu.ucu.decorator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {
    
    private Item cactusFlower;
    private Item romashkaFlower;

    @BeforeEach
    void setUp() {
        cactusFlower = new CactusFlower();
        romashkaFlower = new RomashkaFlower();
    }

    @Test
    void testBasicFlowerPrice() {
        assertEquals(10.0, cactusFlower.price(), 0.01);
        assertEquals(8.0, romashkaFlower.price(), 0.01);
    }

    @Test
    void testBasicFlowerDescription() {
        assertEquals("Cactus flower", cactusFlower.getDescription());
        assertEquals("Romashka (chamomile) flower", romashkaFlower.getDescription());
    }

    @Test
    void testPaperDecorator() {
        Item decoratedFlower = new PaperDecorator(cactusFlower);
        assertEquals(23.0, decoratedFlower.price(), 0.01); // 10 + 13
        assertTrue(decoratedFlower.getDescription().contains("wrapped in paper"));
    }

    @Test
    void testBasketDecorator() {
        Item decoratedFlower = new BasketDecorator(romashkaFlower);
        assertEquals(12.0, decoratedFlower.price(), 0.01); // 8 + 4
        assertTrue(decoratedFlower.getDescription().contains("in a basket"));
    }

    @Test
    void testRibbonDecorator() {
        Item decoratedFlower = new RibbonDecorator(cactusFlower);
        assertEquals(50.0, decoratedFlower.price(), 0.01); // 10 + 40
        assertTrue(decoratedFlower.getDescription().contains("with ribbon"));
    }

    @Test
    void testMultipleDecorators() {
        Item decorated = new PaperDecorator(cactusFlower);
        decorated = new BasketDecorator(decorated);
        decorated = new RibbonDecorator(decorated);
        
        assertEquals(67.0, decorated.price(), 0.01); // 10 + 13 + 4 + 40
        String desc = decorated.getDescription();
        assertTrue(desc.contains("wrapped in paper"));
        assertTrue(desc.contains("in a basket"));
        assertTrue(desc.contains("with ribbon"));
    }

    @Test
    void testFlowerBucketEmpty() {
        FlowerBucket bucket = new FlowerBucket();
        assertEquals(0.0, bucket.price(), 0.01);
        assertEquals("Empty flower bucket", bucket.getDescription());
    }

    @Test
    void testFlowerBucketWithSingleFlower() {
        FlowerBucket bucket = new FlowerBucket();
        bucket.addFlower(cactusFlower);
        
        assertEquals(10.0, bucket.price(), 0.01);
        assertTrue(bucket.getDescription().contains("Cactus flower"));
    }

    @Test
    void testFlowerBucketWithMultipleFlowers() {
        FlowerBucket bucket = new FlowerBucket();
        bucket.addFlower(cactusFlower);
        bucket.addFlower(romashkaFlower);
        
        assertEquals(18.0, bucket.price(), 0.01); // 10 + 8
        String desc = bucket.getDescription();
        assertTrue(desc.contains("Cactus flower"));
        assertTrue(desc.contains("Romashka"));
    }

    @Test
    void testFlowerBucketWithDecoratedFlowers() {
        FlowerBucket bucket = new FlowerBucket();
        
        Item decoratedCactus = new PaperDecorator(cactusFlower);
        Item decoratedRomashka = new RibbonDecorator(romashkaFlower);
        
        bucket.addFlower(decoratedCactus);
        bucket.addFlower(decoratedRomashka);
        
        assertEquals(71.0, bucket.price(), 0.01); // (10+13) + (8+40)
    }

    @Test
    void testDecoratedBucket() {
        FlowerBucket bucket = new FlowerBucket();
        bucket.addFlower(cactusFlower);
        bucket.addFlower(romashkaFlower);
        
        Item decoratedBucket = new BasketDecorator(bucket);
        decoratedBucket = new RibbonDecorator(decoratedBucket);
        
        assertEquals(62.0, decoratedBucket.price(), 0.01); // (10+8) + 4 + 40
        String desc = decoratedBucket.getDescription();
        assertTrue(desc.contains("Flower bucket"));
        assertTrue(desc.contains("in a basket"));
        assertTrue(desc.contains("with ribbon"));
    }

    @Test
    void testComplexDecoratorChain() {
        Item item = new PaperDecorator(cactusFlower);
        item = new BasketDecorator(item);
        item = new RibbonDecorator(item);
        item = new PaperDecorator(item);
        
        assertEquals(80.0, item.price(), 0.01); // 10 + 13 + 4 + 40 + 13
    }
}