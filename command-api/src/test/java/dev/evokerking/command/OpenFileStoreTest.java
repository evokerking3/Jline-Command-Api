package dev.evokerking.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class OpenFileStoreTest {
    private OpenFileStore store;

    @BeforeEach
    void setUp() {
        store = OpenFileStore.getInstance();
    }

    @Test
    void testGetInstance() {
        OpenFileStore store1 = OpenFileStore.getInstance();
        OpenFileStore store2 = OpenFileStore.getInstance();
        assertSame(store1, store2, "OpenFileStore should be a singleton");
    }

    @Test
    void testOpenNonexistentFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        String content = store.open(filePath.toString());
        
        assertEquals("", content, "Should return empty string for non-existent file");
        assertTrue(store.isOpen(filePath.toString()), "File should be marked as open");
    }

    @Test
    void testOpenExistingFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        String originalContent = "Hello, World!";
        Files.writeString(filePath, originalContent);
        
        String content = store.open(filePath.toString());
        assertEquals(originalContent, content, "Should read existing file content");
    }

    @Test
    void testIsOpenTrue(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        assertFalse(store.isOpen(filePath.toString()), "File should not be open initially");
        
        store.open(filePath.toString());
        assertTrue(store.isOpen(filePath.toString()), "File should be open after calling open()");
    }

    @Test
    void testIsOpenFalse() {
        String fileName = "nonexistent_file_xyz.txt";
        assertFalse(store.isOpen(fileName), "File that was never opened should not be open");
    }

    @Test
    void testCloseFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        String content = "Test content";
        Files.writeString(filePath, content);
        
        store.open(filePath.toString());
        assertTrue(store.isOpen(filePath.toString()));
        
        String closedContent = store.close(filePath.toString());
        assertEquals(content, closedContent, "Should return the content when closing");
        assertFalse(store.isOpen(filePath.toString()), "File should not be open after closing");
    }

    @Test
    void testCloseNonexistentFile() {
        String result = store.close("nonexistent_file.txt");
        assertNull(result, "Closing non-open file should return null");
    }

    @Test
    void testReadFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        String content = "File content here";
        Files.writeString(filePath, content);
        
        store.open(filePath.toString());
        String readContent = store.read(filePath.toString());
        assertEquals(content, readContent, "Should read the file content");
    }

    @Test
    void testReadNonexistentFile() {
        String result = store.read("never_opened.txt");
        assertNull(result, "Reading non-open file should return null");
    }

    @Test
    void testWriteNewFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("new_file.txt");
        String content = "New content";
        
        store.write(filePath.toString(), content);
        
        assertTrue(Files.exists(filePath), "File should be created");
        String fileContent = Files.readString(filePath);
        assertEquals(content, fileContent, "File should contain the written content");
        assertTrue(store.isOpen(filePath.toString()), "File should be marked as open");
    }

    @Test
    void testWriteExistingFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        String originalContent = "Original";
        Files.writeString(filePath, originalContent);
        
        String newContent = "Updated content";
        store.write(filePath.toString(), newContent);
        
        String fileContent = Files.readString(filePath);
        assertEquals(newContent, fileContent, "File should contain the new content");
        assertEquals(newContent, store.read(filePath.toString()), "Store should have the new content");
    }

    @Test
    void testWriteWithNestedDirectories(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("nested/deep/path/file.txt");
        String content = "Nested file content";
        
        store.write(filePath.toString(), content);
        
        assertTrue(Files.exists(filePath), "Nested file should be created");
        assertEquals(content, Files.readString(filePath), "File should contain the written content");
    }

    @Test
    void testOpenAndRead(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        String content = "Test content";
        Files.writeString(filePath, content);
        
        String openedContent = store.open(filePath.toString());
        String readContent = store.read(filePath.toString());
        
        assertEquals(openedContent, readContent, "Open and read should return same content");
    }

    @Test
    void testWriteAndRead(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        String content = "Write and read content";
        
        store.write(filePath.toString(), content);
        String readContent = store.read(filePath.toString());
        
        assertEquals(content, readContent, "Should write and read the same content");
    }

    @Test
    void testMultipleFiles(@TempDir Path tempDir) throws IOException {
        Path file1 = tempDir.resolve("file1.txt");
        Path file2 = tempDir.resolve("file2.txt");
        
        Files.writeString(file1, "Content 1");
        Files.writeString(file2, "Content 2");
        
        store.open(file1.toString());
        store.open(file2.toString());
        
        assertTrue(store.isOpen(file1.toString()));
        assertTrue(store.isOpen(file2.toString()));
        
        assertEquals("Content 1", store.read(file1.toString()));
        assertEquals("Content 2", store.read(file2.toString()));
    }

    @Test
    void testOverwriteOpenedFile(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        Files.writeString(filePath, "Original");
        
        store.open(filePath.toString());
        store.write(filePath.toString(), "Updated");
        
        assertEquals("Updated", store.read(filePath.toString()), "Store should have updated content");
        assertEquals("Updated", Files.readString(filePath), "File should have updated content");
    }

    @Test
    void testEmptyFileContent(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("empty.txt");
        
        store.write(filePath.toString(), "");
        
        assertEquals("", store.read(filePath.toString()), "Should handle empty content");
        assertEquals("", Files.readString(filePath), "File should be empty");
    }

    @Test
    void testLargeFileContent(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("large.txt");
        String largeContent = "x".repeat(10000);
        
        store.write(filePath.toString(), largeContent);
        
        assertEquals(largeContent, store.read(filePath.toString()), "Should handle large content");
    }

    @Test
    void testFileWithSpecialCharacters(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("special.txt");
        String content = "Special chars: !@#$%^&*()\\n\\t\"'";
        
        store.write(filePath.toString(), content);
        
        assertEquals(content, store.read(filePath.toString()), "Should preserve special characters");
    }
}
