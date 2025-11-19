package ua.edu.ucu.decorator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

class DocumentDecoratorTest {

    private Document mockDocument;
    private static final String TEST_CONTENT = "This is test document content";

    @BeforeEach
    void setUp() {
        mockDocument = mock(Document.class);
        when(mockDocument.parse()).thenReturn(TEST_CONTENT);
    }

    @Test
    void testAbstractDecoratorDelegation() {
        Document decorator = new AbstractDecorator(mockDocument) {};
        
        String result = decorator.parse();
        
        assertEquals(TEST_CONTENT, result);
        verify(mockDocument, times(1)).parse();
    }

    @Test
    void testTimedDocumentMeasuresTime() {
        Document slowDocument = mock(Document.class);
        when(slowDocument.parse()).thenAnswer(invocation -> {
            Thread.sleep(100);
            return TEST_CONTENT;
        });

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Document timedDocument = new TimedDocument(slowDocument);
        String result = timedDocument.parse();

        assertEquals(TEST_CONTENT, result);
        String output = outContent.toString();
        assertTrue(output.contains("Document parsing took"));
        assertTrue(output.contains("ms"));
        
        System.setOut(System.out);
    }

    @Test
    void testTimedDocumentReturnsCorrectContent() {
        Document timedDocument = new TimedDocument(mockDocument);
        
        String result = timedDocument.parse();
        
        assertEquals(TEST_CONTENT, result);
        verify(mockDocument, times(1)).parse();
    }

    @Test
    void testCachedDocumentCachesMissFirstCall(@TempDir Path tempDir) {
        SmartDocument smartDocument = mock(SmartDocument.class);
        smartDocument.gcsPath = "gs://test-bucket/test-doc.pdf";
        when(smartDocument.parse()).thenReturn(TEST_CONTENT);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Document cachedDocument = new CachedDocument(smartDocument);
        
        String result1 = cachedDocument.parse();
        assertEquals(TEST_CONTENT, result1);
        assertTrue(outContent.toString().contains("Cache miss"));
        
        System.setOut(System.out);
    }

    @Test
    void testCachedDocumentCacheHitSecondCall() {
        SmartDocument smartDocument = mock(SmartDocument.class);
        smartDocument.gcsPath = "gs://test-bucket/test-doc-2.pdf";
        when(smartDocument.parse()).thenReturn(TEST_CONTENT);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        Document cachedDocument = new CachedDocument(smartDocument);
        
        cachedDocument.parse();
        
        outContent.reset();
        System.setOut(new PrintStream(outContent));
        
        String result2 = cachedDocument.parse();
        assertEquals(TEST_CONTENT, result2);
        assertTrue(outContent.toString().contains("Retrieved from cache"));
        
        verify(smartDocument, times(1)).parse();
        
        System.setOut(System.out);
    }

    @Test
    void testCombinedTimedAndCachedDocument() {
        SmartDocument smartDocument = mock(SmartDocument.class);
        smartDocument.gcsPath = "gs://test-bucket/combined-test.pdf";
        when(smartDocument.parse()).thenReturn(TEST_CONTENT);

        Document cachedDocument = new CachedDocument(smartDocument);
        Document timedCachedDocument = new TimedDocument(cachedDocument);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String result = timedCachedDocument.parse();
        assertEquals(TEST_CONTENT, result);
        
        String output = outContent.toString();
        assertTrue(output.contains("Document parsing took"));
        assertTrue(output.contains("Cache miss"));
        
        outContent.reset();
        
        result = timedCachedDocument.parse();
        assertEquals(TEST_CONTENT, result);
        
        output = outContent.toString();
        assertTrue(output.contains("Document parsing took"));
        assertTrue(output.contains("Retrieved from cache"));
        
        System.setOut(System.out);
    }

    @Test
    void testNestedDecorators() {
        Document level1 = new AbstractDecorator(mockDocument) {
            @Override
            public String parse() {
                return "[Level1: " + super.parse() + "]";
            }
        };
        
        Document level2 = new AbstractDecorator(level1) {
            @Override
            public String parse() {
                return "[Level2: " + super.parse() + "]";
            }
        };

        String result = level2.parse();
        assertEquals("[Level2: [Level1: " + TEST_CONTENT + "]]", result);
    }
}