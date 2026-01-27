package dev.evokerking.command;

/**
 * Parser for command input lines.
 */
public class CommandParser {
    
    /**
     * Parse a command line into command name and arguments.
     *
     * @param line The input line
     * @return A CommandInput object containing the command name and arguments
     */
    public static CommandInput parse(String line) {
        String[] parts = line.split("\\s+", 2);
        String commandName = parts[0];
        String commandArgs = parts.length > 1 ? parts[1] : "";
        return new CommandInput(commandName, commandArgs);
    }
}
