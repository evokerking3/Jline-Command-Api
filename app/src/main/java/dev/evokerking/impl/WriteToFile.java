package dev.evokerking.impl;

import dev.evokerking.command.Command;
import dev.evokerking.command.OpenFileStore;

import java.io.IOException;

public class WriteToFile implements Command {

    @Override
    public String execute(String args) {
        if (args == null) {
            return "Usage: " + getUsage();
        }

        // Support using '|' as separator between filename and content: "file.txt | content"
        String[] parts = args.split("\\|", 2);
        String name = parts.length > 0 ? parts[0].trim() : "";
        String content = parts.length > 1 ? parts[1] : "";

        // If no '|' provided, try split by whitespace: first token filename, rest content
        if (name.isEmpty()) {
            return "Usage: " + getUsage();
        }

        if (content.isEmpty()) {
            // No content provided
            return "No content provided to write to '" + name + "'";
        }

        OpenFileStore store = OpenFileStore.getInstance();
        try {
            store.write(name, content);
            return "Wrote " + content.getBytes().length + " bytes to '" + name + "'";
        } catch (IOException e) {
            return "Error writing to file '" + name + "': " + e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "write";
    }

    @Override
    public String getDescription() {
        return "Writes text to a file; use '|' to separate filename and content";
    }

    @Override
    public String getUsage() {
        return "write <file-path> | <text> - Writes the text to the file (creates/overwrites)";
    }

}