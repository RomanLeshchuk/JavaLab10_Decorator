package ua.edu.ucu.decorator;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CachedDocument extends AbstractDecorator {
    private static final String DB_URL = "jdbc:sqlite:document_cache.db";
    
    public CachedDocument(Document document) {
        super(document);
        initializeDatabase();
    }

    @SneakyThrows
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE TABLE IF NOT EXISTS document_cache (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "document_path TEXT UNIQUE NOT NULL," +
                        "content TEXT NOT NULL," +
                        "cached_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
        }
    }

    @Override
    @SneakyThrows
    public String parse() {
        String documentPath = getDocumentPath();
        
        // Try to get from cache first
        String cachedContent = getFromCache(documentPath);
        if (cachedContent != null) {
            System.out.println("Retrieved from cache: " + documentPath);
            return cachedContent;
        }
        
        // If not in cache, parse using the wrapped document
        System.out.println("Cache miss, parsing document: " + documentPath);
        String content = super.parse();
        
        // Store in cache
        storeInCache(documentPath, content);
        
        return content;
    }

    private String getDocumentPath() {
        // Extract path from the wrapped document
        if (document instanceof SmartDocument) {
            return ((SmartDocument) document).gcsPath;
        }
        // For other decorators, recursively find the SmartDocument
        Document current = document;
        while (current instanceof AbstractDecorator) {
            Document wrapped = ((AbstractDecorator) current).document;
            if (wrapped instanceof SmartDocument) {
                return ((SmartDocument) wrapped).gcsPath;
            }
            current = wrapped;
        }
        return "unknown";
    }

    @SneakyThrows
    private String getFromCache(String documentPath) {
        String sql = "SELECT content FROM document_cache WHERE document_path = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, documentPath);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("content");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving from cache: " + e.getMessage());
        }
        
        return null;
    }

    @SneakyThrows
    private void storeInCache(String documentPath, String content) {
        String sql = "INSERT OR REPLACE INTO document_cache (document_path, content) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, documentPath);
            pstmt.setString(2, content);
            pstmt.executeUpdate();
            
            System.out.println("Stored in cache: " + documentPath);
        } catch (SQLException e) {
            System.err.println("Error storing in cache: " + e.getMessage());
        }
    }
}