package dev.evokerking.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for managing and dispatching commands.
 * This class is a singleton - use getInstance() to access the instance.
 */
public class CommandRegistry {
    private static final CommandRegistry instance = new CommandRegistry();
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private CommandRegistry() {
    }

    /**
     * Get the singleton instance of CommandRegistry.
     *
     * @return The singleton instance
     */
    public static CommandRegistry getInstance() {
        return instance;
    }

    /**
     * Register a command in the registry.
     *
     * @param command The command to register
     */
    public void register(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    /**
     * Check if a command is registered.
     *
     * @param commandName The name of the command
     * @return true if the command exists, false otherwise
     */
    public boolean exists(String commandName) {
        return commands.containsKey(commandName.toLowerCase());
    }

    /**
     * Execute a command by name with the given arguments.
     *
     * @param commandName The name of the command to execute
     * @param args The arguments to pass to the command
     * @return The result of command execution, or an error message if command not found
     */
    public String execute(String commandName, String args) {
        Command command = commands.get(commandName.toLowerCase());
        if (command == null) {
            return "Command not found: " + commandName + ". Type 'help' for available commands.";
        }
        try {
            return command.execute(args);
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }

    /**
     * Get all registered commands.
     *
     * @return A map of command names to command objects
     */
    public Map<String, Command> getCommands() {
        return new HashMap<>(commands);
    }

    /**
     * Reset the registry by clearing all registered commands.
     * This method is primarily intended for testing purposes.
     */
    public void reset() {
        commands.clear();
    }


}
