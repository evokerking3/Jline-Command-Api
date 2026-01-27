package dev.evokerking.impl;

import dev.evokerking.command.*;
import dev.evokerking.command.OpenFileStore;

public class CloseFile implements Command {

    @Override
    public String execute(String args) {
        String name = args == null ? "" : args.trim();
        if (name.isEmpty()) {
            return "Usage: " + getUsage();
        }

        OpenFileStore store = OpenFileStore.getInstance();
        if (!store.isOpen(name)) {
            return "File is not open: " + name;
        }

        store.close(name);
        return "Closed '" + name + "'";
    }

    @Override
    public String getName() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "Closes a previously opened file in the session";
    }

    @Override
    public String getUsage() {
        return "close <file-path> - Removes the file from the session";
    }

}
