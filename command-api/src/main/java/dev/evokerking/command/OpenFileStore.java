package dev.evokerking.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory store for opened files in the interactive session.
 * Meant to be a singleton.
 */
public class OpenFileStore {
    private static final OpenFileStore INSTANCE = new OpenFileStore();
    private final Map<String, String> openFiles = new ConcurrentHashMap<>();

    private OpenFileStore() {
    }

    public static OpenFileStore getInstance() {
        return INSTANCE;
    }

    public boolean isOpen(String name) {
        return openFiles.containsKey(name);
    }

    public String open(String name) throws IOException {
        Path p = Paths.get(name);
        String content = "";
        if (Files.exists(p)) {
            content = new String(Files.readAllBytes(p));
        }
        openFiles.put(name, content);
        return content;
    }

    public String close(String name) {
        return openFiles.remove(name);
    }

    public String read(String name) {
        return openFiles.get(name);
    }

    public void write(String name, String content) throws IOException {
        Path p = Paths.get(name);
        Path parent = p.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(p, content.getBytes());
        openFiles.put(name, content);
    }
}
