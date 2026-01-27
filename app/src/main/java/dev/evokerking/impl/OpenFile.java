package dev.evokerking.impl;

import dev.evokerking.command.Command;
import dev.evokerking.command.OpenFileStore;

import java.io.IOException;

public class OpenFile implements Command {

    @Override
    public String execute(String args) {
        String name = args == null ? "" : args.trim();
        if (name.isEmpty()) {
            return "Usage: " + getUsage();
        }

        OpenFileStore store = OpenFileStore.getInstance();
        if (store.isOpen(name)) {
            return "File already open: " + name;
        }

        try {
            String content = store.open(name);
            return "Opened '" + name + "' (" + content.length() + " bytes)";
        } catch (IOException e) {
            return "Error opening file '" + name + "': " + e.getMessage();
        }
    }

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public String getDescription() {
        return "Opens a file into the interactive session (keeps it in memory)";
    }

    @Override
    public String getUsage() {
        return "open <file-path> - Loads the file into the session";
    }

}
